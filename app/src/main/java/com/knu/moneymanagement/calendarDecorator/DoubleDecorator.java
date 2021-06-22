package com.knu.moneymanagement.calendarDecorator;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class DoubleDecorator implements DayViewDecorator {

    private final int color1;
    private final int color2;
    private final HashSet<CalendarDay> dates;

    public DoubleDecorator(Collection<CalendarDay> dates, int color1, int color2) {
        this.color1 = color1;
        this.color2 = color2;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DoubleDotSpan(5, color1, color2)); // 날짜 밑에 점
    }
}
