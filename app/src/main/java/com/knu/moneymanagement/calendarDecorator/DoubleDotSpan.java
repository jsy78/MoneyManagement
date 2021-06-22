package com.knu.moneymanagement.calendarDecorator;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.prolificinteractive.materialcalendarview.spans.DotSpan;

public class DoubleDotSpan extends DotSpan {

    private final float radius;
    private final int color1;
    private final int color2;

    DoubleDotSpan(float radius, int color1, int color2) {
        this.radius = radius;
        this.color1 = color1;
        this.color2 = color2;
    }

    @Override
    public void drawBackground(
            Canvas canvas, Paint paint,
            int left, int right, int top, int baseline, int bottom,
            CharSequence charSequence,
            int start, int end, int lineNum
    ) {
        int oldColor;

        oldColor = paint.getColor();
        if (color1 != 0) {
            paint.setColor(color1);
        }
        canvas.drawCircle((left + right) / 2f - (2*radius), bottom + radius, radius, paint);
        paint.setColor(oldColor);

        oldColor = paint.getColor();
        if (color2 != 0) {
            paint.setColor(color2);
        }
        canvas.drawCircle((left + right) / 2f + (2*radius), bottom + radius, radius, paint);
        paint.setColor(oldColor);
    }
}
