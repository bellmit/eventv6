package cn.ffcs.zhsq.event.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.event.service.IEventDisposalProviderService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.mybatis.domain.event.BusEventDisposalDTO;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.event.ITaskInfoService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;

@Service(value="eventDisposalProviderServiceImpl")
public class EventDisposalProviderServiceImpl implements IEventDisposalProviderService {

	@Autowired
	private IEventDisposalService eventDisposalService;
//	@Autowired
//	private FileUploadService fileUploadService;
	//@Autowired
	private ITaskInfoService taskInfoService;
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	
	@Autowired
    private IAttachmentService attachmentService;
	
//	private static final String GRID_CODE_MEILING = "350582002";
//	private static final String GRID_CODE_JINJIANG = "350582";
	
	private static final String GRID_CODE_MEILING = "350509002";
	private static final String GRID_CODE_JINJIANG = "350509";
	
	//-- 文件上传目录
	private static final String FILE_UPLOAD_FLODER = "eventPhoto";
	//-- 资源服务器域名
	public static String RESOURSE_DOMAIN_KEY = "zzgrid";
	@Override
	public String closeEvent(String jsonStr, byte[] picture) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String extractClosedEvent(String gridCode) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<BusEventDisposalDTO> extractEventByGridCode(String gridCode) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String extractTodoEvent(String gridCode) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String insertEvent(String jsonStr, byte[] picture) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String insertEventForChangLe(String jsonStr, byte[] picture) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String insertEventForGuLou(String jsonStr, byte[] picture) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String insertEventForMaWei(String jsonStr, byte[] picture) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String insertEvents(String jsonStr, byte[] picture) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String receiveFeedback(String jsonStr) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String receiveFeedbackEvent(String jsonStr) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
