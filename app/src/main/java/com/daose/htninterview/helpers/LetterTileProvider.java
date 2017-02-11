package com.daose.htninterview.helpers;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.DimenRes;
import android.text.TextPaint;

import com.daose.htninterview.R;

// With reference from: http://stackoverflow.com/questions/23122088/colored-boxed-with-letters-a-la-gmail
public class LetterTileProvider {
    private static final int NUM_OF_COLORS = 8;
    private final TextPaint paint = new TextPaint();
    private final Rect bounds = new Rect();
    private final Canvas canvas = new Canvas();
    private char[] firstChar = new char[1];

    private final TypedArray colors;
    private final int fontSize;

    public LetterTileProvider(Context context) {
        final Resources res = context.getResources();

        paint.setTypeface(Typeface.DEFAULT);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);

        colors = res.obtainTypedArray(R.array.initial_colors);
        fontSize = res.getDimensionPixelSize(R.dimen.initial_font_size);
    }

    public static BitmapDrawable getTile(Context ctx, String name, @DimenRes int size) {
        int diameter = ctx.getResources().getDimensionPixelSize(size);
        LetterTileProvider provider = new LetterTileProvider(ctx);
        return new BitmapDrawable(ctx.getResources(), provider.getLetterTile(name, diameter));
    }

    private Bitmap getLetterTile(String name, int diameter) {
        final Bitmap bitmap = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        firstChar[0] = name.charAt(0);

        final Canvas c = canvas;
        c.setBitmap(bitmap);
        c.drawColor(getColor(name));

        paint.setTextSize(fontSize);
        paint.getTextBounds(firstChar, 0, 1, bounds);

        c.drawText(firstChar, 0, 1, diameter / 2, diameter / 2 + (bounds.bottom - bounds.top) / 2, paint);
        return bitmap;
    }

    private int getColor(String name){
        final int color = Math.abs(name.hashCode()) % NUM_OF_COLORS;

        try {
            return colors.getColor(color, Color.BLACK);
        } finally {
            colors.recycle();
        }
    }
}
