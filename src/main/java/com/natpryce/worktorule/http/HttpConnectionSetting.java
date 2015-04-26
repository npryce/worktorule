package com.natpryce.worktorule.http;

import java.net.HttpURLConnection;

public interface HttpConnectionSetting {
    void applyTo(HttpURLConnection cx);

    HttpConnectionSetting NONE = new HttpConnectionSetting() {
        @Override
        public void applyTo(HttpURLConnection cx) {
        }
    };
}
