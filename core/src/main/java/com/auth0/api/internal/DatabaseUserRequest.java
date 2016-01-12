package com.auth0.api.internal;


import android.os.Handler;

import com.auth0.api.ParameterizableRequest;
import com.auth0.api.callback.BaseCallback;
import com.auth0.core.DatabaseUser;

import java.util.Map;

public class DatabaseUserRequest extends HandledRequest<DatabaseUser> implements ParameterizableRequest<DatabaseUser> {

    com.auth0.java.api.ParameterizableRequest<com.auth0.java.core.DatabaseUser> request;

    public DatabaseUserRequest(Handler handler, com.auth0.java.api.ParameterizableRequest<com.auth0.java.core.DatabaseUser> request) {
        super(handler);
        this.request = request;
    }

    @Override
    public ParameterizableRequest<DatabaseUser> addParameters(Map<String, Object> parameters) {
        request.addParameters(parameters);
        return this;
    }

    @Override
    public ParameterizableRequest<DatabaseUser> addHeader(String name, String value) {
        request.addHeader(name, value);
        return this;
    }

    @Override
    public void start(BaseCallback<DatabaseUser> callback) {
        setCallback(callback);
        request.start(new com.auth0.java.api.callback.BaseCallback<com.auth0.java.core.DatabaseUser>() {
            @Override
            public void onSuccess(final com.auth0.java.core.DatabaseUser payload) {
                postOnSuccess(new DatabaseUser(payload.getEmail(), payload.getUsername(), payload.isEmailVerified()));
            }

            @Override
            public void onFailure(final Throwable error) {
                postOnFailure(error);
            }
        });
    }
}
