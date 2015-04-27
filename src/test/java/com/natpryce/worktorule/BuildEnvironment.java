package com.natpryce.worktorule;

import com.natpryce.worktorule.http.BasicAuthentication;
import org.junit.AssumptionViolatedException;

public class BuildEnvironment {
    public static BasicAuthentication authFromEnvvars(String usernameVarname, String passwordVarname) {
        return new BasicAuthentication(getenv(usernameVarname), getenv(passwordVarname));
    }

    public static String getenv(String name) {
        final String value = System.getenv(name);
        if (value == null) {
            throw new AssumptionViolatedException("environment variable " + name + " is not set");
        }
        return value;
    }
}
