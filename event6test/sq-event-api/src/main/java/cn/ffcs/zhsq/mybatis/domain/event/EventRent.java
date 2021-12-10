package cn.ffcs.zhsq.mybatis.domain.event;

import java.io.Serializable;
import java.util.Date;

public class EventRent implements Serializable{
 
	private static final long serialVersionUID = -8108975358507251741L;

	private Long erId;//主键  ER_ID
	
	private Long eventId;//事件ID  EVENT_ID
	
	private String lessor;//出租人   LESSOR
	
	private String lessee;//承租人  LESSEE
	
	private Date rentStart;//租赁开始时间   RENT_START
	
	private String rentStartStr;//租赁开始时间文本
	
	private Date rentEnd;//租赁结束时间    RENT_END
	
	private String rentEndStr;//租赁结束时间文本  
	
	private Long rentNum;//租住人数  RENT_NUM
	
	private String rentalStaff;//租住人名单  RENTAL_STAFF
	
	private String remarks;//备注  REMARKS

	public Long getErId() {
		return erId;
	}

	public void setErId(Long erId) {
		this.erId = erId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getLessor() {
		return lessor;
	}

	public void setLessor(String lessor) {
		this.lessor = lessor;
	}

	public String getLessee() {
		return lessee;
	}

	public void setLessee(String lessee) {
		this.lessee = lessee;
	}

	public Date getRentStart() {
		return rentStart;
	}

	public void setRentStart(Date rentStart) {
		this.rentStart = rentStart;
	}

	public String getRentStartStr() {
		return rentStartStr;
	}

	public void setRentStartStr(String rentStartStr) {
		this.rentStartStr = rentStartStr;
	}

	public Date getRentEnd() {
		return rentEnd;
	}

	public void setRentEnd(Date rentEnd) {
		this.rentEnd = rentEnd;
	}

	public String getRentEndStr() {
		return rentEndStr;
	}

	public void setRentEndStr(String rentEndStr) {
		this.rentEndStr = rentEndStr;
	}

	public Long getRentNum() {
		return rentNum;
	}

	public void setRentNum(Long rentNum) {
		this.rentNum = rentNum;
	}

	public String getRentalStaff() {
		return rentalStaff;
	}

	public void setRentalStaff(String rentalStaff) {
		this.rentalStaff = rentalStaff;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
