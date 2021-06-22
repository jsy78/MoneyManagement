package com.knu.moneymanagement.calendarDecorator;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.prolificinteractive.materialcalendarview.spans.DotSpan;

public class SingleDotSpan extends DotSpan {

    private final float radius;
    private final int color;


    SingleDotSpan(float radius, int color) {
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void drawBackground(
            Canvas canvas, Paint paint,
            int left, int right, int top, int baseline, int bottom,
            CharSequence charSequence,
            int start, int end, int lineNum
    ) {
        int oldColor = paint.getColor();
        if (color != 0) {
            paint.setColor(color);
        }
        canvas.drawCircle((left + right) / 2f, bottom + radius, radius, paint);
        paint.setColor(oldColor);
    }
}
