package com.knu.moneymanagement.database;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StaticVariable implements Constant {

    private static final Date currentTime = Calendar.getInstance().getTime();
    @SuppressLint("ConstantLocale")
    private static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
    @SuppressLint("ConstantLocale")
    private static final SimpleDateFormat monthFormat = new SimpleDateFormat("M", Locale.getDefault());
    @SuppressLint("ConstantLocale")
    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("d", Locale.getDefault());

    public static int year = Integer.parseInt(yearFormat.format(currentTime));
    public static int month = Integer.parseInt(monthFormat.format(currentTime));
    public static int day = Integer.parseInt(dayFormat.format(currentTime));
    public static int sort = ASC_DAY;


}
