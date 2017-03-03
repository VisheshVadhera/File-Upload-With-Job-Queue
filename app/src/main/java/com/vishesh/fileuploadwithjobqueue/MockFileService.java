package com.vishesh.fileuploadwithjobqueue;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.http.Part;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;

public class MockFileService implements FileService {

    private final MockRetrofit mockRetrofit;
    private final BehaviorDelegate<FileService> delegate;

    public MockFileService(MockRetrofit mockRetrofit) {
        this.mockRetrofit = mockRetrofit;
        this.delegate = mockRetrofit.create(FileService.class);
    }

    @Override
    public Single<Boolean> uploadKycDocument(@Part("file") MultipartBody.Part part) {
        return delegate.returningResponse(true)
                .uploadKycDocument(part);
    }
}
