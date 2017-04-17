package com.byagowi.persiancalendar.view.activity;

import java.util.Date;
import java.util.Locale;

/**
 * Created by vMagnify on 4/2/16.
 */
public class ConvertPersianCal {

    private static   String[] persianNumbers = new String[]{ "۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹" };
    private static   String[] englishNumbers = new String[]{ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
    private class SolarCalendar {


        public String strWeekDay = "";
        public String strMonth = "";

        int date;
        int month;
        int year;

        public SolarCalendar()
        {
            Date MiladiDate = new Date();
            calcSolarCalendar(MiladiDate);
        }

        public SolarCalendar(Date MiladiDate)
        {
            calcSolarCalendar(MiladiDate);
        }

        private void calcSolarCalendar(Date MiladiDate) {

            int ld;

            int miladiYear = MiladiDate.getYear() + 1900;
            int miladiMonth = MiladiDate.getMonth() + 1;
            int miladiDate = MiladiDate.getDate();
            int WeekDay = MiladiDate.getDay();

            int[] buf1 = new int[12];
            int[] buf2 = new int[12];

            buf1[0] = 0;
            buf1[1] = 31;
            buf1[2] = 59;
            buf1[3] = 90;
            buf1[4] = 120;
            buf1[5] = 151;
            buf1[6] = 181;
            buf1[7] = 212;
            buf1[8] = 243;
            buf1[9] = 273;
            buf1[10] = 304;
            buf1[11] = 334;

            buf2[0] = 0;
            buf2[1] = 31;
            buf2[2] = 60;
            buf2[3] = 91;
            buf2[4] = 121;
            buf2[5] = 152;
            buf2[6] = 182;
            buf2[7] = 213;
            buf2[8] = 244;
            buf2[9] = 274;
            buf2[10] = 305;
            buf2[11] = 335;

            if ((miladiYear % 4) != 0) {
                date = buf1[miladiMonth - 1] + miladiDate;

                if (date > 79) {
                    date = date - 79;
                    if (date <= 186) {
                        switch (date % 31) {
                            case 0:
                                month = date / 31;
                                date = 31;
                                break;
                            default:
                                month = (date / 31) + 1;
                                date = (date % 31);
                                break;
                        }
                        year = miladiYear - 621;
                    } else {
                        date = date - 186;

                        switch (date % 30) {
                            case 0:
                                month = (date / 30) + 6;
                                date = 30;
                                break;
                            default:
                                month = (date / 30) + 7;
                                date = (date % 30);
                                break;
                        }
                        year = miladiYear - 621;
                    }
                } else {
                    if ((miladiYear > 1996) && (miladiYear % 4) == 1) {
                        ld = 11;
                    } else {
                        ld = 10;
                    }
                    date = date + ld;

                    switch (date % 30) {
                        case 0:
                            month = (date / 30) + 9;
                            date = 30;
                            break;
                        default:
                            month = (date / 30) + 10;
                            date = (date % 30);
                            break;
                    }
                    year = miladiYear - 622;
                }
            } else {
                date = buf2[miladiMonth - 1] + miladiDate;

                if (miladiYear >= 1996) {
                    ld = 79;
                } else {
                    ld = 80;
                }
                if (date > ld) {
                    date = date - ld;

                    if (date <= 186) {
                        switch (date % 31) {
                            case 0:
                                month = (date / 31);
                                date = 31;
                                break;
                            default:
                                month = (date / 31) + 1;
                                date = (date % 31);
                                break;
                        }
                        year = miladiYear - 621;
                    } else {
                        date = date - 186;

                        switch (date % 30) {
                            case 0:
                                month = (date / 30) + 6;
                                date = 30;
                                break;
                            default:
                                month = (date / 30) + 7;
                                date = (date % 30);
                                break;
                        }
                        year = miladiYear - 621;
                    }
                }

                else {
                    date = date + 10;

                    switch (date % 30) {
                        case 0:
                            month = (date / 30) + 9;
                            date = 30;
                            break;
                        default:
                            month = (date / 30) + 10;
                            date = (date % 30);
                            break;
                    }
                    year = miladiYear - 622;
                }

            }

            switch (month) {
                case 1:
                    strMonth = "فروردين";
                    break;
                case 2:
                    strMonth = "ارديبهشت";
                    break;
                case 3:
                    strMonth = "خرداد";
                    break;
                case 4:
                    strMonth = "تير";
                    break;
                case 5:
                    strMonth = "مرداد";
                    break;
                case 6:
                    strMonth = "شهريور";
                    break;
                case 7:
                    strMonth = "مهر";
                    break;
                case 8:
                    strMonth = "آبان";
                    break;
                case 9:
                    strMonth = "آذر";
                    break;
                case 10:
                    strMonth = "دي";
                    break;
                case 11:
                    strMonth = "بهمن";
                    break;
                case 12:
                    strMonth = "اسفند";
                    break;
            }

            switch (WeekDay) {

                case 0:
                    strWeekDay = "يکشنبه";
                    break;
                case 1:
                    strWeekDay = "دوشنبه";
                    break;
                case 2:
                    strWeekDay = "سه شنبه";
                    break;
                case 3:
                    strWeekDay = "چهارشنبه";
                    break;
                case 4:
                    strWeekDay = "پنج شنبه";
                    break;
                case 5:
                    strWeekDay = "جمعه";
                    break;
                case 6:
                    strWeekDay = "شنبه";
                    break;
            }

        }

    }
    public static String yearPersian() {
        Locale loc = new Locale("en_US");
        ConvertPersianCal util = new ConvertPersianCal();
        SolarCalendar sc = util.new SolarCalendar();
        return String.valueOf(sc.year);
    }
    public static String dayPersian() {
        Locale loc = new Locale("en_US");
        ConvertPersianCal util = new ConvertPersianCal();
        SolarCalendar sc = util.new SolarCalendar();
        return String.valueOf(String.format(loc, "%02d", sc.date));
    }
    public static String weekPersianCal() {

        ConvertPersianCal util = new ConvertPersianCal();
        SolarCalendar sc = util.new SolarCalendar();
        return sc.strWeekDay;
    }
    public static String mounthPersianCal() {

        ConvertPersianCal util = new ConvertPersianCal();
        SolarCalendar sc = util.new SolarCalendar();
        return sc.strMonth;
    }


    public static String toPersianNumber(String text) {
        if (text.length() == 0)
            return "";
        String out = "";
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if ('0' <= c && c <= '9') {
                int number = Integer.parseInt(String.valueOf(c));
                out += persianNumbers[number];
            } else if (c == '٫') {
                out += '،';
            } else {
                out += c;
            }

        }
        return out;
    }
    public static String toEnglishNumber(String text) {
        if (text.length() == 0)
            return "";
        String out = "";
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if ('۰' <= c && c <= '۹') {
                int number = Integer.parseInt(String.valueOf(c));
                out += englishNumbers[number];
            } else if (c == '٫') {
                out += '،';
            } else {
                out += c;
            }

        }
        return out;
    }
}
