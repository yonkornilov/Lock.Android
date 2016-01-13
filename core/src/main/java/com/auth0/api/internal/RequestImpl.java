/*
 * Request.java
 *
 * Copyright (c) 2015 Auth0 (http://auth0.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.auth0.api.internal;

import android.os.Handler;

import com.auth0.api.Request;
import com.auth0.api.callback.BaseCallback;

/**
 * Defines a request that can be started
 * @param <T>
 */
public class RequestImpl<T> extends HandledRequest<T> implements Request<T> {

    protected com.auth0.java.api.Request<T> request;

    public RequestImpl(Handler handler, com.auth0.java.api.Request<T> request) {
        super(handler);
        this.request = request;
    }

    public void start(final BaseCallback<T> callback) {
        setCallback(callback);
        request.start(new com.auth0.java.api.callback.BaseCallback<T>() {
            @Override
            public void onSuccess(final T payload) {
                postOnSuccess(payload);
            }

            @Override
            public void onFailure(final Throwable error) {
                postOnFailure(error);
            }
        });
    }
}