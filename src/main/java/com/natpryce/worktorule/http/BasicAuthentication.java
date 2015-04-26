package com.natpryce.worktorule.http;

import com.google.common.base.Charsets;

import javax.xml.bind.DatatypeConverter;
import java.net.HttpURLConnection;

public class BasicAuthentication implements HttpConnectionSetting {
    public final String headerValue;

    public BasicAuthentication(String username, String password) {
        this.headerValue = "Basic " + base64(username + ":" + password);
    }

    public static BasicAuthentication fromEnvironmentVariables(String usernameVarname, String passwordVarname) {
        return new BasicAuthentication(getenv(usernameVarname), getenv(passwordVarname));
    }

    private static String getenv(String name) {
        final String value = System.getenv(name);
        if (value == null) {
            throw new IllegalArgumentException("environment variable " + name + " is not set");
        }
        return value;
    }

    @Override
    public void applyTo(HttpURLConnection cx) {
        cx.setRequestProperty("Authorization", headerValue);
    }

    private static String base64(String s) {
        return DatatypeConverter.printBase64Binary(s.getBytes(Charsets.UTF_8));
    }
}
