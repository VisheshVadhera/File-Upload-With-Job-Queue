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

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

public class FileUploadActivity extends AppCompatActivity {

    private static final int FILE_SELECT_REQUEST_CODE = 1001;
    private static final int READ_EXTERNAL_STORAGE_PERMISSIONS_REQUEST = 2001;

    private static final double NETWORK_DELAY_SECONDS = 1.5;
    private static final int NETWORK_DELAY_VARIANCE_PERCENT = 40;

    private Button button;
    private MockFileService mockFileService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);

        initializeView();

        setupRetrofit();
    }

    private void setupRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("baseUrl")
                .client(client)
                .build();

        NetworkBehavior behavior = NetworkBehavior.create();
        behavior.setDelay((long) NETWORK_DELAY_SECONDS, TimeUnit.SECONDS);
        behavior.setVariancePercent(NETWORK_DELAY_VARIANCE_PERCENT);

        MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior)
                .build();

        mockFileService = new MockFileService(mockRetrofit);
    }

    private void initializeView() {
        button = (Button) findViewById(R.id.btn_file_upload);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getPermissionToReadExternalStorage();
                } else {
                    openFileExplorer();
                }
            }
        });
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
