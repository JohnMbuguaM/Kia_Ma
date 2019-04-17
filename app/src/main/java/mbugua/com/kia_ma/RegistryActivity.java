package mbugua.com.kia_ma;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistryActivity extends AppCompatActivity {
    private EditText rTextUsername, rTextpassword, rTextCfmPass;
    private Button RegisterButton;
    private TextView RegisterLogin;
    private CheckBox rshowpwd;
    private ProgressBar RegisterProgBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        mAuth = FirebaseAuth.getInstance();


        rTextUsername = findViewById(R.id.edittext_username);
        rTextpassword = findViewById(R.id.edittext_passward);
        rTextCfmPass = findViewById(R.id.edittext_cnf_passward);
        RegisterButton = findViewById(R.id.button_register);
        RegisterLogin = findViewById(R.id.register_login);
        RegisterProgBar = findViewById(R.id.reg_progressBar);

        RegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent loginIntent = new Intent(RegistryActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();

            }
        });


        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = rTextUsername.getText().toString();
                String pass = rTextpassword.getText().toString();
                String confirm_pass = rTextCfmPass.getText().toString();


                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) & !TextUtils.isEmpty(confirm_pass)) {


                    if (pass.equals(confirm_pass)) {


                        RegisterProgBar.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if (task.isSuccessful()) {

                                    Intent mainIntent = new Intent(RegistryActivity.this, SetupActivity.class);
                                    startActivity(mainIntent);
                                    finish();


                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(RegistryActivity.this, "Hari hadu wahetia Muthuri---> " + errorMessage, Toast.LENGTH_LONG).show();
                                }

                                RegisterProgBar.setVisibility(View.INVISIBLE);
                            }

                        });
                    } else {
                        Toast.makeText(RegistryActivity.this, "Muthuri Password ciaku citihanaine...cokera", Toast.LENGTH_LONG).show();


                    }


                }




            }



        });

        rshowpwd = findViewById(R.id.showpwd);
        rshowpwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b) {
                    rTextpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    rTextpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                if (b) {
                    rTextCfmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    rTextCfmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }


            }


        });
    }

}


