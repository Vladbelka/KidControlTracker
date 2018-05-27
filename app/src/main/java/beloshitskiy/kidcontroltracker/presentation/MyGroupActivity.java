package beloshitskiy.kidcontroltracker.presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import beloshitskiy.kidcontroltracker.R;
import beloshitskiy.kidcontroltracker.application.MembersAdapter;
import beloshitskiy.kidcontroltracker.domain.CreateUser;
import beloshitskiy.kidcontroltracker.domain.GroupJoin;

public class MyGroupActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    FirebaseAuth auth;
    FirebaseUser user;
    CreateUser createUser;
    ArrayList<CreateUser> nameList;
    ArrayList<GroupJoin> groupList;

    DatabaseReference reference, userReference;
    String groupMemberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        nameList = new ArrayList<>();
        groupList = new ArrayList<>();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        userReference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("GroupMembers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameList.clear();
                if(dataSnapshot.exists())
                {
                    for (DataSnapshot dss: dataSnapshot.getChildren())
                    {
                        groupMemberId = dss.child("groupMemberId").getValue(String.class);
                        userReference.child(groupMemberId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        createUser = dataSnapshot.getValue(CreateUser.class);
                                        nameList.add(createUser);
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        adapter = new MembersAdapter(nameList, getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
