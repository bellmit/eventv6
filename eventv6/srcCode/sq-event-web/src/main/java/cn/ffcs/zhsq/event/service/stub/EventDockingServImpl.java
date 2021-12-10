package cn.ffcs.zhsq.event.service.stub;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.JaxbBinder;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusTwoWayService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.stub.EventDockingServiceImplStub;
import cn.ffcs.zhsq.event.service.stub.WebServicePublishImplServiceStub;
import cn.ffcs.zhsq.event.service.stub.WebServiceStub;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.XmlDataAuth;
import cn.ffcs.zhsq.mybatis.domain.event.XmlDataResponse;
import cn.ffcs.zhsq.mybatis.domain.event.XmlDataResult;
import cn.ffcs.zhsq.mybatis.domain.event.XmlEventResult;
import cn.ffcs.zhsq.mybatis.domain.event.XmlTaskResult;
import cn.ffcs.zhsq.mybatis.domain.event.docking.AHResultData;
import cn.ffcs.zhsq.mybatis.domain.event.docking.ReceivedCommand;
import cn.ffcs.zhsq.mybatis.domain.event.docking.ReportedFeedback;
import cn.ffcs.zhsq.mybatis.domain.event.docking.ReportedIncident;
import cn.ffcs.zhsq.mybatis.domain.event.docking.SJLCData;
import cn.ffcs.zhsq.mybatis.domain.event.docking.SJLCDatas;
import cn.ffcs.zhsq.mybatis.domain.event.docking.SJLCResult;
import cn.ffcs.zhsq.mybatis.domain.event.docking.SJXXData;
import cn.ffcs.zhsq.mybatis.domain.event.docking.SJXXDatas;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;


public class EventDockingServImpl {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**迪艾斯便民服务*/
	private String BIZPLATFORM_DS = "004";
	/**南威市级平台*/	
	private String BIZPLATFORM_NW = "007";
	/**西滨*/
	private String BIZPLATFORM_XB = "008";
	/**安海*/
	private String BIZPLATFORM_AH = "009";
	/**梅岭街道*/
	private String BIZPLATFORM_ML = "010";
	
	@Autowired
	private IDataExchangeStatusService dataExchangeStatusService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IEventDisposalService eventDisposalService;
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private IDataExchangeStatusTwoWayService dataExchangeStatusTwoWayService;

	private String nwWebServiceUri;
	private String nwWebServicTaskUri;
	private String dsWebServiceUri;
	private String xbWebServiceUri = "http://220.162.239.191:7777/services/eventDockingServiceImpl?wsdl";
	private String ahWebServiceUri;
	
	private boolean doSync = true;
	
	private void run(){
		if(doSync){
			logger.info("================Timer task start================");
			
			
			logger.info("================Timer task stop================");
		}
	}
}
