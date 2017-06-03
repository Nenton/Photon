package com.nenton.photon.data.storage.dto;

import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by serge on 18.12.2016.
 */

public class ActivityResultDto {
    private int requestCode;
    private int resultCode;
    @Nullable
    private Intent data;

    public ActivityResultDto(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    @Nullable
    public Intent getIntent() {
        return data;
    }

    public void setData(@Nullable Intent data) {
        this.data = data;
    }
}
