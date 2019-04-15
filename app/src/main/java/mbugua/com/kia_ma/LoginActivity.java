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

public class LoginActivity extends AppCompatActivity {

    EditText mTextUsername, mTextpassword;
    Button mButtonLogin;
    TextView mTextViewRegister;
    CheckBox showpwd;

    private FirebaseAuth mAuth;
    private ProgressBar login_progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();





                mTextUsername = (EditText) findViewById(R.id.edittext_username);
                mTextpassword = (EditText) findViewById(R.id.edittext_passward);
                mTextViewRegister = (TextView) findViewById(R.id.textview_register);
                mButtonLogin = (Button) findViewById(R.id.button_login);
                login_progress = (ProgressBar) findViewById(R.id.login_progress);
                showpwd = findViewById(R.id.showpwd);


                mButtonLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String loginEmail = mTextUsername.getText().toString();
                        String loginPass = mTextpassword.getText().toString();

                         if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass)){
                             login_progress.setVisibility(View.VISIBLE);
                             mAuth.signInWithEmailAndPassword(loginEmail, loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                 @Override
                                 public void onComplete(@NonNull Task<AuthResult> task) {
                                     if (task.isSuccessful()){
                                         sendToMain();

                                     }else{
                                         String errorMessage = task.getException().getMessage();
                                         Toast.makeText(LoginActivity.this, "Hari Hadu Wahetia Muthuri  " + errorMessage, Toast.LENGTH_LONG).show();
                                     }
                                     login_progress.setVisibility(View.INVISIBLE);

                                 }

                             });
                             showpwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                 @Override
                                 public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                                     if (b) {
                                         mTextpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                     } else {
                                         mTextpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                     }
                                 }
                             });

                    }

            }

        });
    }

    private void sendToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}