package cn.ffcs.zhsq.map.sanitationtrunck.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.zhsq.event.service.ITimerTaskService;
import cn.ffcs.zhsq.map.sanitationtrunck.SanitationTrunckService;
import cn.ffcs.zhsq.mybatis.domain.map.sanitationtrunck.SanitationTrunck;

@Component("synchroSanitationTrunckJob")
public class SynchroSanitationTrunckJob extends ApplicationObjectSupport implements ITimerTaskService {
	private Logger logger = LoggerFactory.getLogger(SynchroSanitationTrunckJob.class);

	@Value("${syn_sanitationtrunck}")
	private String synUrl;
	
	@Autowired
	private SanitationTrunckService sanitationTrunckService;
	
	@Override
	public void run() {
		logger.info("开始调用定时器...获取环卫车基础数据");
		//环卫车基础数据同步数据库
		mergeData(synUrl);
	}
	
	@Override
	public void execute() {
		run();
	}
	
	/**
	 * 同步数据
	 * @param <T>
	 */
	@SuppressWarnings("unchecked")
	private <T> void mergeData(String configUrl){
		try {
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			JSONObject json = HttpUtil.invokeOutInterface(synUrl, "POST", "", headers, null);
			
			int returnCode = json.getInt("returnCode");
			if(returnCode == 0){
				String result = json.getString("result");
				JSONArray array = JSONArray.fromObject(result);
				if(!array.isEmpty() && array.size() > 0){
					List<SanitationTrunck> list = JsonHelper.getObjectList(array.toString(), SanitationTrunck.class);
					sanitationTrunckService.sysSanitatinTrunckInfo(list);
				}
			}else{
				logger.error("环卫车基础数据同步失败",SynchroSanitationTrunckJob.class.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
