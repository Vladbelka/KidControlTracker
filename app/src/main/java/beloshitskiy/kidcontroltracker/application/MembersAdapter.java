package beloshitskiy.kidcontroltracker.application;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import beloshitskiy.kidcontroltracker.domain.CreateUser;
import beloshitskiy.kidcontroltracker.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder>
{
    ArrayList<CreateUser> nameList;
    Context c;

    MembersAdapter(ArrayList<CreateUser> nameList, Context c)
    {
        this.nameList = nameList;
        this.c = c;
    }


    @Override
    public int getItemCount() {
        return nameList.size();
    }

    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        MembersViewHolder membersViewHolder = new MembersViewHolder(v,c,nameList);
        return membersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MembersViewHolder holder, int position) {

        CreateUser currentUserObj = nameList.get(position);
        holder.name_txt.setText(currentUserObj.name);
        Picasso.get().load(currentUserObj.imageUrl).placeholder(R.drawable.default_profile).into(holder.circleImageView);
    }

    public static class MembersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView name_txt;
        CircleImageView circleImageView;
        View v;
        Context c;
        ArrayList<CreateUser> nameArrayList;
        FirebaseAuth auth;
        FirebaseUser user;

        public MembersViewHolder(View itemView, Context c, ArrayList<CreateUser> nameArrayList) {
            super(itemView);
            this.c = c;
            this.nameArrayList = nameArrayList;

            itemView.setOnClickListener(this);
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();

            name_txt = itemView.findViewById(R.id.item_title);
            circleImageView = itemView.findViewById(R.id.i11);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(c, "You have selected this user", Toast.LENGTH_LONG).show();
        }
    }
}
