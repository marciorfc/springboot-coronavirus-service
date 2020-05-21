package com.base.coronavirustracker.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String formatarData(String data, String maskFrom, String maskTo) {
        if (data!=null && !maskFrom.equals(maskTo)) {
	        Date dt = parseToDate(data, maskFrom); 
	        String newData = parseFromDate(dt, maskTo);
	        return newData;
        }
		return data;
	}

	
	public static Date parseToDate(String data, String mask){
        try {
            SimpleDateFormat format = new SimpleDateFormat(mask);
            format.setLenient(false);
            Date dt = format.parse(data);
            return dt;
        } catch (ParseException e) {}
        return null;
	}

	public static String parseFromDate(Date data, String mask){
        SimpleDateFormat format = new SimpleDateFormat(mask);
        String dt = format.format(data);
        return dt;
	}
}