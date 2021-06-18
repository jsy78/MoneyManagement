package com.knu.moneymanagement;

import android.graphics.Bitmap;
import android.icu.util.Calendar;
import java.text.DecimalFormat;

public class RecyclerViewItem {

    private boolean check;
    private int id;
    private String category;
    private Bitmap iconBitmap;
    private int year;
    private int month;
    private int day;
    private String dateStr;
    private String detailStr;
    private int money;
    private String moneyStr;

    Bitmap getIcon() {
        return iconBitmap;
    }

    public void setIcon(Bitmap iconBitmap) {
        this.iconBitmap = iconBitmap;
    }

    String getDate() {
        return dateStr;
    }

    public void setDate(int year, int month, int day) {
        if (month < 10 && day < 10)
            this.dateStr = "0" + month + "." + "0" + day;
        else if (month < 10)
            this.dateStr = "0" + month + "." + day;
        else if (day < 10)
            this.dateStr = month + "." + "0" + day;
        else
            this.dateStr = month + "." + day;

        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, day);
        int temp = cal.get(Calendar.DAY_OF_WEEK);
        if (temp == Calendar.SUNDAY) {
            this.dateStr += " (일)";
        }
        else if (temp == Calendar.MONDAY) {
            this.dateStr += " (월)";
        }
        else if (temp == Calendar.TUESDAY) {
            this.dateStr += " (화)";
        }
        else if (temp == Calendar.WEDNESDAY) {
            this.dateStr += " (수)";
        }
        else if (temp == Calendar.THURSDAY) {
            this.dateStr += " (목)";
        }
        else if (temp == Calendar.FRIDAY) {
            this.dateStr += " (금)";
        }
        else if (temp == Calendar.SATURDAY) {
            this.dateStr += " (토)";
        }
    }

    public String getDetail() {
        return detailStr;
    }

    public void setDetail(String detailStr) {
        this.detailStr = detailStr;
    }

    String getMoneyStr() {
        return moneyStr;
    }

    public void setMoneyStr(int money) {
        DecimalFormat myFormatter = new DecimalFormat("###,###");
        String formattedStringPrice = myFormatter.format(money);

        this.moneyStr = formattedStringPrice+"원";
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean isChecked() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

}
