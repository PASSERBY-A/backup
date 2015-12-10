package com.cmsz.cloudplatform.web.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.cmsz.cloudplatform.model.TEquipmentenroll;
import com.cmsz.cloudplatform.model.request.EquipmentEnrollRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.service.EquipmentEnrollManager;
import com.cmsz.cloudplatform.service.LogManager;
import com.cmsz.cloudplatform.utils.LogConstants;
import com.cmsz.cloudplatform.web.core.BaseAction;

public class EquipmentEnrollAction extends BaseAction {

	private static final long serialVersionUID = -187863783503789179L;

	@Autowired
	private EquipmentEnrollManager equipmentEnrollManager;

	@Autowired
	private LogManager logManager;

	public String list() {
		EquipmentEnrollRequest request = (EquipmentEnrollRequest) this.wrapRequest(new EquipmentEnrollRequest());
		ListResponse<TEquipmentenroll> response = equipmentEnrollManager.get(request);
		writeResponse(response);

		return NONE;
	}

	public String create() {
		EquipmentEnrollRequest request = (EquipmentEnrollRequest) this.wrapRequest(new EquipmentEnrollRequest());

		String content = "主机：" + request.getIp();
		try {
			ListResponse<TEquipmentenroll> result = equipmentEnrollManager.create(request);

			logManager.log(this.request.getRemoteHost(), LogConstants.EQUIPMENT_ENROLL, LogConstants.CREATE, request.getLoginId(), content,
					LogConstants.SUCCESS, "自动发现目标【注册】成功");
			writeResponse(result);
		} catch (Exception e) {
			log.error("自动发现目标【注册】发生异常：", e);
			logManager.log(this.request.getRemoteHost(), LogConstants.EQUIPMENT_ENROLL, LogConstants.CREATE, request.getLoginId(), content,
					LogConstants.FAIL, "自动发现目标【注册】发生异常");
		}

		return NONE;
	}

	public String update() {
		EquipmentEnrollRequest request = (EquipmentEnrollRequest) this.wrapRequest(new EquipmentEnrollRequest());

		String content = "主机：" + request.getIp();
		try {
			ListResponse<TEquipmentenroll> result = equipmentEnrollManager.update(request);

			logManager.log(this.request.getRemoteHost(), LogConstants.EQUIPMENT_ENROLL, LogConstants.MODIFY, request.getLoginId(), content,
					LogConstants.SUCCESS, "自动发现目标【更新】成功");
			writeResponse(result);
		} catch (Exception e) {
			log.error("自动发现目标【更新】发生异常：", e);
			logManager.log(this.request.getRemoteHost(), LogConstants.EQUIPMENT_ENROLL, LogConstants.MODIFY, request.getLoginId(), content,
					LogConstants.FAIL, "自动发现目标【更新】发生异常");
		}

		return NONE;
	}

	public String remove() {
		EquipmentEnrollRequest request = (EquipmentEnrollRequest) this.wrapRequest(new EquipmentEnrollRequest());

		String content = "主机：" + request.getIp();
		try {
			ListResponse<TEquipmentenroll> result = equipmentEnrollManager.remove(request);

			logManager.log(this.request.getRemoteHost(), LogConstants.EQUIPMENT_ENROLL, LogConstants.DELETE, request.getLoginId(), content,
					LogConstants.SUCCESS, "自动发现目标【删除】成功");
			writeResponse(result);
		} catch (Exception e) {
			log.error("自动发现目标【删除】发生异常：", e);
			logManager.log(this.request.getRemoteHost(), LogConstants.EQUIPMENT_ENROLL, LogConstants.DELETE, request.getLoginId(), content,
					LogConstants.FAIL, "自动发现目标【删除】发生异常");
		}
		return NONE;
	}
}
