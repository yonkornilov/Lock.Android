/*
 * ApplicationInfoRequest.java
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
import com.auth0.core.Application;

public class ApplicationInfoRequest extends HandledRequest<Application> implements Request<Application> {

    com.auth0.java.api.Request<com.auth0.java.core.Application> request;

    public ApplicationInfoRequest(Handler handler, com.auth0.java.api.Request<com.auth0.java.core.Application> request) {
        super(handler);
        this.request = request;
    }

    @Override
    public void start(BaseCallback<Application> callback) {
        setCallback(callback);
        request.start(new com.auth0.java.api.callback.BaseCallback<com.auth0.java.core.Application>() {
            @Override
            public void onSuccess(final com.auth0.java.core.Application payload) {
                postOnSuccess(new Application(payload));
            }

            @Override
            public void onFailure(final Throwable error) {
                postOnFailure(error);
            }
        });
    }
}
