package cn.ffcs.zhsq.event.service;

import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;

public interface IEventExDsService {

	String[] report(EventDisposal event);

}
