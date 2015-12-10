package com.hp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import com.hp.core.Constants;

/**
 * Date Utility Class used to convert Strings to Dates and Timestamps
 *
 * @author Zhefang Chen
 */
public final class DateUtil {
//    private static Log log = LogFactory.getLog(DateUtil.class);
    private static final String TIME_PATTERN = Constants.DEFAULT_TIME_PATTERN;

    /**
     * Checkstyle rule: utility classes should not have public constructor
     */
    private DateUtil() {
    }

    /**
     * Return default datePattern (MM/dd/yyyy)
     *
     * @return a string representing the date pattern on the UI
     */
    public static String getDatePattern() {
        Locale locale = LocaleContextHolder.getLocale();
        String defaultDatePattern;
        try {
            defaultDatePattern = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale)
                    .getString("date.format");
        } catch (MissingResourceException mse) {
            defaultDatePattern = Constants.DEFAULT_DATE_PATTERN;
        }

        return defaultDatePattern;
    }

    public static String getDateTimePattern() {
        return Constants.DEFAULT_DATETIME_PATTERN;
    }

    public static String getDate(Date aDate) {
        SimpleDateFormat df;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(getDatePattern());
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @throws ParseException when String doesn't match the expected format
     * @see java.text.SimpleDateFormat
     */
    public static Date convertStringToDate(String aMask, String strDate)
            throws ParseException {
        SimpleDateFormat df;
        Date date;
        df = new SimpleDateFormat(aMask);

//        if (log.isDebugEnabled()) {
//            log.debug("converting '" + strDate + "' to date with mask '" + aMask + "'");
//        }

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            //log.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }
    
    public static Calendar convertStringToCalendar(String aMask, String strDate)  {
    	Calendar cal = null;
    	try {
    		cal = Calendar.getInstance();
    		cal.setTime(convertStringToDate(aMask, strDate));
	    } catch (ParseException pe) {
		    //log.error("ParseException: " + pe);
		    //throw new ParseException(pe.getMessage(), pe.getErrorOffset());
//	    	log.error(pe);
	    	
		}
		
		return cal;
	}
	    

    /**
     * This method returns the current date time in the format:
     * MM/dd/yyyy HH:MM a
     *
     * @param theTime the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(TIME_PATTERN, theTime);
    }

    /**
     * This method returns the current date in the format: MM/dd/yyyy
     *
     * @return the current date
     * @throws ParseException when String doesn't match the expected format
     */
    public static Calendar getToday() throws ParseException {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

        // This seems like quite a hack (date -> string -> date),
        // but it works ;-)
        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));

        return cal;
    }

    /**
     * This method generates a string representation of a date's date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     * @see java.text.SimpleDateFormat
     */
    public static String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
//            log.warn("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date based
     * on the System Property 'dateFormat'
     * in the format you specify on input
     *
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static String convertDateToString(Date aDate) {
        return getDateTime(getDatePattern(), aDate);
    }
    
    public static String convertDateToString(Date date,String pattern) {
    	if(!StringUtil.isNotEmpty(pattern)){
    		pattern=getDatePattern();
    	}
        return getDateTime(pattern, date);
    }

    /**
     * This method converts a String to a date using the datePattern
     *
     * @param strDate the date to convert 
     * @return a date object
     * @throws ParseException when String doesn't match the expected format
     */
    public static Date convertStringToDate(final String strDate) throws ParseException {
        return convertStringToDate(getDatePattern(), strDate);
    }
    
    public static Date fillTime(final Date date, int hours, int minutes, int seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hours);
		cal.set(Calendar.MINUTE, minutes);
		cal.set(Calendar.SECOND, seconds);
		return cal.getTime();
    }
}
