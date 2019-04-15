package mbugua.com.kia_ma;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class RegistryActivity extends AppCompatActivity {
    EditText mTextUsername, mTextpassword;
    Button mButtonLogin;
    TextView mTextViewRegister;
    CheckBox showpwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        showpwd = findViewById(R.id.showpwd);
        showpwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b) {
                    mTextpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mTextpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }


                mTextUsername = (EditText) findViewById(R.id.edittext_username);
                mTextpassword = (EditText) findViewById(R.id.edittext_passward);
                mTextViewRegister = (TextView) findViewById(R.id.textview_register);
                mButtonLogin = (Button) findViewById(R.id.button_login);



            }



        });
    }

}
