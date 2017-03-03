package com.vishesh.fileuploadwithjobqueue;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class FileUploadJobCreator implements JobCreator {


    @Override
    public Job create(String tag) {
        switch (tag) {
            case FileUploadJob.TAG:
                return new FileUploadJob();
            default:
                return null;
        }
    }
}
