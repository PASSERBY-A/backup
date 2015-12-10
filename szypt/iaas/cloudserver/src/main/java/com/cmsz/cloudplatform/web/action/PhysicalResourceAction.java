package com.cmsz.cloudplatform.web.action;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmsz.cloudplatform.model.TBayinfo;
import com.cmsz.cloudplatform.model.THba;
import com.cmsz.cloudplatform.model.THost;
import com.cmsz.cloudplatform.model.TRack;
import com.cmsz.cloudplatform.model.request.ListTHostRequest;
import com.cmsz.cloudplatform.model.request.ListTRackRequest;
import com.cmsz.cloudplatform.model.request.SaveTHostRequest;
import com.cmsz.cloudplatform.model.request.SaveTRackRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.response.Response;
import com.cmsz.cloudplatform.model.vo.HostPropertyVO;
import com.cmsz.cloudplatform.model.vo.RackPropertyVO;
import com.cmsz.cloudplatform.service.PhysicalResourceManager;
import com.cmsz.cloudplatform.web.core.BaseAction;
import com.hp.util.Page;

public class PhysicalResourceAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4315386051822953252L;
	@Autowired
	private PhysicalResourceManager physicalResourceManager;

	public String listHost() {

		ListTHostRequest request = (ListTHostRequest) this.wrapRequest(new ListTHostRequest());

		if (request != null && request.getId()!=null && request.getId()>0) {
			ListResponse<HostPropertyVO> response = physicalResourceManager.listHostProperty(request);
			int totalPage = 0;
			if (null != response.getResponses()) {
				totalPage = response.getCount();
			}

			Page page = new Page();
			page.setTotalCount(totalPage);
			page.setPageNo(request.getPage());
			page.setPageSize(request.getPagesize());
			JSONObject jsonPage = this.getJsonPage(page);

			List<HostPropertyVO> thosts = response.getResponses();
			jsonPage.put("hosts", thosts);

			this.renderText(jsonPage.toString());

		} else {
			ListResponse<THost> response = physicalResourceManager.listHost(request);

			int totalPage = 0;
			if (null != response.getResponses()) {
				totalPage = response.getCount();
			}

			Page page = new Page();
			page.setTotalCount(totalPage);
			page.setPageNo(request.getPage());
			page.setPageSize(request.getPagesize());
			JSONObject jsonPage = this.getJsonPage(page);

			List<THost> thosts = response.getResponses();
			for (THost t : thosts) {
				for (THba h : t.getHbaList()) {
					h.setFkHost(null);
				}
			}
			jsonPage.put("hosts", thosts);

			this.renderText(jsonPage.toString());
		}

		return NONE;
	}

	public String listRack() {

		ListTRackRequest request = (ListTRackRequest) this.wrapRequest(new ListTRackRequest());

		if (request != null && request.getId() != null && request.getId()!=0) {
			ListResponse<RackPropertyVO> response = physicalResourceManager.listRackProperty(request);
			int totalPage = 0;
			if (null != response.getResponses()) {
				totalPage = response.getCount();
			}

			Page page = new Page();
			page.setTotalCount(totalPage);
			page.setPageNo(request.getPage());
			page.setPageSize(request.getPagesize());
			JSONObject jsonPage = this.getJsonPage(page);

			List<RackPropertyVO> tracks = response.getResponses();
			jsonPage.put("racks", tracks);

			this.renderText(jsonPage.toString());

		} else {
			ListResponse<TRack> response = physicalResourceManager.listRack(request);

			int totalPage = 0;
			if (null != response.getResponses()) {
				totalPage = response.getCount();
			}

			Page page = new Page();
			page.setTotalCount(totalPage);
			page.setPageNo(request.getPage());
			page.setPageSize(request.getPagesize());
			JSONObject jsonPage = this.getJsonPage(page);
			List<TRack> tracks = response.getResponses();
			for (TRack t : tracks) {
				for (TBayinfo h : t.getBayinfos()) {
					h.setFkRack(null);
				}
			}
			jsonPage.put("racks", tracks);
			this.renderText(jsonPage.toString());
		}
		return NONE;
	}

	public String updateHost() {

		Long hostId = requestParams.get("hostid") == null ? -1 : Long.parseLong(requestParams.get("hostid")[0].toString());
		String resourcepoolid = requestParams.get("resourcepoolid") == null ? null : (String) requestParams.get("resourcepoolid")[0];
		physicalResourceManager.updateHostResourcePool(hostId, resourcepoolid);
		String responseType = requestParams.get("response") == null ? "json" : (String) requestParams.get("response")[0];
		Response response = this.transformResponse("success", responseType, HttpStatus.SC_OK);
		this.writeResponse(response);
		return NONE;
	}

	public String archiveHost() {
		try {
			SaveTHostRequest saveTHostRequest = (SaveTHostRequest) this.wrapRequest(new SaveTHostRequest());
			physicalResourceManager.archivedHost(saveTHostRequest);
			Response response = this.transformResponse("success", "json", HttpStatus.SC_OK);
			this.writeResponse(response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return NONE;
	}

	public String archiveRack() {
		try {
			SaveTRackRequest saveTHostRequest = (SaveTRackRequest) this.wrapRequest(new SaveTRackRequest());
			physicalResourceManager.archivedRack(saveTHostRequest);
			Response response = this.transformResponse("success", "json", HttpStatus.SC_OK);
			this.writeResponse(response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return NONE;
	}

	public String removeHost() {
		try {
			SaveTHostRequest saveTHostRequest = (SaveTHostRequest) this.wrapRequest(new SaveTHostRequest());
			physicalResourceManager.removeHost(saveTHostRequest);
			Response response = this.transformResponse("success", "json", HttpStatus.SC_OK);
			this.writeResponse(response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return NONE;
	}

	public String deleteRack() {
		try {
			String trackId = request.getParameter("id");
			physicalResourceManager.removeOA(Long.parseLong(trackId));
			Response response = this.transformResponse("success", "json", HttpStatus.SC_OK);
			this.writeResponse(response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return NONE;
	}

	public String saveRack() {
		SaveTRackRequest saveTRackRequest = (SaveTRackRequest) this.wrapRequest(new SaveTRackRequest());
		TRack track = new TRack();
		BeanUtils.copyProperties(saveTRackRequest, track);
		track.setCreatedBy(this.activeUser.getLoginId());
		track.setCreatedOn(new Date());
		track.setModifiedBy(this.activeUser.getLoginId());
		track.setModifiedOn(new Date());
		track.setSaveStatus(TRack.SAVE_STATUS_DEAL);
		track.setStatus(TRack.STATUS_ARCHIVE);
		physicalResourceManager.saveRack(track);
		Response response = this.transformResponse("success", "json", HttpStatus.SC_OK);
		this.writeResponse(response);
		return NONE;
	}
}
