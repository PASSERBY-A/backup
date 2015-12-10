package com.cmsz.cloudplatform.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.hp.core.model.BaseEntity;

@Entity(name = "T_EQUIPMENTEVENT")
public class TEquipmentevent extends BaseEntity {
	private static final long serialVersionUID = 7023997959022476827L;

	// 1.增加物理机
	// 2.移除物理机
	// 3.变化位置
	// 4物理机状态变化
	public static final String EVENT_TYPE_CREATE = "1";

	public static final String EVENT_TYPE_DELETE = "2";

	public static final String EVENT_TYPE_CHANGE_LOC = "3";

	public static final String EVENT_TYPE_CHANGE_STATUS = "4";

	// 事件状态：
	// 1-----待处理；
	// 2-----己处理；
	// 3-----关闭；
	public static final Integer EVENT_STATUS_PENDING = 1;

	public static final Integer EVENT_STATUS_FINISH = 2;

	public static final Integer EVENT_STATUS_CLOSE = 3;

	@SequenceGenerator(sequenceName = "t_equipmentevent_id_seq", name = "t_equipmentevent_id_seq_gen")
	@Id
	@GeneratedValue(generator = "t_equipmentevent_id_seq_gen", strategy = GenerationType.AUTO)
	@Column(name = "PK_EQUIEVENT_ID")
	private Long id;

	@Column(name = "SERIALNUMBER")
	private String serialnumber;

	@Column(name = "EVENTTYPE")
	private String eventtype;

	@Column(name = "EVENTSTATUS")
	private Integer eventstatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	public String getEventtype() {
		return eventtype;
	}

	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}

	public Integer getEventstatus() {
		return eventstatus;
	}

	public void setEventstatus(Integer eventstatus) {
		this.eventstatus = eventstatus;
	}
	
	
	@Column(name = "DESCRIPT")
	private String descript;

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}
	
	

}