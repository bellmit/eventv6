package cn.ffcs.zhsq.event.service;


/**
 * @Description:实时更新ES库
 * @Author: ztc
 * @Date: 2018/9/10 14:46
 */
public interface IEventDisposalEsService<T>{

    //管理列表获取分页信息方法--管理列表还未做
   //Pagination getEventDisposalPage();

    /**
     * 找到更新到数据库后的对象，新增到es
     * @param eventId
     * @return
     */
    boolean findAndSave(Long eventId);

    /**
     * 找到更新到数据库后的对象，同步到es
     * @param eventId
     * @return
     */
    boolean findAndUpdate(Long eventId);

}
