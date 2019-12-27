package com.kyle.circuitgame.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyle.circuitgame.R;
import com.kyle.circuitgame.models.User;
import com.kyle.circuitgame.utils.UserFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserActivity extends AppCompatActivity {
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    public static final String EDIT_USER_EXTRA = "maze_game_user";
    private ImageView mProfileImage;
    private User mUser;
    private Uri mImageUri = Uri.EMPTY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mUser = new User("No Profile");
        mProfileImage = findViewById(R.id.activity_user_img_profile);
        Button cameraButton = findViewById(R.id.activity_user_btn_cam);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraIntent();
            }
        });
        final boolean edit = getIntent().getBooleanExtra(EDIT_USER_EXTRA, false);
        final EditText username = findViewById(R.id.activity_user_et_name);
        Button addButton = findViewById(R.id.activity_user_btn_save);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setUsername(username.getText().toString());
                if (edit) UserFile.getInstance(UserActivity.this).editUser(mUser);
                else UserFile.getInstance(UserActivity.this).addUser(mUser);
                finish();
            }
        });

        if (edit) {
            mUser = UserFile.getInstance(this).getCurrentUser();
            username.setText(mUser.getUsername());
            if (!mUser.getUri().equals(Uri.EMPTY)) {
                mProfileImage.setImageURI(mUser.getUri());
            }
            addButton.setText(R.string.save_user);
            TextView title = findViewById(R.id.activity_user_tv_title);
            title.setText(R.string.save_user);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            mProfileImage.setImageURI(mImageUri);
            mUser.setUri(mImageUri);
        }
    }

    @Override
    protected void onStop() {
        UserFile.getInstance(this).saveToFile(this);
        super.onStop();
    }

    private Uri createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, "jpg", storageDir);
        return FileProvider.getUriForFile(this, "com.kyle.circuitgame.fileprovider", image);
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) == null) return;
        try {
            mImageUri = createImageFile();
        } catch (IOException ex) {
            mImageUri = null;
        }
        if (mImageUri != null) {
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
        }
    }
}
