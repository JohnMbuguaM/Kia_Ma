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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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
    private FirebaseFirestore firebaseFirestore;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);




        Toolbar setupToolbar = findViewById(R.id.setupToolbar);
        setSupportActionBar(setupToolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();


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
                final String Ritwa = edit_Ritwa.getText().toString();
                final String Muhiriga = edit_Muhiriga.getText().toString();
                final String Mbari = edit_Mbari.getText().toString();
                final String  Rika = edit_Rika.getText().toString();
                final String Mwaki = edit_Mwaki.getText().toString();
                final String Thimu = edit_Thimu.getText().toString();

                if (!TextUtils.isEmpty(Ritwa) && !TextUtils.isEmpty(Muhiriga) && !TextUtils.isEmpty(Mbari) && !TextUtils.isEmpty(Rika) && !TextUtils.isEmpty(Mwaki) && !TextUtils.isEmpty(Thimu) && mainImageURI != null)
                {
                    pgBar.setVisibility(View.VISIBLE);
                    final String User_id = firebaseAuth.getCurrentUser().getUid();
                    StorageReference image_path = storageReference.child("profile_images").child(User_id + "jpg");
                    image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful())
                                {
                                    Task<Uri> image = task.getResult().getMetadata().getReference().getDownloadUrl();



                                    Map<String, String> userMap = new HashMap<>();
                                    userMap.put("image", image.toString());
                                    userMap.put("Ritwa", Ritwa);
                                    userMap.put("Muhiriga", Muhiriga);
                                    userMap.put("Mbari", Mbari);
                                    userMap.put("Rika", Rika);
                                    userMap.put("Mwaki", Mwaki);
                                    userMap.put("Thimu", Thimu);


                                    firebaseFirestore.collection("Users").document(User_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                Toast.makeText(SetupActivity.this, "Your profile was successfully updated", Toast.LENGTH_LONG).show();


                                                Toast.makeText(SetupActivity.this, "You have successfully updated your Profile ", Toast.LENGTH_LONG).show();
                                                Intent MainIntent = new Intent (SetupActivity.this, MainActivity.class);
                                                startActivity(MainIntent);


                                            } else {

                                                String error = task.getException().getMessage();
                                                Toast.makeText(SetupActivity.this, "FireStore Error" + error, Toast.LENGTH_LONG).show();


                                            }

                                            pgBar.setVisibility(View.INVISIBLE);
                                        }


                                    });


                                }else
                                    {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(SetupActivity.this, "Error" + error, Toast.LENGTH_LONG).show();

                                        pgBar.setVisibility(View.INVISIBLE);



                                }


                        }
                    });





                }else {
                    Toast.makeText(SetupActivity.this, "Please fill all the parts and select a photo" , Toast.LENGTH_LONG).show();

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

                        BringImagePicker();
                    }

                } else {

                    BringImagePicker();

                }
            }
        });



    }

    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(SetupActivity.this);
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

