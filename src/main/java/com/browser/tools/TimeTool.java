package com.browser.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 *
 * @author tangyifei
 */
public class TimeTool {

    private TimeTool() {

    }

    public static String getIntervalTimeStr(Date start, Date end) {
        // Determine whether there is a year, 365 days a year
        long from = start.getTime();
        long to = end.getTime();
        long timeInterval = to - from;
        // 365 days here
        long years = timeInterval / 31536000000L;
        StringBuilder sb = new StringBuilder(1 << 3);
        sb.append("about ");
        if (years >= 1) {
            sb.append(years);
            sb.append(" years ");
            sb.append("ago");
            return sb.toString();
        }
        // Here is 30 days
        long months = timeInterval / 2592000000L;
        if (months >= 1) {
            sb.append(months);
            sb.append(" months ");
            sb.append("ago");
            return sb.toString();
        }
        int days = (int) ((to - from) / (1000 * 60 * 60 * 24));
        if (days >= 1) {
            sb.append(days);
            sb.append(" days ");
            sb.append("ago");
            return sb.toString();
        }
        int hours = (int) ((to - from) / (1000 * 60 * 60));
        if (hours >= 1) {
            sb.append(hours);
            sb.append(" hours ");
            sb.append("ago");
            return sb.toString();
        }
        int minutes = (int) ((to - from) / (1000 * 60));
        if (minutes < 1) {
            minutes = 1;
        }
        sb.append(minutes);
        sb.append(" minutes ");
        sb.append("ago");
        return sb.toString();

    }

    //    public static void main(String[] args) throws ParseException {
//        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date from = simpleFormat.parse("2021-06-06 11:00:00");
//        Date to = simpleFormat.parse("2021-06-07 12:50:36");
//        System.out.println(getIntervalTimeStr(from, to));
//    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z",
//            Locale.ENGLISH).format(new Date());
//        System.out.println(timestamp);
//    }

}
