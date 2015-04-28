package com.natpryce.worktorule.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.natpryce.worktorule.http.HttpConnectionSetting;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class JsonHttp {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String acceptedContentType;
    private final HttpConnectionSetting[] connectionSettings;

    public JsonHttp(String acceptedContentType, HttpConnectionSetting... connectionSettings) {
        this.acceptedContentType = acceptedContentType;
        this.connectionSettings = connectionSettings;
    }

    public JsonNode getJson(URL url) throws IOException {
        HttpURLConnection cx = (HttpURLConnection) url.openConnection();
        cx.setRequestProperty("Accept", acceptedContentType);
        cx.setRequestProperty("User-Agent", getClass().getName());
        for (HttpConnectionSetting connectionSetting : connectionSettings) {
            connectionSetting.applyTo(cx);
        }

        int responseCode = cx.getResponseCode();
        if (responseCode >= 300) {
            throw new IOException("request to " + url + " failed with status " + responseCode);
        }

        return parseJson(cx);
    }

    private JsonNode parseJson(HttpURLConnection cx) throws IOException {
        Reader r = new InputStreamReader(new BufferedInputStream(cx.getInputStream()), parseCharset(cx));
        try {
            return objectMapper.readTree(r);
        } finally {
            r.close();
        }
    }

    private String parseCharset(MimeType contentType) {
        return Optional.fromNullable(contentType.getParameter("charset")).or("utf-8");
    }

    private String parseCharset(HttpURLConnection cx) throws IOException {
        return parseCharset(parseContentType(cx));
    }

    private MimeType parseContentType(HttpURLConnection cx) throws IOException {
        String contentType = cx.getContentType();
        try {
            return new MimeType(contentType);
        } catch (MimeTypeParseException e) {
            throw new IOException("failed to parse content type: " + contentType, e);
        }
    }
}
