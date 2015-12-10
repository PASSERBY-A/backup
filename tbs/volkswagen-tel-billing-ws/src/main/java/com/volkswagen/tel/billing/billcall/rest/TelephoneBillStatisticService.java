package com.volkswagen.tel.billing.billcall.rest;

import com.volkswagen.tel.billing.billcall.biz.BillCallRecordStatisticBizService;
import com.volkswagen.tel.billing.billcall.biz.util.TbsSessionUtil;
import com.volkswagen.tel.billing.billcall.rest.util.TelephoneChecker;
import com.volkswagen.tel.billing.pdf.PDFGenerator;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;


/**
 * This class is used to provide a restful service to retrieve the telephone
 * call record statistic
 * 
 * @author ELZHNTA
 * 
 */
@Service("telephoneBillStatisticService")
@Path("/telephoneBillStatisticService")
public class TelephoneBillStatisticService extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory
			.getLogger(TelephoneBillStatisticService.class);

	@Autowired
	BillCallRecordStatisticBizService billCallRecordStatisticBizService;

	@Context
	HttpServletRequest request;

	@Autowired
	TelephoneChecker telephoneChecker;

	@Autowired
	PDFGenerator pdfGenerator;

	/**
	 * retrieve the telephone call record statistic
	 * 
	 * @param telephoneNumber
	 * @param year
	 * @param month
	 * @return
	 */
	@POST
	@Path("getCommunicationTypeStatistic")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCommunicationTypeStatistic(
			@FormParam("telephoneNumber") String telephoneNumber,
			@FormParam("year") int year, @FormParam("month") int month) {
		log.info("---------- getCommunicationTypeStatistic strart.");

		// - valid date period can be calculated from table user_telephone
		log.info(">>> telephoneNumber=" + telephoneNumber + ", year=" + year
				+ ", month=" + month);

		JSONObject jObj = new JSONObject();
		String userId = TbsSessionUtil.getUidFromSession(request);

		if (telephoneChecker.isValidTelephone(userId, telephoneNumber)) {
			jObj = billCallRecordStatisticBizService
					.getCommunicationTypeStatisticByCallingNumerYearMonth(
							userId, telephoneNumber, year, month);

		} else {
			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage",
					"Invalid user session. The telephone number is not owned by you.");
		}

		log.info("---------- getCommunicationTypeStatistic end.");
		return jObj.toString();
	}

	@POST
	@Path("exportCallListAsPDF")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject exportCallListAsPDF(
			@FormParam("telephoneNumber") String telephoneNumber,
			@FormParam("year") int year, @FormParam("month") int month,
            @FormParam("monthPkg")String monthPkg, @FormParam("dataBoPkg")String dataBoPkg,
            @FormParam("smsBoPkg")String smsBoPkg, @FormParam("roamingBoPkg")String roamingBoPkg)
            throws IOException, ParserConfigurationException, SAXException,
			com.lowagie.text.DocumentException, URISyntaxException {

		JSONObject jObj = new JSONObject();

		String userId = TbsSessionUtil.getUidFromSession(request);

		if (telephoneChecker.isValidTelephone(userId, telephoneNumber)) {
			JSONObject recordList = billCallRecordStatisticBizService
					.getSavedCallRecordList(userId, telephoneNumber, year,
							month);

			String path = pdfGenerator.htmlCodeToPdf(recordList,
					telephoneNumber, year, month, monthPkg, dataBoPkg, smsBoPkg, roamingBoPkg, request);

			jObj.put("pdfFile", path);
			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "SUCCESS");
		} else {
			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage",
					"Invalid user session. The telephone number is not owned by you.");
		}

		log.info("---------- export pdf end.");

		return jObj;
	}
}