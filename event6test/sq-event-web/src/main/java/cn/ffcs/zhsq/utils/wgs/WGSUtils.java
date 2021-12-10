package cn.ffcs.zhsq.utils.wgs;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfo;
import cn.ffcs.zhsq.utils.wgs.GISWebServiceStub.Wgs84ToXm92List;
import cn.ffcs.zhsq.utils.wgs.GISWebServiceStub.Wgs84ToXm92ListResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class WGSUtils {
	
	private static Logger logger = LoggerFactory.getLogger(WGSUtils.class);

	public static void Wgs84ToXm92List(List<? extends ArcgisInfo> gisInfos) {
		if (gisInfos != null && gisInfos.size() > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			List<Object[]> points = new ArrayList<Object[]>();
			
			for (ArcgisInfo arcgisInfo : gisInfos) {
				Object[] xy = new Object[2];
				xy[0] = arcgisInfo.getX();
				xy[1] = arcgisInfo.getY();
				points.add(xy);
			}
			
			params.put("Points", points);
			
			String pointsJson = JSON.toJSONString(params);
			
			logger.info("发送84经纬度数据：" + pointsJson);
			
			Wgs84ToXm92List list = new Wgs84ToXm92List();
			try {
				list.setCoords(pointsJson);
				
				GISWebServiceStub stub = new GISWebServiceStub();
				Wgs84ToXm92ListResponse response = stub.wgs84ToXm92List(list);
				String resultJson = response.getWgs84ToXm92ListResult();
				logger.info("接收92经纬度数据：" + resultJson);
				if (StringUtils.isNotBlank(resultJson)) {
					JSONObject jsonObj = JSON.parseObject(resultJson);
					JSONArray obj = (JSONArray) jsonObj.get("Points");
					for (int i = 0; i < obj.size(); i++) {
						JSONArray xy = (JSONArray) obj.get(i);
						if (xy != null && xy.size() > 1) {
							ArcgisInfo arcgisInfoOfPublic = gisInfos.get(i);
							arcgisInfoOfPublic.setX(xy.getDouble(0));
							arcgisInfoOfPublic.setY(xy.getDouble(1));
						}
					}
				}
			} catch (AxisFault e) {
				throw new RuntimeException(e);
			} catch (RemoteException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
