package cn.ffcs.zhsq.mybatis.domain.checkConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CheckResultInfo implements Serializable {
	private String funcName;		//功能名称
	private String funcCode;		//功能编码
	private String orgCode;			//组织域编码
	private String orgName;			//组织域名称
	private String status;			//功能配置状态 001启用；003删除
	private String resultCode;		//检测结果编码 0检测通过 1检测不通过  2警告
	private List<String> reasonDesc = new ArrayList<String>();		//失败原因描述
	private List<String> solutionDesc = new ArrayList<String>();		//解决方案描述
	public String getFuncName() {
		return funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	public String getFuncCode() {
		return funcCode;
	}
	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public List<String> getReasonDesc() {
		return reasonDesc;
	}
	public void setReasonDesc(List<String> reasonDesc) {
		this.reasonDesc = reasonDesc;
	}
	public List<String> getSolutionDesc() {
		return solutionDesc;
	}
	public void setSolutionDesc(List<String> solutionDesc) {
		this.solutionDesc = solutionDesc;
	}
	
	//添加失败原因
	public void addReasonDesc(String reason){
		this.reasonDesc.add(reason);
	}
	
	//添加失败原因
	public void addSolutionDesc(String solution){
		this.solutionDesc.add(solution);
	}
	
}
