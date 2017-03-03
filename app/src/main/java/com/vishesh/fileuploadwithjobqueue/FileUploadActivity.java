package com.vishesh.fileuploadwithjobqueue;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FileUploadActivity extends AppCompatActivity {

    private static final int FILE_SELECT_REQUEST_CODE = 1001;
    private static final int READ_EXTERNAL_STORAGE_PERMISSIONS_REQUEST = 2001;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);

        button = (Button) findViewById(R.id.btn_file_upload);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileExplorer();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermissionToReadExternalStorage();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getPermissionToReadExternalStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

            }

            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_EXTERNAL_STORAGE_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFileExplorer();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void openFileExplorer() {
        Intent fileChooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileChooserIntent.addCategory(Intent.CATEGORY_OPENABLE);

        if (fileChooserIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(fileChooserIntent,
                    "No file explorer found"), FILE_SELECT_REQUEST_CODE);
        } else {
            showMessage("Unable to open file explorer");
        }
    }

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT)
                .show();
    }
}
