package cn.ffcs.zhsq.utils;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 * 自定义类型绑定器
 * @author Administrator
 *
 */
public class CustomWebBinding implements WebBindingInitializer {

	@Override
	public void initBinder(WebDataBinder webDataBinder) {

	}
	
	public void initBinder(WebDataBinder binder, WebRequest arg1) {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		//增加TimeStamp的类型编辑器
		final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		binder.registerCustomEditor(Date.class, new TimestampEditor(dateTimeFormat, true));
	}

	public class TimestampEditor extends PropertyEditorSupport {

		private DateFormat dateFormat;
		private boolean allowEmpty;

		public TimestampEditor() {
			dateFormat = new SimpleDateFormat("MM.dd.yy HH:mm");
			allowEmpty = true;
		}

		public TimestampEditor(DateFormat dateFormat, boolean allowEmpty) {
			this.dateFormat = dateFormat;
			this.allowEmpty = allowEmpty;
		}

		
		public String getAsText() {
			Timestamp timestamp = (Timestamp) getValue();
			return timestamp == null ? "" : dateFormat.format(timestamp);
		}

		
		public void setAsText(String s) throws IllegalArgumentException {
			if (allowEmpty && !StringUtils.hasText(s)) {
				setValue(null);
				return;
			}
			try {
				Date date = dateFormat.parse(s);
				Timestamp timestamp = new Timestamp(date.getTime());
				setValue(timestamp);
			} catch (ParseException e) {
				throw new IllegalArgumentException("Could not parse Date Time " + e.getMessage());
			}

		}
	}
}
