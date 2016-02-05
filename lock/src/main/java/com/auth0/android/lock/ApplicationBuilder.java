/*
 * ApplicationBuilder.java
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

import android.support.annotation.NonNull;
import android.util.Log;

import com.auth0.Auth0;
import com.auth0.android.lock.utils.Application;
import com.auth0.android.lock.utils.Connection;
import com.auth0.android.lock.utils.Strategies;
import com.auth0.android.lock.utils.Strategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: Test this class
public class ApplicationBuilder {
    private static final String TAG = ApplicationBuilder.class.getSimpleName();
    public static final String JSONP_HEADER = "Auth0.setClient(";
    public static final String JSONP_TRAIL = ");";

    private String id;
    private String authorizeUrl;
    private Map<String, List<Connection>> connectionsMap;

    private ApplicationBuilder(Auth0 account) {
        id = account.getClientId();
        authorizeUrl = account.getAuthorizeUrl();
        connectionsMap = new HashMap<>();
    }

    //We can avoid this constructor by moving this builder into an inner class on Lock class.
    public static ApplicationBuilder newBuilder(Auth0 account) {
        return new ApplicationBuilder(account);
    }

    public ApplicationBuilder withDatabaseConnection(DatabaseConnection connection) {
        Connection c = new Connection(connection.getValues());
        if (connectionsMap.containsKey(Strategies.Auth0.getName())) {
            connectionsMap.get(Strategies.Auth0.getName()).add(c);
        } else {
            List<Connection> list = new ArrayList<>();
            list.add(c);
            connectionsMap.put(Strategies.Auth0.getName(), list);
        }
        return this;
    }

    public ApplicationBuilder withSocialConnection(SocialConnection connection) {
        Connection c = new Connection(connection.getValues());
        if (connectionsMap.containsKey(connection.getStrategyName())) {
            connectionsMap.get(connection.getStrategyName()).add(c);
        } else {
            List<Connection> list = new ArrayList<>();
            list.add(c);
            connectionsMap.put(connection.getStrategyName(), list);
        }
        return this;
    }

    //TODO: Add enterprise connection

    public String build() {
        List<Strategy> strategies = checkStrategies();
        Application app = new Application(id, authorizeUrl, strategies);
        ObjectWriter ow = new ObjectMapper().writer();
        String json = null;
        try {
            json = JSONP_HEADER + ow.writeValueAsString(app) + JSONP_TRAIL;
            json = json.replace("\"authorizeURL\":", "\"authorize\":");
            json = json.replace("\"callbackURL\":", "\"callback\":");
        } catch (JsonProcessingException e) {
            Log.e(TAG, "Error processing the input data");
            e.printStackTrace();
        }
        return json;
    }

    @NonNull
    private List<Strategy> checkStrategies() {
        if (connectionsMap.isEmpty()) {
            throw new IllegalArgumentException("You must supply at least 1 Strategy");
        }

        List<Strategy> strategies = new ArrayList<>();
        for (String name : connectionsMap.keySet()) {
            strategies.add(new Strategy(name, connectionsMap.get(name)));
        }

        return strategies;
    }


    public static class DatabaseConnection {
        public static final String NAME = "name";
        public static final String SHOW_SIGNUP = "showSignup";
        public static final String SHOW_FORGOT = "showForgot";
        public static final String REQUIRES_USERNAME = "requires_username";
        private final Map<String, Object> values;

        public DatabaseConnection(String connectionName) {
            values = new HashMap<>();
            values.put(NAME, connectionName);
            values.put(SHOW_SIGNUP, true);
            values.put(SHOW_FORGOT, true);
            values.put(REQUIRES_USERNAME, false);
        }

        public DatabaseConnection(String connectionName, boolean signUpEnabled, boolean changePasswordEnabled) {
            values = new HashMap<>();
            values.put(NAME, connectionName);
            values.put(SHOW_SIGNUP, signUpEnabled);
            values.put(SHOW_FORGOT, changePasswordEnabled);
            values.put(REQUIRES_USERNAME, false);
        }

        public DatabaseConnection(String connectionName, boolean signUpEnabled, boolean changePasswordEnabled, boolean requiresUsername) {
            values = new HashMap<>();
            values.put(NAME, connectionName);
            values.put(SHOW_SIGNUP, signUpEnabled);
            values.put(SHOW_FORGOT, changePasswordEnabled);
            values.put(REQUIRES_USERNAME, requiresUsername);
        }

        public Map<String, Object> getValues() {
            return values;
        }
    }

    public static class SocialConnection {
        public static final String NAME = "name";
        private final String strategyName;
        private final Map<String, Object> values;

        public SocialConnection(String strategyName, String connectionName) {
            this.strategyName = strategyName;
            values = new HashMap<>();
            values.put(NAME, connectionName);
        }

        public Map<String, Object> getValues() {
            return values;
        }

        public String getStrategyName() {
            return strategyName;
        }
    }
}
