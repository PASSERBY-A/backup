package com.cmsz.cloudplatform.web.action;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmsz.cloudplatform.model.request.ListWorkOrderReportRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.vo.WorkOrderReportVO;
import com.cmsz.cloudplatform.service.WorkOrderReportManager;
import com.cmsz.cloudplatform.web.core.BaseAction;
import com.hp.util.StringUtil;

public class WorkOrderReportAction extends BaseAction {

	private static final long serialVersionUID = 1827741518779785934L;
	@Autowired
	private WorkOrderReportManager workOrderReportManager;

	public String getWorkOrderReport() {
		ListWorkOrderReportRequest request = (ListWorkOrderReportRequest) this.wrapRequest(new ListWorkOrderReportRequest());
		ListResponse<WorkOrderReportVO> response = workOrderReportManager.getWorkOrderReportData(request);

		writeResponse(response);
		return NONE;
	}

	public String workOrderReportExcel() {
		ListWorkOrderReportRequest request = (ListWorkOrderReportRequest) this.wrapRequest(new ListWorkOrderReportRequest());
		List<WorkOrderReportVO> worList = workOrderReportManager.getWorkOrderReportData(request).getResponses();

		String fileNameHead = "工单统计";
		String fileName = request.getStartDate() + "至" + request.getEndDate();

		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.reset();
		resp.addHeader("Content-Disposition", "attachment; filename=" + StringUtil.urlEncode(fileNameHead) + ("(") + StringUtil.urlEncode(fileName)
				+ ").xls");
		// resp.setContentType("octets/stream; charset=UTF-8");
		resp.setContentType("application/vnd.ms-excel; charset=UTF-8");
		OutputStream os = null;
		WritableWorkbook wwb = null;
		try {
			os = resp.getOutputStream();

			String[] title = { "月分", "待审批", "审批通过待处理", "审批未通过", "审批通过正在处理", "处理成功", "处理失败", "工单总数" };

			// 创建Excel工作薄
			int row = 0;
			wwb = Workbook.createWorkbook(os);
			WritableSheet sheet = wwb.createSheet(request.getWorkOrderTypeName(), row);
			Label label;
			for (int i = 0; i < title.length; i++) {
				// Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
				// 在Label对象的子对象中指明单元格的位置和内容
				label = new Label(i, 0, title[i]);
				// 将定义好的单元格添加到工作表中
				sheet.addCell(label);
			}

			for (WorkOrderReportVO vo : worList) {
				row++;
				for (int i = 0; i < title.length; i++) {
					String val = "";
					if (vo.getObj()[i] != null && vo.getObj()[i] instanceof BigDecimal) {
						val = ((BigDecimal) vo.getObj()[i]).toString();
					} else if (vo.getObj()[i] != null && vo.getObj()[i] instanceof String) {
						val = (String) vo.getObj()[i];
					}
					label = new Label(i, row, val);
					sheet.addCell(label);
				}
			}

			// 写入数据
			wwb.write();

			os.flush();
		} catch (IOException e) {
			if (log.isTraceEnabled()) {
				log.trace("exception writing response: " + e);
			}
		} catch (Exception ex) {
			if (!(ex instanceof IllegalStateException)) {
				log.error("unknown exception writing api response", ex);
			} else {
				log.error("exception writing api response", ex);
			}
		}

		if (wwb != null) {
			try {
				// 关闭文件
				wwb.close();
			} catch (WriteException e) {
				if (log.isTraceEnabled()) {
					log.trace("exception close writableWorkbook outputStream: " + e);
				}
			} catch (IOException e) {
				if (log.isTraceEnabled()) {
					log.trace("exception close writableWorkbook outputStream: " + e);
				}
			}
		}

		if (os != null) {
			try {
				os.close();
				os = null;
			} catch (IOException e) {
				if (log.isTraceEnabled()) {
					log.trace("exception close response outputStream: " + e);
				}
			}
		}
		return NONE;
	}
}
