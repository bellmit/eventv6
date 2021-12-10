package cn.ffcs.zhsq.event.service;

import java.util.List;

import cn.ffcs.zhsq.mybatis.domain.event.BusEventDisposalDTO;

/**
 * 事件Servcie
 * @author youzh
 *
 */
public interface IEventDisposalProviderService {

	
	/**
	 * 梅林调用接口插入他们那边的事件
	 * @return
	 */
	public String insertEvent(String jsonStr, byte[] picture);
	
	/**
	 * 梅林拉取我们这边的结案数据
	 * @return
	 */
	public String extractClosedEvent(String gridCode);
	
	/**
	 * 梅林拉取我们这边的待办事件
	 * @return
	 */
	public String extractTodoEvent(String gridCode);
	
	
	/**
	 * 针对第三方平台接收完结案数据后要调用此接口，反馈回成功接收的事件ID，以便下次再次拉取网格平台结案数据时不会造成重复拉取。
	 * @return
	 */
	public String receiveFeedback(String jsonStr);
	
	
	/**
	 * 梅林那边将事件结案后更换新我们这边的事件状态
	 * @return
	 */
	public String closeEvent(String jsonStr, byte[] picture); 

	
	/**
	 * 鼓楼调用接口插入第三方事件
	 * @return
	 */
	public String insertEventForGuLou(String jsonStr, byte[] picture);

	
	/**
	 * 马尾调用接口插入第三方事件
	 * @return
	 */
	public String insertEventForMaWei(String jsonStr, byte[] picture);

	public String insertEventForChangLe(String jsonStr, byte[] picture);

	public String insertEvents(String jsonStr, byte[] picture);

	/**
	 * @Title: extractEventByGridCode 
	 * @Description: TODO(事件接口) 
	 * @param gridCode
	 * @return 设定文件 
	 * @return String 返回类型 
	 * @throws 
	 * @author zhongshm
	 */
	public List<BusEventDisposalDTO> extractEventByGridCode(String gridCode);

	public String receiveFeedbackEvent(String jsonStr);
}
