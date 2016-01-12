/*
 * AuthenticationRequest.java
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

import com.auth0.api.callback.AuthenticationCallback;
import com.auth0.core.Token;
import com.auth0.core.UserProfile;

import java.util.Map;

/**
 * Represent a Authentication request that consist of a log in and a fetch profile requests
 */
public class AuthenticationRequest {

    Handler handler;
    com.auth0.java.api.authentication.AuthenticationRequest request;

    protected AuthenticationRequest(Handler handler, com.auth0.java.api.authentication.AuthenticationRequest request) {
        this.handler = handler;
        this.request = request;
    }

    /**
     * Adds additional parameters for the login request
     * @param parameters as a non-null dictionary
     * @return itself
     */
    public AuthenticationRequest addParameters(Map<String, Object> parameters) {
        request.addParameters(parameters);
        return this;
    }

    /**
     * Set the scope used to authenticate the user
     * @param scope value
     * @return itself
     */
    public AuthenticationRequest setScope(String scope) {
        request.setScope(scope);
        return this;
    }

    /**
     * Set the connection used to authenticate
     * @param connection name
     * @return itself
     */
    public AuthenticationRequest setConnection(String connection) {
        request.setConnection(connection);
        return this;
    }

    /**
     * Starts the log in request and then fetches the user's profile
     * @param callback called on either success or failure
     */
    public void start(final AuthenticationCallback callback) {
        request.start(new com.auth0.java.api.callback.AuthenticationCallback() {
            @Override
            public void onSuccess(final com.auth0.java.core.UserProfile profile, final com.auth0.java.core.Token token) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(new UserProfile(profile), new Token(token));
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
