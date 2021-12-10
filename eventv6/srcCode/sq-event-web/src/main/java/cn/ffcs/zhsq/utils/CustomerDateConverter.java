package cn.ffcs.zhsq.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;

public class CustomerDateConverter implements Converter<String, Date> {
	
	public Set<String> dateFormats;
	
	@Override
	public Date convert(String source) {
		if(source != null){
			int len = source.length();
			if(dateFormats != null){
				for(String s : dateFormats){
					try {
						return new SimpleDateFormat(s).parse(source);
					} catch (ParseException e) {}
				}
				throw new RuntimeException("格式化时间异常");
			}else{
				try {
					switch(len){
						case 4 : return new SimpleDateFormat("yyyy").parse(source);
						case 5 : return new SimpleDateFormat("yyyy年").parse(source);
						case 6 : return new SimpleDateFormat("yyyyMM").parse(source);
						case 7 : return new SimpleDateFormat("yyyy-MM").parse(source);
						case 8 : return new SimpleDateFormat("yyyy年MM月").parse(source);
						case 10 : return new SimpleDateFormat("yyyy-MM-dd").parse(source);
						case 11 : return new SimpleDateFormat("yyyy年MM月dd日").parse(source);
						case 19 : return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(source);
						case 20 : return new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").parse(source);
						default : throw new RuntimeException("格式化时间异常");
					}
				} catch (ParseException e) {
					throw new RuntimeException("格式化时间异常");
				}
			}
		}
		return null;
	}

	public Set<String> getDateFormats() {
		return dateFormats;
	}

	public void setDateFormats(Set<String> dateFormats) {
		this.dateFormats = dateFormats;
	}

}
