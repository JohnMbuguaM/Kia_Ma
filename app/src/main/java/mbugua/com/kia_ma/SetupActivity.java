package mbugua.com.kia_ma;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.os.Build.VERSION_CODES.M;

public class SetupActivity extends AppCompatActivity {

    private CircleImageView setupImage;
    private Uri mainImageURI = null;

    private EditText edit_Ritwa, edit_Muhiriga, edit_Mbari, edit_Rika, edit_Mwaki,edit_Thimu;
    private Button setup_button;
    private ProgressBar pgBar;



    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        storageReference= FirebaseStorage.getInstance().getReference();



        Toolbar setupToolbar = findViewById(R.id.setupToolbar);
        setSupportActionBar(setupToolbar);

        firebaseAuth = FirebaseAuth.getInstance();


        pgBar = findViewById(R.id.setup_progress);
        setupImage =findViewById(R.id.setup_image);
        edit_Ritwa = findViewById(R.id.editText_Ritwa);
        edit_Muhiriga = findViewById(R.id.editText_Muhiriga);
        edit_Mbari = findViewById(R.id.editText_Mbari);
        edit_Rika = findViewById(R.id.editText_Rika);
        edit_Mwaki = findViewById(R.id.editText_Mwaki);
        edit_Thimu = findViewById(R.id.editText_Thimu);

        setup_button = findViewById(R.id.Setup_button);
        setup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Ritwa = edit_Ritwa.getText().toString();
                String Muhiriga = edit_Muhiriga.getText().toString();
                String Mbari = edit_Mbari.getText().toString();
                String  Rika = edit_Rika.getText().toString();
                String Mwaki = edit_Mwaki.getText().toString();
                String Thimu = edit_Thimu.getText().toString();

                if (!TextUtils.isEmpty(Ritwa) && !TextUtils.isEmpty(Muhiriga) && !TextUtils.isEmpty(Mbari) && !TextUtils.isEmpty(Rika) && !TextUtils.isEmpty(Mwaki) && !TextUtils.isEmpty(Thimu) && mainImageURI != null)
                {
                    pgBar.setVisibility(View.VISIBLE);
                    String User_id = firebaseAuth.getCurrentUser().getUid();
                    StorageReference image_path = storageReference.child("profile_images").child(User_id + "jpg");
                    image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful())
                                {

                                    Task<Uri> u = task.getResult().getMetadata().getReference().getDownloadUrl();
                                    Toast.makeText(SetupActivity.this, "Upload successful " , Toast.LENGTH_LONG).show();


                                }else
                                    {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(SetupActivity.this, "Error" + error, Toast.LENGTH_LONG).show();


                                }
                            pgBar.setVisibility(View.INVISIBLE);

                        }
                    });





                }else {

                }
            }
        });
        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(SetupActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {

                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(SetupActivity.this);
                    }

                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                setupImage.setImageURI(mainImageURI);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }
}

