package beloshitskiy.kidcontroltracker.presentation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import beloshitskiy.kidcontroltracker.R;
import beloshitskiy.kidcontroltracker.domain.CreateUser;

public class InviteCodeActivity extends AppCompatActivity {


    String name, email, password, date, issharing, code;
    Uri imageUri;
    ProgressDialog progressDialog;

    TextView t1;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    StorageReference storageReference;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);
        t1 = (TextView)findViewById(R.id.textView);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        Intent myIntent = getIntent();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("User_images");


        if(myIntent!=null)
        {
            name = myIntent.getStringExtra("name");
            email = myIntent.getStringExtra("email");
            password = myIntent.getStringExtra("password");
            code = myIntent.getStringExtra("code");
            issharing = myIntent.getStringExtra("isSharing");
            imageUri = myIntent.getParcelableExtra("imageUri");

        }
        t1.setText(code);

    }

    public void registerUser(View v)
    {
        progressDialog.setMessage("Creating an account");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            user = auth.getCurrentUser();

                            //insert values in Firebase Realtime Database
                            CreateUser createUser = new CreateUser(name, email, password, code, "false", "na", "na", "na", user.getUid());

                            userId = user.getUid();

                            reference.child(userId).setValue(createUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                //save the image to firebase storage
                                            StorageReference sr = storageReference.child(user.getUid()+".jpg");
                                            sr.putFile(imageUri)
                                                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                            if(task.isSuccessful())
                                                            {

                                                                String download_image_path = task.getResult().getDownloadUrl().toString();
                                                                reference.child(user.getUid()).child("imageUrl").setValue(download_image_path)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if(task.isSuccessful())
                                                                                {
                                                                                    progressDialog.dismiss();
                                                                                    //Toast.makeText(getApplicationContext(),"Check email for verification",Toast.LENGTH_SHORT).show();

                                                                                    sendVerificationEmail();

                                                                                    Intent myIntent = new Intent(InviteCodeActivity.this,MainActivity.class);
                                                                                    startActivity(myIntent);
                                                                                }
                                                                                else
                                                                                {
                                                                                    progressDialog.dismiss();
                                                                                    Toast.makeText(getApplicationContext(),"An error occurred while creating account",Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });


                                                            }
                                                        }
                                                    });



                                            }
                                            else
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(),"Couldn't finish registration",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                        }
                    }
                });

    }

    public void sendVerificationEmail()
    {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "Email sent for verification", Toast.LENGTH_SHORT).show();
                            finish();
                            auth.signOut();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Couldn't send email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}