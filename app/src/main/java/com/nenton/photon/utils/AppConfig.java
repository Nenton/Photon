package com.nenton.photon.utils;

/**
 * Created by serge on 03.06.2017.
 */

public interface AppConfig {
    String BASE_URL = "http://207.154.248.163:8000/";

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
}
