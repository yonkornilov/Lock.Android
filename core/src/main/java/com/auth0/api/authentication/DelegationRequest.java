/*
 * DelegationRequest.java
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

package com.auth0.api.authentication;

import android.os.Handler;

import com.auth0.api.callback.RefreshIdTokenCallback;

import java.util.Map;

/**
 * Represent a delegation request for Auth0 tokens that will yield a new 'id_token'
 */
public class DelegationRequest {

    Handler handler;
    com.auth0.java.api.authentication.DelegationRequest request;

    DelegationRequest(Handler handler, com.auth0.java.api.authentication.DelegationRequest request) {
        this.handler = handler;
        this.request = request;
    }

    /**
     * Add additional parameters to be sent in the request
     * @param parameters as a non-null dictionary
     * @return itself
     */
    public DelegationRequest addParameters(Map<String, Object> parameters) {
        request.addParameters(parameters);
        return this;
    }

    /**
     * Performs the HTTP request against Auth0 API
     * @param callback called either on success or failure
     */
    public void start(final RefreshIdTokenCallback callback) {
        request.start(new com.auth0.java.api.callback.RefreshIdTokenCallback() {
            @Override
            public void onSuccess(final String idToken, final String tokenType, final int expiresIn) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(idToken, tokenType, expiresIn);
                    }
                });
            }

            @Override
            public void onFailure(final Throwable error) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(error);
                    }
                });
            }
        });
    }
}
