package com.natpryce.worktorule.http;

import javax.xml.bind.DatatypeConverter;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

public class BasicAuthentication implements HttpConnectionSetting {
    public final String headerValue;

    public BasicAuthentication(String username, String password) {
        this.headerValue = "Basic " + base64(username + ":" + password);
    }

    @Override
    public void applyTo(HttpURLConnection cx) {
        cx.setRequestProperty("Authorization", headerValue);
    }

    private static String base64(String s) {
        return DatatypeConverter.printBase64Binary(s.getBytes(Charset.forName("UTF-8")));
    }
}
