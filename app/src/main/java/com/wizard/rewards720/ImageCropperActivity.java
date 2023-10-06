package com.wizard.rewards720;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class ImageCropperActivity extends AppCompatActivity {
    String intentCamUri, destinationUri, intentGalleryUri;
    Uri sourceCamUri, sourceGalleryUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_cropper);

        Intent intent = getIntent();
        intentCamUri = intent.getStringExtra("RAW_IMG");
        intentGalleryUri = intent.getStringExtra("GALLERY");

        if (intentGalleryUri==null){
            sourceCamUri = Uri.parse(intentCamUri);
        } else {
            sourceCamUri = Uri.parse(intentGalleryUri);
        }

//            sourceCamUri = Uri.parse(intentCamUri);

        destinationUri = new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();

        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCompressionQuality(100);

        UCrop.of(sourceCamUri, Uri.fromFile(new File(getCacheDir(), destinationUri)))
                .withOptions(options)
//                .withAspectRatio(1, 1)
                .withMaxResultSize(100000, 100000)
                .start(ImageCropperActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            if (data!=null){

            final Uri finalUri = UCrop.getOutput(data);
            Intent finalIntent = new Intent();
            if (finalUri != null)
                finalIntent.putExtra("FINAL_URI", finalUri.toString());
            setResult(2,finalIntent);
            finish();
            }

        } else if (requestCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Toast.makeText(this, "Process Terminated...", Toast.LENGTH_SHORT).show();
            finish();
        }else{

            Toast.makeText(this, "Process Terminated...", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}