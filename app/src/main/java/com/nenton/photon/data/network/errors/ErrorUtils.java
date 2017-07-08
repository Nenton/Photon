package com.nenton.photon.data.network.errors;

import retrofit2.Response;

/**
 * Created by serge on 03.01.2017.
 */

public class ErrorUtils {
    public static ApiError parseError(Response<?> response) {
        return new ApiError(response.code());
    }
}
