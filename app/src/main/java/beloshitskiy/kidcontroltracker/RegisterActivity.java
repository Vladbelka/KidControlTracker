package beloshitskiy.kidcontroltracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class RegisterActivity extends AppCompatActivity {

    EditText e3_email;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        e3_email = (EditText)findViewById(R.id.editText3);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
    }

    public void goToPasswordActivity(View v)
    {
        dialog.setMessage("Checking email address");
        dialog.show();
        // check if this email is already registered
        auth.fetchProvidersForEmail(e3_email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {

                        if(task.isSuccessful())
                        {
                            dialog.dismiss();
                            boolean check = !task.getResult().getProviders().isEmpty();

                            if(!check)
                            {
                                //email doesn't exist, we can create user with this email
                                Intent myIntent = new Intent(RegisterActivity.this, PasswordActivity.class);
                                myIntent.putExtra("email", e3_email.getText().toString());
                                startActivity(myIntent);
                                finish();
                            }
                            else
                            {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(),"This email is already used", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}
