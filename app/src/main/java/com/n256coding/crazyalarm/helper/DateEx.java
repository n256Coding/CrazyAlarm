package com.n256coding.crazyalarm.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateEx extends Date {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DateFormat timeFormat = new SimpleDateFormat("hh:mm a");

    public DateEx(){
        super();
    }

    public static String getTimeString(Date date){
        return timeFormat.format(date);
    }

    public static String getDateTimeString(Date date){
        return dateTimeFormat.format(date);
    }

    public static Date getDateOfDateTime(String date) throws ParseException {
        return dateTimeFormat.parse(date);
    }

    /**
     *
     * @return hour of day
     */
    public static int getCurrentHourOfDay(){
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    /**
     *
     * @return current minute
     */
    public static int getCurrentMinute(){
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    public static Date getAlarmDateOf(int hourOfDay, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        if(calendar.getTimeInMillis() < new Date().getTime()){
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return calendar.getTime();
    }

}
