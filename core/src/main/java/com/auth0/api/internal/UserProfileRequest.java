package com.auth0.api.internal;

import android.os.Handler;


import com.auth0.api.Request;
import com.auth0.core.UserProfile;
import com.auth0.api.callback.BaseCallback;

public class UserProfileRequest extends HandledRequest<UserProfile> implements Request<UserProfile> {

    com.auth0.java.api.Request<com.auth0.java.core.UserProfile> request;

    public UserProfileRequest(Handler handler, com.auth0.java.api.Request<com.auth0.java.core.UserProfile> request) {
        super(handler);
        this.request = request;
    }

    @Override
    public void start(BaseCallback<UserProfile> callback) {
        setCallback(callback);
        request.start(new com.auth0.java.api.callback.BaseCallback<com.auth0.java.core.UserProfile>() {
            @Override
            public void onSuccess(final com.auth0.java.core.UserProfile payload) {
                postOnSuccess(new UserProfile(payload));
            }

            @Override
            public void onFailure(final Throwable error) {
                postOnFailure(error);
            }
        });
    }
}
