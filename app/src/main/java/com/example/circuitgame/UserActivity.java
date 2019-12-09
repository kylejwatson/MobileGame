package com.example.circuitgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserActivity extends AppCompatActivity {
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    public static final String EDIT_USER_EXTRA = "maze_game_user";
    private ImageView profileImage;
    private User user;

    Uri imageUri = Uri.EMPTY;
    private Uri createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return FileProvider.getUriForFile(this,"com.example.circuitgame.fileprovider", image);
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            try {
                imageUri = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (imageUri != null) {
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        imageUri);
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        final boolean edit = getIntent().getBooleanExtra(EDIT_USER_EXTRA, false);
        user = new User("No Profile");

        final EditText username = findViewById(R.id.usernameText);
        profileImage = findViewById(R.id.profileImage);
        Button cameraButton = findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraIntent();
            }
        });
        Button addButton = findViewById(R.id.saveButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(username.getText().toString());
                if (edit) UserFile.getInstance(UserActivity.this).editUser(user);
                else UserFile.getInstance(UserActivity.this).addUser(user);
                finish();
            }
        });

        if (edit) {
            user = UserFile.getInstance(this).getCurrentUser();
            username.setText(user.getUsername());
            if (!user.getUri().equals(Uri.EMPTY)) {
                profileImage.setImageURI(user.getUri());
            }
            addButton.setText("Save User");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            //don't compare the data to null, it will always come as  null because we are providing a file URI, so load with the imageFilePath we obtained before opening the cameraIntent
            profileImage.setImageURI(imageUri);
            user.setUri(imageUri);
            // If you are using Glide.
        }
    }

    @Override
    protected void onStop() {
        UserFile.getInstance(this).saveToFile(this);
        super.onStop();
    }
}
