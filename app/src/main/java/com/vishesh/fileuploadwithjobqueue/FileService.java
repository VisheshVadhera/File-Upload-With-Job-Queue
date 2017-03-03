package com.vishesh.fileuploadwithjobqueue;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileService {

    @Multipart
    @POST("file")
    Single<Boolean> uploadKycDocument(@Part("file") MultipartBody.Part part);
}
