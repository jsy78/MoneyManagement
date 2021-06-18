package com.knu.moneymanagement.calendarDecorator;

import android.graphics.Color;
import android.icu.util.Calendar;
import android.text.style.ForegroundColorSpan;

import com.knu.moneymanagement.database.StaticVariable;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class SaturdayDecorator implements DayViewDecorator {

    private final Calendar cal = Calendar.getInstance();

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        cal.set(day.getYear(), day.getMonth()-1, day.getDay());
        int year = day.getYear();
        int month = day.getMonth();
        int week = cal.get(Calendar.DAY_OF_WEEK);
        return year == StaticVariable.year && month == StaticVariable.month && week == Calendar.SATURDAY;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.BLUE));
    }
}
