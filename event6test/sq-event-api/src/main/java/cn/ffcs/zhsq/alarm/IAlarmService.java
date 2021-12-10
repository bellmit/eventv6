package cn.ffcs.zhsq.alarm;

import java.util.List;

import cn.ffcs.doorsys.bo.equipment.AfsLog;
import cn.ffcs.zhsq.mybatis.domain.alarm.AlarmLog;
import cn.ffcs.zhsq.mybatis.domain.alarm.SnapRecordInfo;


public interface IAlarmService {

	SnapRecordInfo getData();

	SnapRecordInfo getAlarmDatas(String beginTime, String endTime);

}
