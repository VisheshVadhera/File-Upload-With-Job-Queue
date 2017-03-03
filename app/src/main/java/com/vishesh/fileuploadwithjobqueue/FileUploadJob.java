package com.vishesh.fileuploadwithjobqueue;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;


public class FileUploadJob extends Job {

    public static final String TAG = "FileUploadJob";

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        return null;
    }

    public static void schedulerJob() {
        new JobRequest.Builder(TAG)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setPersisted(true);
    }
}
