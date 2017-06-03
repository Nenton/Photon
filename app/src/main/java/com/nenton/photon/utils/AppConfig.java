package com.nenton.photon.utils;

public interface AppConfig {
    String BASE_URL = "https://skba1.mgbeta.ru/api/v1/";

    long MAX_CONNECTION_TIMEOUT = 5 * 1000;
    long MAX_READ_TIMEOUT = 5 * 1000;
    long MAX_WRITE_TIMEOUT = 5 * 1000;
    int MIN_CONSUMER_COUNT = 1;
    int MAX_CONSUMER_COUNT = 3;
    int LOAD_FACTOR = 3;
    int KEEP_ALIVE = 120;
    int INITIAL_BACK_OFF_IN_MS = 1000;
    int UPDATE_DATA_INTERVAL = 30;
    int RETRY_REQUEST_COUNT = 5;
    int RETRY_REQUEST_BASE_DELAY = 500;
    String BASE_VK_URL = "https://api.vk.com/method/";
    String FB_BASE_URL = "https://graph.facebook.com/v2.8/";
}
