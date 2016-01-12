package com.auth0.core;

import com.auth0.java.core.Strategy;

import java.util.List;

/**
 * Class with your Auth0's application information and the list of enabled connections (DB, Social, Enterprise, Passwordless).
 */

public class Application extends com.auth0.java.core.Application {

    public Application(String id, String tenant, String authorizeURL, String callbackURL, String subscription, boolean hasAllowedOrigins, List<Strategy> strategies) {
        super(id, tenant, authorizeURL, callbackURL, subscription, hasAllowedOrigins, strategies);
    }

    public Application(com.auth0.java.core.Application application) {
        super(application);
    }
}
