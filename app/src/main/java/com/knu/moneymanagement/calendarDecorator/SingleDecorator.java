package com.knu.moneymanagement.calendarDecorator;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class SingleDecorator implements DayViewDecorator {

    private final int color;
    private final HashSet<CalendarDay> dates;

    public SingleDecorator(Collection<CalendarDay> dates, int color) {
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new SingleDotSpan(5, color)); // 날짜 밑에 점
    }
}
