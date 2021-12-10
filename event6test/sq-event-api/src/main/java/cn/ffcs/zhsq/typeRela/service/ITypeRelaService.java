package cn.ffcs.zhsq.typeRela.service;

import cn.ffcs.uam.bo.UserInfo;

import java.util.Map;

/**
 * Created by 张天慈 on 2018/3/22.
 */
public interface ITypeRelaService {

	/**
	 * 新增类别关联记录
	 * @param    param
	 * 必填参数	 bizType   关联表（T_TYPE_RELA）业务类型：00执纪问责问题，02南昌扫黑除恶-涉及打击重点
	 * 			 bizId	   业务ID
	 *			 typeValue 多选字段值（以英文逗号相隔的字典业务编码）
	 *			 typeCode  父级字典编码
	 *		     bizColumn 关联表业务字段
	 *
	 * */
	public Long insertTypeRela(Map<String,Object> param, UserInfo userInfo);


	/**
	 * 根据业务ID，业务类型删除类别关联记录
	 * @param param
	 * 必填参数
	 * 		bizId    关联业务ID
	 * 		bizType  关联业务类型
	 *
	 * */
	public Boolean delTypeRela(Map<String,Object> param);
}
