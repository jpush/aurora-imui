package cn.jiguang.imui.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {

    private DateFormatter() {
        throw new AssertionError();
    }

    public static String format(Date date, Template template) {
        return format(date, template.get());
    }

    public static String format(Date date, String format) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(format, Locale.getDefault()).format(date);
    }

    public enum Template {
        STRING_MONTH("dd MMMM yyyy"),
        TIME("HH:mm");

        private String template;

        Template(String template) {
            this.template = template;
        }

        public String get() {
            return template;
        }
    }
}
