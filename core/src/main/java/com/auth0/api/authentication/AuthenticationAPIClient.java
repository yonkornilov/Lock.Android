/*
 * AuthenticationClient.java
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
import android.os.Looper;

import com.auth0.api.ParameterizableRequest;
import com.auth0.api.Request;
import com.auth0.api.internal.ApplicationInfoRequest;
import com.auth0.api.internal.DatabaseUserRequest;
import com.auth0.api.internal.ParameterizableRequestImpl;
import com.auth0.api.internal.RequestImpl;
import com.auth0.api.internal.TokenRequest;
import com.auth0.api.internal.UserProfileRequest;
import com.auth0.core.Application;
import com.auth0.core.Auth0;
import com.auth0.core.DatabaseUser;
import com.auth0.core.Token;
import com.auth0.core.UserProfile;

import java.util.Map;

/**
 * API client for Auth0 Authentication API.
 * @see <a href="https://auth0.com/docs/auth-api">Auth API docs</a>
 */
public class AuthenticationAPIClient {

    private final Handler handler;
    private final com.auth0.java.api.authentication.AuthenticationAPIClient apiClient;

    public AuthenticationAPIClient(com.auth0.java.api.authentication.AuthenticationAPIClient apiClient) {
        this(apiClient, new Handler(Looper.getMainLooper()));
    }

    public AuthenticationAPIClient(com.auth0.java.api.authentication.AuthenticationAPIClient apiClient, Handler handler) {
        this.apiClient = apiClient;
        this.handler = handler;
    }

    /**
     * Creates a new API client instance providing Auth0 account info.
     * @param auth0 account information
     */
    public AuthenticationAPIClient(Auth0 auth0) {
        this(new com.auth0.java.api.authentication.AuthenticationAPIClient(auth0), new Handler(Looper.getMainLooper()));
    }

    /**
     * Creates a new API client instance providing Auth0 account info and a handler where all callbacks will be called
     * @param auth0 account information
     * @param handler where callback will be called with either the response or error from the server
     */
    public AuthenticationAPIClient(Auth0 auth0, Handler handler) {
        this(new com.auth0.java.api.authentication.AuthenticationAPIClient(auth0), handler);
    }

    /**
     * Creates a new API client instance providing Auth API and Configuration Urls different than the default. (Useful for on premise deploys).
     * @param clientID Your application clientID.
     * @param baseURL Auth0's auth API endpoint
     * @param configurationURL Auth0's enpoint where App info can be retrieved.
     */
    @SuppressWarnings("unused")
    public AuthenticationAPIClient(String clientID, String baseURL, String configurationURL) {
        this(new Auth0(clientID, baseURL, configurationURL));
    }

    public String getClientId() {
        return apiClient.getClientId();
    }

    public String getBaseURL() {
        return apiClient.getBaseURL();
    }

    /**
     * Set the default DB connection name used. By default is 'Username-Password-Authentication'
     * @param defaultDbConnection name to use on every login with DB connection
     */
    public void setDefaultDbConnection(String defaultDbConnection) {
        apiClient.setDefaultDbConnection(defaultDbConnection);
    }

    /**
     * Fetch application information from Auth0
     * @return a Auth0 request to start
     */
    public Request<Application> fetchApplicationInfo() {
        return new ApplicationInfoRequest(handler, apiClient.fetchApplicationInfo());
    }

    /**
     * Log in a user using resource owner endpoint (<a href="https://auth0.com/docs/auth-api#!#post--oauth-ro">'/oauth/ro'</a>)
     * @return a request to configure and start
     */
    public ParameterizableRequest<Token> loginWithResourceOwner() {
        return new TokenRequest(handler, apiClient.loginWithResourceOwner());
    }

    /**
     * Log in a user with email/username and password using a DB connection and fetch it's profile from Auth0
     * @param usernameOrEmail of the user depending of the type of DB connection
     * @param password of the user
     * @return a request to configure and start that will yield {@link Token} and {@link UserProfile}
     */
    public AuthenticationRequest login(String usernameOrEmail, String password) {
        return new AuthenticationRequest(handler, apiClient.login(usernameOrEmail, password));
    }

    /**
     * Log in a user with a OAuth 'access_token' of a Identity Provider like Facebook or Twitter using <a href="https://auth0.com/docs/auth-api#!#post--oauth-access_token">'\oauth\access_token' endpoint</a>
     * @param token obtained from the IdP
     * @param connection that will be used to authenticate the user, e.g. 'facebook'
     * @return a request to configure and start that will yield {@link Token} and {@link UserProfile}
     */
    public AuthenticationRequest loginWithOAuthAccessToken(String token, String connection) {
        return new AuthenticationRequest(handler, apiClient.loginWithOAuthAccessToken(token, connection));
    }

    /**
     * Log in a user using a phone number and a verification code received via SMS (Part of passwordless login flow)
     * @param phoneNumber where the user received the verification code
     * @param verificationCode sent by Auth0 via SMS
     * @return a request to configure and start that will yield {@link Token} and {@link UserProfile}
     */
    public AuthenticationRequest loginWithPhoneNumber(String phoneNumber, String verificationCode) {
        return new AuthenticationRequest(handler, apiClient.loginWithPhoneNumber(phoneNumber, verificationCode));
    }

    /**
     * Log in a user using an email and a verification code received via Email (Part of passwordless login flow)
     * @param email where the user received the verification code
     * @param verificationCode sent by Auth0 via Email
     * @return a request to configure and start that will yield {@link Token} and {@link UserProfile}
     */
    public AuthenticationRequest loginWithEmail(String email, String verificationCode) {
        return new AuthenticationRequest(handler, apiClient.loginWithEmail(email, verificationCode));
    }

    /**
     * Fetch the token information from Auth0
     * @param idToken used to fetch it's information
     * @return a request to start
     */
    public Request<UserProfile> tokenInfo(String idToken) {
        return new UserProfileRequest(handler, apiClient.tokenInfo(idToken));
    }

    /**
     * Creates a user in a DB connection using <a href="https://auth0.com/docs/auth-api#!#post--dbconnections-signup">'/dbconnections/signup' endpoint</a>
     * @param email of the user and must be non null
     * @param password of the user and must be non null
     * @param username of the user and must be non null
     * @return a request to start
     */
    public ParameterizableRequest<DatabaseUser> createUser(String email, String password, String username) {
        return new DatabaseUserRequest(handler, apiClient.createUser(email, password, username));
    }

    /**
     * Creates a user in a DB connection using <a href="https://auth0.com/docs/auth-api#!#post--dbconnections-signup">'/dbconnections/signup' endpoint</a>
     * @param email of the user and must be non null
     * @param password of the user and must be non null
     * @return a request to start
     */
    public ParameterizableRequest<DatabaseUser> createUser(String email, String password) {
        return createUser(email, password, null);
    }

    /**
     * Creates a user in a DB connection using <a href="https://auth0.com/docs/auth-api#!#post--dbconnections-signup">'/dbconnections/signup' endpoint</a>
     * and then logs in and fetches it's user profile
     * @param email of the user and must be non null
     * @param password of the user and must be non null
     * @param username of the user and must be non null
     * @return a request to configure and start that will yield {@link Token} and {@link UserProfile}
     */
    public SignUpRequest signUp(String email, String password, String username) {
        return new SignUpRequest(handler, apiClient.signUp(email, password, username));
    }

    /**
     * Creates a user in a DB connection using <a href="https://auth0.com/docs/auth-api#!#post--dbconnections-signup">'/dbconnections/signup' endpoint</a>
     * and then logs in and fetches it's user profile
     * @param email of the user and must be non null
     * @param password of the user and must be non null
     * @return a request to configure and start that will yield {@link Token} and {@link UserProfile}
     */
    public SignUpRequest signUp(String email, String password) {
        return new SignUpRequest(handler, apiClient.signUp(email, password));
    }

    /**
     * Perform a change password request using <a href="https://auth0.com/docs/auth-api#!#post--dbconnections-change_password">'/dbconnections/change_password'</a>
     * @param email of the user that changes the password. It's also where the confirmation email will be sent
     * @param newPassword to use
     * @return a request to configure and start
     */
    public ParameterizableRequest<Void> changePassword(String email, String newPassword) {
        return new ParameterizableRequestImpl<>(handler, apiClient.changePassword(email, newPassword));
    }

    /**
     * Performs a <a href="https://auth0.com/docs/auth-api#!#post--delegation">delegation</a> request
     * @return a request to configure and start
     */
    public ParameterizableRequest<Map<String, Object>> delegation() {
        return new ParameterizableRequestImpl<>(handler, apiClient.delegation());
    }

    /**
     * Performs a <a href="https://auth0.com/docs/auth-api#!#post--delegation">delegation</a> request that will yield a new Auth0 'id_token'
     * @param idToken issued by Auth0 for the user. The token must not be expired.
     * @return a request to configure and start
     */
    public DelegationRequest delegationWithIdToken(String idToken) {
        return new DelegationRequest(handler, apiClient.delegationWithIdToken(idToken));
    }

    /**
     * Performs a <a href="https://auth0.com/docs/auth-api#!#post--delegation">delegation</a> request that will yield a new Auth0 'id_token'.
     * Check our <a href="https://auth0.com/docs/refresh-token">refresh token</a> docs for more information
     * @param refreshToken issued by Auth0 for the user when using the 'offline_access' scope when logging in.
     * @return a request to configure and start
     */
    public DelegationRequest delegationWithRefreshToken(String refreshToken) {
        return new DelegationRequest(handler, apiClient.delegationWithRefreshToken(refreshToken));
    }

    /**
     * Unlink a user identity calling <a href="https://auth0.com/docs/auth-api#!#post--unlink">'/unlink'</a> endpoint
     * @param userId of the identity to unlink
     * @param accessToken of the main identity obtained after login
     * @return a request to start
     */
    public Request<Void> unlink(String userId, String accessToken) {
        return new RequestImpl<>(handler, apiClient.unlink(userId, accessToken));
    }

    /**
     * Start a passwordless flow with either <a href="https://auth0.com/docs/auth-api#!#post--with_email">Email</a> or <a href="https://auth0.com/docs/auth-api#!#post--with_sms">SMS</a>
     * @return a request to configure and stat
     */
    public ParameterizableRequest<Void> passwordless() {
        return new ParameterizableRequestImpl<>(handler, apiClient.passwordless());
    }

    /**
     * Start a passwordless flow with either <a href="https://auth0.com/docs/auth-api#!#post--with_email">Email</a>
     * @param email that will receive a verification code to use for login
     * @param useMagicLink whether the email should contain the magic link or the code
     * @return a request to configure and start
     */
    public ParameterizableRequest<Void> passwordlessWithEmail(String email, boolean useMagicLink) {
        return new ParameterizableRequestImpl<>(handler, apiClient.passwordlessWithEmail(email, useMagicLink));
    }

    /**
     * Start a passwordless flow with <a href="https://auth0.com/docs/auth-api#!#post--with_sms">SMS</a>
     * @param phoneNumber where an SMS with a verification code will be sent
     * @param useMagicLink whether the SMS should contain the magic link or the code
     * @return a request to configure and stat
     */
    public ParameterizableRequest<Void> passwordlessWithSMS(String phoneNumber, boolean useMagicLink) {
        return new ParameterizableRequestImpl<>(handler, apiClient.passwordlessWithSMS(phoneNumber, useMagicLink));
    }
}
