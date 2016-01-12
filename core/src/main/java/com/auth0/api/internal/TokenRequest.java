package com.auth0.api.internal;

import android.os.Handler;

import com.auth0.api.ParameterizableRequest;
import com.auth0.core.Token;
import com.auth0.api.callback.BaseCallback;

import java.util.Map;

public class TokenRequest extends HandledRequest<Token> implements ParameterizableRequest<Token> {

    com.auth0.java.api.ParameterizableRequest<com.auth0.java.core.Token> request;

    public TokenRequest(Handler handler, com.auth0.java.api.ParameterizableRequest<com.auth0.java.core.Token> request) {
        super(handler);
        this.request = request;
    }

    @Override
    public ParameterizableRequest<Token> addParameters(Map<String, Object> parameters) {
        request.addParameters(parameters);
        return this;
    }

    @Override
    public ParameterizableRequest<Token> addHeader(String name, String value) {
        request.addHeader(name, value);
        return this;
    }

    @Override
    public void start(BaseCallback<Token> callback) {
        setCallback(callback);
        request.start(new com.auth0.java.api.callback.BaseCallback<com.auth0.java.core.Token>() {
            @Override
            public void onSuccess(final com.auth0.java.core.Token payload) {
                postOnSuccess(new Token(payload));
            }

            @Override
            public void onFailure(final Throwable error) {
                postOnFailure(error);
            }
        });
    }
}
