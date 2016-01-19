package com.auth0.api;

import android.os.Build;

import java.util.HashMap;
import java.util.Map;


/**
 * Builder class for Auth API parameters.
 */
public class ParameterBuilder extends com.auth0.authentication.api.ParameterBuilder {

    public ParameterBuilder() {
        super();
    }

    public ParameterBuilder(Map<String, Object> parameters) {
        super(parameters);
    }

    /**
     * Sets the 'scope' parameter. If the scope includes 'offline_access', it will set the 'device' parameter.
     * @param scope a scope value
     * @return itself
     */
    @Override
    public ParameterBuilder setScope(String scope) {
        super.setScope(scope);
        if (scope.contains("offline_access")) {
            setDevice(Build.MODEL);
        } else {
            setDevice(null);
        }
        return this;
    }

    /**
     * Creates a new instance of the builder with default values
     * @return a new builder
     */
    public static ParameterBuilder newBuilder() {
        return new ParameterBuilder();
    }

    /**
     * Creates a new instance of the builder without any default values
     * @return a new builder
     */
    public static ParameterBuilder newEmptyBuilder() {
        return new ParameterBuilder(new HashMap<String, Object>());
    }

    /**
     * Creates a new instance of the builder with parameters.
     * @param parameters default parameters
     * @return a new builder
     */
    public static ParameterBuilder newBuilder(Map<String, Object> parameters) {
        return new ParameterBuilder(parameters);
    }
}
