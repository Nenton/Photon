package com.nenton.photon.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

/**
 * Created by serge on 04.06.2017.
 */

public class PhotoTransform implements Transformation {
    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap bitmap = Bitmap.createBitmap(source.getWidth(),
                source.getHeight(),
                source.getConfig());

        Shader[] shaders = new Shader[2];
        shaders[0] = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        shaders[1] = new LinearGradient(0, source.getHeight()*3/4, 0, source.getHeight(),
                Color.TRANSPARENT, Color.BLACK,
                Shader.TileMode.CLAMP);

        ComposeShader composeShader = new ComposeShader(shaders[0], shaders[1], PorterDuff.Mode.SRC_ATOP);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(composeShader);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawPaint(paint);

        source.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "Gradient";
    }
}
