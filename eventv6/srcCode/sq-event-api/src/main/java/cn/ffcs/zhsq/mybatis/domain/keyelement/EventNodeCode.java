package cn.ffcs.zhsq.mybatis.domain.keyelement;

import java.io.Serializable;

import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;

public class EventNodeCode implements Serializable, INodeCodeHandler {

	private static final long serialVersionUID = 8593320497576321511L;

	private String nodeCode;

	private boolean report;
	
	private boolean reportX;

	private boolean send;

	private boolean splitFlow;
	
	private boolean splitFlowGlobal;

	private String lineCode;

	private int lineLevel;
	
	private boolean fromUnit;
	
	private boolean toUnit;
	
	private boolean fromDept;
	
	private boolean toDept;
	
	private boolean fromExclusiveGrid;
	
	private boolean toExclusiveGrid;

	private int fromLevel;

	private int toLevel;

	private boolean collect;

	private boolean close;

	private boolean comment;

	private boolean placeFile;

	private boolean person;
	
	private boolean organization;
	
	private boolean toCollect;//采集人员组织
	
	private boolean toBegin;//采集人员
	
	private boolean global;//展示全部组织
	
	private boolean allocation;//配置办理人员
	
	private boolean equality;//同组织操作
	
	public EventNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	@Override
	public boolean isReport() {
		return report;
	}

	@Override
	public boolean isReportX() {
		return reportX;
	}
	
	@Override
	public boolean isSend() {
		return send;
	}

	@Override
	public boolean isSplitFlow() {
		return splitFlow;
	}

	@Override
	public boolean isSplitFlowGlobal() {
		return splitFlowGlobal;
	}
	
	@Override
	public String getLineCode() {
		return lineCode;
	}

	@Override
	public int getLineLevel() {
		return lineLevel;
	}

	@Override
	public boolean isFromUnit() {
		return fromUnit;
	}

	@Override
	public boolean isToUnit() {
		return toUnit;
	}
	
	@Override
	public boolean isFromDept() {
		return fromDept;
	}

	@Override
	public boolean isToDept() {
		return toDept;
	}
	
	@Override
	public boolean isFromExclusiveGrid() {
		return fromExclusiveGrid;
	}

	@Override
	public boolean isToExclusiveGrid() {
		return toExclusiveGrid;
	}

	@Override
	public int getFromLevel() {
		return fromLevel;
	}

	@Override
	public int getToLevel() {
		return toLevel;
	}

	@Override
	public boolean isCollect() {
		return collect;
	}

	@Override
	public boolean isClose() {
		return close;
	}

	@Override
	public boolean isComment() {
		return comment;
	}

	@Override
	public boolean isPlaceFile() {
		return placeFile;
	}
	
	@Override
	public boolean isPerson(){
		return person;
	}

	@Override
	public boolean isOrganization(){
		return organization;
	}
	
	@Override
	public boolean isToBegin() {
		return toBegin;
	}
	
	@Override
	public boolean isEquality() {
		return equality;
	}

	public void setEquality(boolean equality) {
		this.equality = equality;
	}
	
	public void setToBegin(boolean toBegin) {
		this.toBegin = toBegin;
	}

	public void setOrganization(boolean organization){
		this.organization = organization;
	}
	
	public void setPerson(boolean person){
		this.person = person;
	}
	
	public void setReport(boolean isReport) {
		this.report = isReport;
	}
	
	public void setReportX(boolean isReportX) {
		this.reportX = isReportX;
	}

	public void setSend(boolean isSend) {
		this.send = isSend;
	}
	

	public void setSplitFlow(boolean isSplitFlow) {
		this.splitFlow = isSplitFlow;
	}
	
	public void setSplitFlowGlobal(boolean isSplitFlowGlobal) {
		this.splitFlowGlobal = isSplitFlowGlobal;
	}
	
	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}

	public void setLineLevel(int lineLevel) {
		this.lineLevel = lineLevel;
	}

	public void setFromLevel(int fromLevel) {
		this.fromLevel = fromLevel;
	}
	

	public void setToLevel(int toLevel) {
		this.toLevel = toLevel;
	}
	

	public void setCollect(boolean isCollect) {
		this.collect = isCollect;
	}
	

	public void setClose(boolean isClose) {
		this.close = isClose;
	}
	

	public void setComment(boolean isComment) {
		this.comment = isComment;
	}
	

	public void setPlaceFile(boolean isPlaceFile) {
		this.placeFile = isPlaceFile;
	}

	public void setFromUnit(boolean isFromUnit) {
		this.fromUnit = isFromUnit;
	}

	public void setToUnit(boolean isToUnit) {
		this.toUnit = isToUnit;
	}

	public void setFromDept(boolean isFromDept) {
		this.fromDept = isFromDept;
	}

	public void setToDept(boolean isToDept) {
		this.toDept = isToDept;
	}
	
	public void setFromExclusiveGrid(boolean isFromExclusiveGrid) {
		this.fromExclusiveGrid = isFromExclusiveGrid;
	}
	
	public void setToExclusiveGrid(boolean isToExclusiveGrid) {
		this.toExclusiveGrid = isToExclusiveGrid;
	}

	public String getNodeCode() {
		return nodeCode;
	}

	public void setToCollect(boolean toCollect) {
		this.toCollect = toCollect;
	}
	
	public void setGlobal(boolean global) {
		this.global = global;
	}
	
	public void setAllocation(boolean allocation) {
		this.allocation = allocation;
	}
	
	@Override
	public boolean isSendToGrid() {
		return this.isSend() && this.isToUnit() && this.getToLevel() == INodeCodeHandler.GRID;
	}

	@Override
	public boolean isSendToCommunity() {
		return this.isSend() && this.isToUnit() && this.getToLevel() == INodeCodeHandler.COMMUNITY;
	}

	@Override
	public boolean isReportToCommunity() {
		return this.isReport() && this.isToUnit() && this.getToLevel() == INodeCodeHandler.COMMUNITY;
	}

	@Override
	public boolean isGridReportToCommunity() {
		return this.isReportToCommunity() && this.isFromUnit() && this.getFromLevel() == INodeCodeHandler.GRID;
	}

	@Override
	public boolean isToGrid() {
		return this.isToUnit() && this.getToLevel() == INodeCodeHandler.GRID;
	}

	@Override
	public boolean isToCommunity() {
		return this.isToUnit() && this.getToLevel() == INodeCodeHandler.COMMUNITY;
	}

	@Override
	public boolean isFromGrid() {
		return this.isFromUnit() && this.getFromLevel() == INodeCodeHandler.GRID;
	}

	@Override
	public boolean isFromCommunity() {
		return this.isFromUnit() && this.getFromLevel() == INodeCodeHandler.COMMUNITY;
	}

	@Override
	public boolean isToCollect() {
		return toCollect;
	}
	
	@Override
	public boolean isGlobal() {
		return global;
	}
	
	@Override
	public boolean isAllocation() {
		return allocation;
	}

}
