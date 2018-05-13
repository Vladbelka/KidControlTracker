package beloshitskiy.kidcontroltracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {

    String email;
    EditText e4_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        e4_password = (EditText)findViewById(R.id.editText4);

        Intent myIntent = getIntent();
        if(myIntent!=null)
        {
            email = myIntent.getStringExtra("email");
        }


    }
    public void goToNamePicActivity(View v)
    {
        if(e4_password.getText().toString().length()>=6 && e4_password.getText().toString().length()<=20)
        {
            Intent myIntent = new Intent(PasswordActivity.this, NameActivity.class);
            myIntent.putExtra("email",email);
            myIntent.putExtra("password", e4_password.getText().toString());
            startActivity(myIntent);
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Password must be between 6 and 20 characters in length", Toast.LENGTH_LONG).show();
        }
    }

}
