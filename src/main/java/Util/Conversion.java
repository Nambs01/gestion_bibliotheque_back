package Util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.Integer.parseInt;

public class Conversion {
    public static java.sql.Date convertDateStringToSqlDate(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = null;
        java.sql.Date sqlDate = null;
        try {
            utilDate = df.parse(dateStr);
            sqlDate = new java.sql.Date(utilDate.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return sqlDate;
    }

    public static java.sql.Timestamp convertDateStringToSqlTimestamp(String dateStr) {// Exemple de chaîne de date à convertir
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.sql.Timestamp timestamp = null;

        try {
            // Try to parse the string as a long (timestamp)
            long timeInMillis = Long.parseLong(dateStr);
            timestamp = new java.sql.Timestamp(timeInMillis);
        } catch (NumberFormatException e) {
            // If it's not a valid long, try to parse it as a date string
            try {
                java.util.Date parsedDate = dateFormat.parse(dateStr);
                timestamp = new java.sql.Timestamp(parsedDate.getTime());
            } catch (ParseException pe) {
                throw new RuntimeException("Invalid date format. Please use 'yyyy-MM-dd HH:mm:ss' or a valid timestamp.", pe);
            }
        }
        return timestamp;
    }

    public static int convertStringToNumber(String numberStr) {
        return Integer.parseInt(numberStr);
    }

    public static String sqlDateToString(java.sql.Date sqlDate) {
        LocalDate localDate = sqlDate.toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return localDate.format(formatter);
    }
    public static String sqlDateToString(Timestamp sqlDate) {
        LocalDateTime localDate = sqlDate.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return localDate.format(formatter);
    }
}
