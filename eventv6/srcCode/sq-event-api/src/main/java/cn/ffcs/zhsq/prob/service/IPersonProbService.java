package cn.ffcs.zhsq.prob.service;

import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;

public interface IPersonProbService {

	EUDGPagination searchList(int page, int rows, Map<String, Object> params);

	Map<String, Object> searchById(int probId);

}
