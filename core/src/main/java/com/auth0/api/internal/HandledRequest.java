package com.auth0.api.internal;

import android.os.Handler;

import com.auth0.api.callback.BaseCallback;

public abstract class HandledRequest<T> {

    private Handler handler;
    private BaseCallback<T> callback;

    protected HandledRequest(Handler handler) {
        this(handler, null);
    }

    protected HandledRequest(Handler handler, BaseCallback<T> callback) {
        this.handler = handler;
        this.callback = callback;
    }

    protected void setCallback(BaseCallback<T> callback) {
        this.callback = callback;
    }

    protected void postOnSuccess(final T result) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(result);
            }
        });
    }

    protected void postOnFailure(final Throwable error) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(error);
            }
        });
    }
}
