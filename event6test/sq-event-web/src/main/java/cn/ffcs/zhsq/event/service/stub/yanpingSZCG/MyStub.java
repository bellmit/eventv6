package cn.ffcs.zhsq.event.service.stub.yanpingSZCG;/**
 * Created by Administrator on 2018/2/8.
 */

/**
 * @author zhongshm
 * @create 2018-02-08 21:09
 **/
public class MyStub extends IComplexUserServiceServiceCallbackHandler {

    @Override
    public void receiveResultgetPublicDisOutTotal(IComplexUserServiceServiceStub.GetPublicDisOutTotalResponse result) {

        System.out.println(result.local_return+"result---"+result.get_return());
        super.receiveResultgetPublicDisOutTotal(result);
    }


}
