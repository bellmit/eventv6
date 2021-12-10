package cn.ffcs.zhsq.event.service.stub.jiangyin.XunfangApp;/**
 * Created by Administrator on 2018/3/6.
 */

import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 实现类
 *
 * @author zhongshm
 * @create 2018-03-06 8:48
 **/
public class MessagePushCallbackHandlerImpl extends MessagePushCallbackHandler {
    private final Logger logger = LoggerFactory.getLogger("dailyRollingFile");

    @Autowired
    private IDataExchangeStatusService dataExchangeStatusService;

    @Override
    public void receiveErrorstartEvt(Exception e) {
        System.out.println(e);
    }

    @Override
    public void receiveResultstartEvt(MessagePushStub.StartEvtResponse result) {
        logger.info("=======================receiveResultstartEvt==========================");
        logger.info(new Date()+"sync:"+result.getOut());
        logger.info("=======================receiveResultstartEvt==========================");
    }

    @Override
    public void receiveErrorpushCloseEvt(Exception e) {
        super.receiveErrorpushCloseEvt(e);
    }

    @Override
    public void receiveResultcloseEvt(MessagePushStub.CloseEvtResponse result) {
        logger.info("=======================receiveResultcloseEvt==========================");
        logger.info(new Date()+"sync:"+result.getOut());
        logger.info("=======================receiveResultcloseEvt==========================");
    }

    @Override
    public void receiveErrorpushRejectEvt(Exception e) {
        super.receiveErrorpushRejectEvt(e);
    }

    @Override
    public void receiveResultrejectEvt(MessagePushStub.RejectEvtResponse result) {
        logger.info("=======================receiveResultrejectEvt==========================");
        logger.info(new Date()+"sync:"+result.getOut());
        logger.info("=======================receiveResultrejectEvt==========================");
    }
}
