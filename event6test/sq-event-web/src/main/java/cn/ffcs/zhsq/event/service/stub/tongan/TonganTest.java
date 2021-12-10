package cn.ffcs.zhsq.event.service.stub.tongan;/**
 * Created by Administrator on 2017/9/1.
 */

import cn.ffcs.shequ.wap.util.DateUtils;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * 同安对接单元测试
 *
 * @author zhongshm
 * @create 2017-09-01 9:13
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:applicationContext-*.xml")
@TransactionConfiguration(defaultRollback = false)
public class TonganTest {

    @Autowired
    private IEventDisposalWorkflowService eventDisposalWorkflowService;


    @Test
    public void testTask(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("eventId","9142");
        params.put("transactOrgName","transactOrgName");
//        String remark = jsonTaskRows.get("remark").toString();s
        params.put("results","results");
//        String createUserName = jsonTaskRows.get("userName").toString();
        params.put("createUserName","createUserName");
        params.put("optType","1");
//        String oppoSideBizCode = jsonTaskRows.get("id").toString();
        params.put("oppoSideBizCode","111");
        params.put("oppoSideBizType","0");
//        String logTypeName = jsonTaskRows.get("logTypeName").toString();
        params.put("taskName","taskName");
        params.put("createTime", DateUtils.getNow());
        params.put("srcPlatform","040");

        Long record = eventDisposalWorkflowService.saveOrUpdateTask(params);
        System.out.println(record);
    }
}
