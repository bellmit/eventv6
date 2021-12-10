package cn.ffcs.zhsq.event.service.stub.bug;/**
 * Created by Administrator on 2017/8/2.
 */

import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusTwoWayService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhongshm
 * @create 2017-08-02 8:56
 * 测试事件对接
 **/
public class QuartzJob implements Job {

    private final Logger logger = LoggerFactory.getLogger("dailyRollingFile");

    private IFunConfigurationService funConfigurationService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("===================================================");
        logger.info("测试事件对接");
        logger.info("===================================================");
        try {
            SchedulerContext schCtx = context.getScheduler().getContext();
            ApplicationContext appCtx = (ApplicationContext)schCtx.get("applicationContext");
            this.funConfigurationService = (IFunConfigurationService) appCtx.getBean("funConfigurationService");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        String run = funConfigurationService.changeCodeToValue("TONGAN_EVENT","RUN", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        System.out.println(run);
    }

}
