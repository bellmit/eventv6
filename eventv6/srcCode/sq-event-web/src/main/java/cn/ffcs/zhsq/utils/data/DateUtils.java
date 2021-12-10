package cn.ffcs.zhsq.utils.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


public class DateUtils {
	

	public final static String PATTERN_24TIME = "yyyy-MM-dd HH:mm:ss"; 
	public final static String PATTERN_DATE = "yyyy-MM-dd"; 
    /**
     * 根据传入的模式参数返回当天的日期
     * @param pattern 传入的模式
     * @return 按传入的模式返回一个字符串
     */
    public static String getToday(String pattern)
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
    
    /**
     * 根据传入的模式参数返回当天的日期
     * @param pattern 传入的模式
     * @return 按传入的模式返回一个字符串
     */
    public static String getToday()
    {
        return getToday("yyyy-MM-dd");
    }
    
    /**
     * 根据传入的模式参数返回当天的日期
     * @param pattern 传入的模式
     * @return 按传入的模式返回一个字符串
     */
    public static String getNow()
    {
    	return getToday("yyyy-MM-dd HH:mm:ss");
    }   
    
    /**
     * 比较两个日期大小
     * @param date1 日期字符串
     * @param pattern1 日期格式
     * @param date2 日期字符串
     * @param pattern2 日期格式
     * @return boolean 若是date1比date2小则返回true
     * @throws ParseException
     */
    public static boolean compareMinDate(String date1, String pattern1,
                                         String date2, String pattern2) throws ParseException
    {
        Date d1 = convertToCalendar(date1, pattern1).getTime();
        Date d2 = convertToCalendar(date2, pattern2).getTime();
        return d1.before(d2);
    }

    /**
     * 比较两个日期大小
     * @param date1 Date
     * @param date2 Date
     * @return boolean 若是date1比date2小则返回true
     */
    public static boolean compareMinDate(Date date1, Date date2)
    {
        try
        {
            return DateUtils.compareMinDate(DateUtils.formatDate(date1, "yyyy-MM-dd HH:mm:ss"),
                                           "yyyy-MM-dd HH:mm:ss",
                                           DateUtils.formatDate(date2, "yyyy-MM-dd HH:mm:ss"),
                                           "yyyy-MM-dd HH:mm:ss");
        }
        catch(Exception ex)
        {
            return false;
        }
    }

    /**
     * 根据传入的日期字符串以及格式，产生一个Calendar对象
     * @param date 日期字符串
     * @param pattern 日期格式
     * @return Calendar
     * @throws ParseException 当格式与日期字符串不匹配时抛出该异常
     */
    public static Calendar convertToCalendar(String date, String pattern) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date d = sdf.parse(date);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        return calendar;
    }

    /**
     * 用途：以指定的格式格式化日期字符串
     * @param pattern 字符串的格式
     * @param currentDate 被格式化日期
     * @return String 已格式化的日期字符串
     * @throws NullPointerException 如果参数为空
     */
    public static String formatDate(Calendar currentDate, String pattern)
    {
        if(currentDate == null || pattern == null)
        {
            throw new NullPointerException("The arguments are null !");
        }
        Date date = currentDate.getTime();
        return formatDate(date, pattern);
    }

    /**
     * 用途：以指定的格式格式化日期字符串
     * @param pattern 字符串的格式
     * @param currentDate 被格式化日期
     * @return String 已格式化的日期字符串
     * @throws NullPointerException 如果参数为空
     */
    public static String formatDate(Date currentDate, String pattern){
        if(currentDate == null || "".equals(pattern) || pattern == null){
        	return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(currentDate);
    }

    /**
     * 用途：以指定的格式格式化日期字符串
     * @param currentDate 被格式化日期字符串 必须为yyyy-MM-dd
     * @param pattern 字符串的格式
     * @return String 已格式化的日期字符串
     * @throws NullPointerException 如果参数为空
     * @throws java.text.ParseException 若被格式化日期字符串不是yyyy-MM-dd形式时抛出
     */
    public static String formatDate(String currentDate, String pattern)
    {
        if(currentDate == null || pattern == null  || "".equals(currentDate))
        {
        	return null;
            //throw new NullPointerException("The arguments are null !");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
		try {
			date = sdf.parse(currentDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        sdf.applyPattern(pattern);
        return sdf.format(date);
    }
    
    public static Date formatDate(String date, String pattern, String dd){
        if(date == null || pattern == null)
        {
        	return null;
            //throw new NullPointerException("The arguments are null !");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return d;
    }

    /**
     * 用途：以指定的格式格式化日期字符串
     * @param strDate 被格式化日期字符串 必须为yyyymmdd
     * @param formator 格式字符串
     * @return String 已格式化的日期字符串
     * @throws NullPointerException 如果参数为空
     * @throws java.text.ParseException 若被格式化日期字符串不是yyyymmdd形式时抛出
     */
    public static Calendar strToDate(String strDate, String formator)
    {
        if(strDate == null || formator == null)
        {
            throw new NullPointerException("The arguments are null !");
        }

        Calendar date = Calendar.getInstance();
        date.setTime(java.sql.Date.valueOf(strDate));
        return date;
    }

    /**
     * 判断当前时间是否在参数时间内（当开始时间大于结束时间表示时间段的划分从begin到第二天的end时刻）
     *  例如当前时间在12：00 传入参数为（12,12,0,1）返回true
     *  例如当前时间在12：00 传入参数为（12,12,1,0）返回true
     * @param beginHour int 开始的小时值
     * @param endHour int   结束的小时值
     * @param beginMinu int 开始的分钟值
     * @param endMinu int   结束的分钟值
     * @return boolean
     */
    public static boolean isInTime(int beginHour, int endHour,
                                   int beginMinu,
                                   int endMinu)
    {
        Date date1 = new Date();
        Date date2 = new Date();
        Date nowDate = new Date();
        date1.setHours(beginHour);
        date2.setHours(endHour);
        date1.setMinutes(beginMinu);
        date2.setMinutes(endMinu);
        if(date1 == date2)
        {
            return false;
        }
        //yyyy-MM-dd HH:mm:ss
        if(
                DateUtils.compare(date2, date1))
        {
            if(!DateUtils.compare(nowDate, date1)
               || DateUtils.compare(nowDate, date2))
            {
                return true;
            }
        }
        else
        {
            if(
                    !DateUtils.compare(nowDate, date1) &&
                    DateUtils.compare(nowDate, date2)
                    )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 开始时间小于结束时间返回true，否则返回false
     * @param beginDate Date
     * @param endDate Date
     * @return boolean
     */
    private static boolean compare(Date beginDate, Date endDate)
    {
        try
        {

            return DateUtils.compareMinDate(DateUtils.formatDate(beginDate,
                    "yyyy-MM-dd HH:mm:ss"),
                                           "yyyy-MM-dd HH:mm:ss",
                                           DateUtils.formatDate(endDate,
                    "yyyy-MM-dd HH:mm:ss"),
                                           "yyyy-MM-dd HH:mm:ss");

        }
        catch(Exception ex)
        {
//            log.error ( "时间格式转换错误" + ex ) ;
            return false;
        }
    }

    /**
     * 将指定格式的时间String转为Date类型
     * @param dateStr String 待转换的时间String
     * @param pattern String 转换的类型
     * @throws ParseException
     * @return Date
     */
    public static Date convertStringToDate(String dateStr,String patternner) throws ParseException
    {
        String pattern =patternner;

        if(StringUtils.isBlank(dateStr)){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(dateStr);
    }

    public static String convertDateToString(Date date) throws ParseException
    {
        if(date == null)
        {
            return "";
        }
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }
    
    public static void main(String[] args){
//    	try {
//			String sdate = formatDate("2006-03-01","yyyy-MM-dd");
//			System.out.println(">>>>>>>>>>>>>>>"+sdate);
//		} catch (ParseException e) {
//		
//			e.printStackTrace();
//		}
    }
    
    /**
     * 给日期增加时间间隔
     * 
     * eg.给当前的日期加一周:addInterval(date, 1, "01");
     * 
     * @param date 基础日期
     * @param num 时间间隔量
     * @param timeUnit 时间间隔单位 00:Calendar.DAY_OF_YEAR;01:Calendar.WEEK_OF_YEAR;02:Calendar.MONTH;否则返回null
     * @return 增加后的日期
     * @author shenyj 2012/07/17 created
     */
    public static Date addInterval(Date date, int num, String timeUnit) {
    	if (date == null) {
    		return null;
    	}
    	Integer calendarTimeUnit = convert2CalendarUnit(timeUnit);
    	if (calendarTimeUnit == null) {
    		//不支持的单位转换或者timeUnit为null，则直接返回date
    		return date;
    	}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(calendarTimeUnit, num);
		return calendar.getTime();
    }

    /**
     * 给日期减去时间间隔
     *
     * eg.给当前的日期减一周:subInterval(date, 1, "01");
     *
     * @param date 基础日期
     * @param num 时间间隔量
     * @param timeUnit 时间间隔单位 00:Calendar.DAY_OF_YEAR;01:Calendar.WEEK_OF_YEAR;02:Calendar.MONTH;否则返回null
     * @return 增加后的日期
     * @author sulch 2018/01/08 created
     */
    public static Date subInterval(Date date, int num, String timeUnit) {
        if (date == null) {
            return null;
        }
        Integer calendarTimeUnit = convert2CalendarUnit(timeUnit);
        if (calendarTimeUnit == null) {
            //不支持的单位转换或者timeUnit为null，则直接返回date
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendarTimeUnit, -num);
        return calendar.getTime();
    }
    
    /**
	 * 将时间单位转换为Calendar类的单位
	 * 
	 * @param timeUnit 时间单位 00:Calendar.DAY_OF_YEAR;01:Calendar.WEEK_OF_YEAR;02:Calendar.MONTH;否则返回null
	 * @return 
	 * @author shenyj 2012/07/17 created
	 */
	private static Integer convert2CalendarUnit(String timeUnit) {
		if (timeUnit != null && !"".equals(timeUnit)) {
			if ("00".equals(timeUnit)) {
				return Calendar.DAY_OF_YEAR;//日
			} else if ("01".equals(timeUnit)) {
				return Calendar.WEEK_OF_YEAR;//周
			} else if ("02".equals(timeUnit)) {
				return Calendar.MONTH;//月
			}
		}
		
		return null;
	}
	
	/**
	 * 格式化分钟，如“2000”转化为“1天9小时20分钟”
	 * 
	 * @param minutes
	 * @return
	 */
	public static String formatMinute(int minutes) {
		if (minutes <= 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int m = minutes % 60;
		if (m != 0) {
			sb.append(m).append("分钟");
		}
		int hours = minutes / 60;
		if (hours == 0) {
			return sb.toString();
		}
		int h = hours % 24;
		if (h != 0) {
			sb.insert(0, h + "小时");
		}
		int days = hours / 24;
		if (days == 0) {
			return sb.toString();
		}
		sb.insert(0, days + "天");
		return sb.toString();
	}

	
	// 取得当前日期
	public static String getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String today = formatter.format(cal.getTime());
		return today;
	}
	
	public static String getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("HH-mm-ss");
		String today = formatter.format(cal.getTime());
		return today;
	}
	
	// 获得当天的日期
	public static String getCurrDateString() {
		Calendar rightNow = Calendar.getInstance();
		String y = getCurrYear();
		String m = "";
		String d = "";
		int month = rightNow.get(Calendar.MONTH) + 1;
		if (month < 10) {
			m = "0" + month;
		} else {
			m = month + "";
		}
		int date = rightNow.get(Calendar.DATE);
		if (date < 10) {
			d = "0" + date;
		} else {
			d = date + "";
		}
		return y + "-" + m + "-" + d;
	}

	// 获得当天的日期
	public static String getCurrDateCompact() {
		Calendar rightNow = Calendar.getInstance();
		String y = getCurrYear();
		String m = "";
		String d = "";
		int month = rightNow.get(Calendar.MONTH) + 1;
		if (month < 10) {
			m = "0" + month;
		} else {
			m = month + "";
		}
		int date = rightNow.get(Calendar.DATE);
		if (date < 10) {
			d = "0" + date;
		} else {
			d = date + "";
		}
		return y + m + d;
	}

	// 获得当天的年份 return String
	public static String getCurrYear() {
		Calendar rightNow = Calendar.getInstance();
		return rightNow.get(Calendar.YEAR) + "";
	}
	
	/**
	 * 某一个月第一天和最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Map<String, Object> getFirstLastDayByMonth(Date date,
			String pattern, boolean isNeedHms) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Date theDate = calendar.getTime();

		// 上个月第一天
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		String day_first = df.format(gcLast.getTime());
		StringBuffer str = new StringBuffer().append(day_first);
		if (isNeedHms)
			str.append(" 00:00:00");
		day_first = str.toString();

		// 上个月最后一天
		calendar.add(Calendar.MONTH, 1); // 加一个月
		calendar.set(Calendar.DATE, 1); // 设置为该月第一天
		calendar.add(Calendar.DATE, -1); // 再减一天即为上个月最后一天
		String day_last = df.format(calendar.getTime());
		StringBuffer endStr = new StringBuffer().append(day_last);
		if (isNeedHms)
			endStr.append(" 23:59:59");
		day_last = endStr.toString();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("first", day_first);
		map.put("last", day_last);
		return map;
	}
        
    public static int getDifferYears(Date begin, Date end) {
           Calendar c1 = Calendar.getInstance();
           Calendar c2 = Calendar.getInstance();
           c1.setTime(begin);
           c2.setTime(end);
           return Math.abs(c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR));
	}
    
	public static String getQuarter() {
		Calendar c = Calendar.getInstance();
		int month = c.get(c.MONTH) + 1;
		int quarter = 0;
		if (month >= 1 && month <= 3) {
			quarter = 1;
		} else if (month >= 4 && month <= 6) {
			quarter = 2;
		} else if (month >= 7 && month <= 9) {
			quarter = 3;
		} else {
			quarter = 4;
		}
		return quarter + "";
	}
	
	public static String[] getCurrQuarter(int num) {
		String[] s = new String[2];
		String str = "";
		// 设置本年的季
		Calendar quarterCalendar = null;
		switch (num) {
		case 1: // 本年到现在经过了一个季度，在加上前4个季度
			quarterCalendar = Calendar.getInstance();
			quarterCalendar.set(Calendar.MONTH, 3);
			quarterCalendar.set(Calendar.DATE, 1);
			quarterCalendar.add(Calendar.DATE, -1);
			str = DateUtils.formatDate(quarterCalendar.getTime(), "yyyy-MM-dd");
			s[0] = str.substring(0, str.length() - 5) + "01-01";
			s[1] = str;
			break;
		case 2: // 本年到现在经过了二个季度，在加上前三个季度
			quarterCalendar = Calendar.getInstance();
			quarterCalendar.set(Calendar.MONTH, 6);
			quarterCalendar.set(Calendar.DATE, 1);
			quarterCalendar.add(Calendar.DATE, -1);
			str = DateUtils.formatDate(quarterCalendar.getTime(), "yyyy-MM-dd");
			s[0] = str.substring(0, str.length() - 5) + "04-01";
			s[1] = str;
			break;
		case 3:// 本年到现在经过了三个季度，在加上前二个季度
			quarterCalendar = Calendar.getInstance();
			quarterCalendar.set(Calendar.MONTH, 9);
			quarterCalendar.set(Calendar.DATE, 1);
			quarterCalendar.add(Calendar.DATE, -1);
			str = DateUtils.formatDate(quarterCalendar.getTime(), "yyyy-MM-dd");
			s[0] = str.substring(0, str.length() - 5) + "07-01";
			s[1] = str;
			break;
		case 4:// 本年到现在经过了四个季度，在加上前一个季度
			quarterCalendar = Calendar.getInstance();
			str = DateUtils.formatDate(quarterCalendar.getTime(), "yyyy-MM-dd");
			s[0] = str.substring(0, str.length() - 5) + "10-01";
			s[1] = str.substring(0, str.length() - 5) + "12-31";
			break;
		}
		return s;
	}

    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate,Date bdate) {

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            smdate=sdf.parse(sdf.format(smdate));
            bdate=sdf.parse(sdf.format(bdate));
        }catch (Exception e){
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 字符串的日期格式的计算
     * @param smdate 较小的时间
     * @param bdate 较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(String smdate,String bdate) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        long between_days = 0l;
        try {
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            between_days=(time2-time1)/(1000*3600*24);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Integer.parseInt(String.valueOf(between_days));
    }
}