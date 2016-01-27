/*
 * Lock.java
 *
 * Copyright (c) 2016 Auth0 (http://auth0.com)
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

package com.auth0.android.lock;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.auth0.Auth0;
import com.auth0.android.lock.utils.LockException;
import com.auth0.authentication.result.Authentication;
import com.auth0.authentication.result.Token;
import com.auth0.authentication.result.UserProfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lock {

    private AuthenticationCallback callback;
    private final LockOptions options;

    public static final String OPTIONS_EXTRA = "com.auth0.android.lock.key.Options";

    static final String AUTHENTICATION_ACTION = "com.auth0.android.lock.action.Authentication";
    static final String CANCELED_ACTION = "com.auth0.android.lock.action.Canceled";

    static final String ID_TOKEN_EXTRA = "com.auth0.android.lock.extra.IdToken";
    static final String ACCESS_TOKEN_EXTRA = "com.auth0.android.lock.extra.AccessToken";
    static final String TOKEN_TYPE_EXTRA = "com.auth0.android.lock.extra.TokenType";
    static final String REFRESH_TOKEN_EXTRA = "com.auth0.android.lock.extra.RefreshToken";

    /**
     * Listens to LockActivity broadcasts and fires the correct action on the AuthenticationCallback.
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent data) {
            // Get extra data included in the Intent
            String action = data.getAction();
            if (action.equals(Lock.AUTHENTICATION_ACTION)) {
                processEvent(data);
            } else if (action.equals(Lock.CANCELED_ACTION)) {
                callback.onCanceled();
            }
        }
    };

    protected Lock(LockOptions options, AuthenticationCallback callback) {
        this.options = options;
        this.callback = callback;
    }

    public static Builder newBuilder() {
        return new Lock.Builder();
    }

    /**
     * Builds a new intent to launch LockActivity with the given options
     *
     * @param activity a valid Activity context
     * @return the intent to which the user has to call startActivity or startActivityForResult
     */
    public Intent newIntent(Activity activity) {
        Intent lockIntent = new Intent(activity, LockActivity.class);
        lockIntent.putExtra(OPTIONS_EXTRA, options);
        return lockIntent;
    }

    public void onCreate(Activity activity) {
        // nikolaseu: register broadcast listener only when callback is set?
        //            i.e. when NOT using startForResult
        //  L= lets leave this as an improvement

        //if (callback != null) //can the callback be optional?
        IntentFilter filter = new IntentFilter();
        filter.addAction(Lock.AUTHENTICATION_ACTION);
        filter.addAction(Lock.CANCELED_ACTION);
        LocalBroadcastManager.getInstance(activity).registerReceiver(this.receiver, filter);
    }

    public void onDestroy(Activity activity) {
        // unregister listener (if something was registered)
        if (this.receiver != null) {
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(this.receiver);
            this.receiver = null;
        }
    }

    /*
    Evaluate changing the name of this method: parseActivityResult? processResult?
    */
    public void onActivityResult(Activity activity, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            processEvent(data);
            return;
        }

        //user pressed back.
        callback.onCanceled();
    }

    /**
     * Extracts the Authentication data from the intent data.
     *
     * @param eventData the intent received at the end of the login process.
     */
    private void processEvent(Intent eventData) {
        String idToken = eventData.getStringExtra(Lock.ID_TOKEN_EXTRA);
        String accessToken = eventData.getStringExtra(Lock.ACCESS_TOKEN_EXTRA);
        String tokenType = eventData.getStringExtra(Lock.TOKEN_TYPE_EXTRA);
        String refreshToken = eventData.getStringExtra(Lock.REFRESH_TOKEN_EXTRA);
        Token token = new Token(idToken, accessToken, tokenType, refreshToken);

        //TODO: Fetch UserProfile
        HashMap<String, Object> profileData = new HashMap<String, Object>();
        profileData.put("user_id", "1");
        UserProfile profile = new UserProfile(profileData); //TODO. Ask api for profile?
        Authentication authentication = new Authentication(profile, token);

        if (idToken != null && accessToken != null) {
            callback.onAuthentication(authentication);
        } else {
            LockException up = new LockException(R.string.com_auth0_social_error_authentication);
            callback.onError(up);
            //throw up. haha
        }
    }

    public static class Builder {
        private LockOptions options;
        private AuthenticationCallback callback;

        public Builder() {
            options = new LockOptions();
        }

        public Lock build() {
            return new Lock(options, callback);
        }

        public Builder withAccount(Auth0 account) {
            options.account = account;
            return this;
        }

        public Builder withCallback(AuthenticationCallback callback) {
            this.callback = callback;
            return this;
        }

        public Builder useBrowser(boolean useBrowser) {
            options.useBrowser = useBrowser;
            return this;
        }

        public Builder closable(boolean closable) {
            options.closable = closable;
            return this;
        }

        public Builder fullscreen(boolean fullscreen) {
            options.fullscreen = fullscreen;
            return this;
        }

        public Builder withAuthenticationParameters(Map<String, Object> authenticationParameters) {
            if (authenticationParameters instanceof HashMap) {
                options.authenticationParameters = (HashMap<String, Object>) authenticationParameters;
            } else {
                options.authenticationParameters = new HashMap<String, Object>(authenticationParameters);
            }

            return this;
        }

        public Builder onlyUseConnections(List<String> connections) {
            options.connections = connections;
            return this;
        }

        public Builder doNotSendSDKInfo() {
            options.sendSDKInfo = false;
            return this;
        }

        public Builder useEmail() {
            options.useEmail = true;
            return this;
        }

        public Builder disableSignUp() {
            options.signUpEnabled = false;
            return this;
        }

        public Builder disableChangePassword() {
            options.changePasswordEnabled = false;
            return this;
        }
    }
}
