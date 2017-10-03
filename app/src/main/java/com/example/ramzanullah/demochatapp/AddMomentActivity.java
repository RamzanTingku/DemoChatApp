package com.example.ramzanullah.demochatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddMomentActivity extends AppCompatActivity {

    private ImageButton addImage;
    private EditText addTitleET, addDescriptionET;
    private Button postMomentBTN;
    private Uri imageUri = null;
    private static final int GALLERY_REQUEST = 1;
    private StorageReference mStorageRef;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_moment);

        /*mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null){
                    moveToLogin();
                }
            }
        };*/

        mStorageRef = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("moments");

        addImage = (ImageButton) findViewById(R.id.add_image);
        addTitleET = (EditText) findViewById(R.id.et_add_title);
        addDescriptionET = (EditText) findViewById(R.id.et_add_description);
        postMomentBTN = (Button) findViewById(R.id.btn_post_moment);
        progressDialog = new ProgressDialog(this);

        postMomentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

    }

   /* @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);

    }*/

    private void moveToLogin() {
        Intent intent = new Intent(AddMomentActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

    private void startPosting() {
        progressDialog.setMessage("Uploading Post..");
        progressDialog.show();
        final String title = addTitleET.getText().toString().trim();
        final String description = addDescriptionET.getText().toString().trim();
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && imageUri != null){
            progressDialog.show();

            StorageReference filepath = mStorageRef.child("Moment Image").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            @SuppressWarnings("VisibleForTests")  Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            DatabaseReference newPost = databaseReference.push();
                            newPost.child("title").setValue(title);
                            newPost.child("desc").setValue(description);
                            newPost.child("image").setValue(downloadUrl.toString());


                            progressDialog.dismiss();

                            startActivity(new Intent(AddMomentActivity.this, ViewMomentActivity.class));
                            finish();


                            Toast.makeText(AddMomentActivity.this, "Post Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(AddMomentActivity.this, "Uploading Post Failed", Toast.LENGTH_SHORT).show();
                            // ...
                        }
                    });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            imageUri = data.getData();
            addImage.setImageURI(imageUri);

        }

    }

}
