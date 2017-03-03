package com.vishesh.fileuploadwithjobqueue;

import android.app.Application;

import com.evernote.android.job.JobManager;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        JobManager.create(this)
                .addJobCreator(new FileUploadJobCreator());
    }
}
