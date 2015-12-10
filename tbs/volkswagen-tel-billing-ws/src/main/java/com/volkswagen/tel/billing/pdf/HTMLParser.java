package com.volkswagen.tel.billing.pdf;

import com.volkswagen.tel.billing.billcall.rest.UserInfoService;
import com.volkswagen.tel.billing.common.util.CommonUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

@Component
public class HTMLParser extends HttpServlet{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private UserInfoService userInfoService;

	/**
	 * generate html according to call record data and user info
	 * @param obj
	 * @param telephoneNumber
	 * @param selectedYear
	 * @param selectedMonth
	 * @param monthPkg
     *@param dataBoPkg
     * @param smsBoPkg
     * @param roamingBoPkg
     * @param request  @return
	 * @throws IOException
	 */
	public String getHTMLCode(JSONObject obj, String telephoneNumber, int selectedYear, int selectedMonth, String monthPkg, String dataBoPkg, String smsBoPkg, String roamingBoPkg, HttpServletRequest request) throws IOException {
		ServletContext context = request.getSession().getServletContext();
		
		Document doc;
		doc = Jsoup.parse(new File(context.getRealPath("/tmp/")+"/pdfTemplate.htm"),"UTF-8", "lcoalhost");
		
		JSONObject userInfo = userInfoService.getUserInfoBySessionUid();
		
		String firstName = "";
		String lastName = "";
		String staffCode = "";
		
		if(userInfo.getString("returnCode").equalsIgnoreCase("SUCCESS")){
			
			firstName = userInfo.getString("firstName");
			lastName  = userInfo.getString("lastName");
			staffCode = userInfo.getString("staffCode");
		}
		
		doc.getElementById("firstName").children().first().text(firstName);
		doc.getElementById("lastName").children().first().text(lastName);
		doc.getElementById("staffCode").children().first().text(staffCode);
				
				
		doc.getElementById("telNumberSel").text(telephoneNumber);
		doc.getElementById("yearSel").text(String.valueOf(selectedYear));
		doc.getElementById("monthSel").text(String.valueOf(selectedMonth));

        doc.getElementById("monthPkg").text(monthPkg);
        doc.getElementById("dataBoPkg").text(dataBoPkg);
        doc.getElementById("smsBoPkg").text(smsBoPkg);
        doc.getElementById("roamingBoPkg").text(roamingBoPkg);

		String pageHtml = "";

		Elements trOdd = doc.getElementsByAttributeValue("class", "gradeA odd");

		Elements trEven = doc.getElementsByAttributeValue("class",
				"gradeA even");

		JSONArray array = null;
		if (obj.get("returnCode").equals("SUCCESS")) {
			array = (JSONArray) obj.get("resultList");
		}

		StringBuilder tbodyhtmlBuilder = new StringBuilder("<tbody>");
		if (array != null && !array.isEmpty()) {
			for (int i = 0; i < array.size(); i++) {
				String duration = "";
				String calledNumber = "";
				String type = "";
				String location = "";
				String cost = "";
				String isPrivate = "";

				JSONObject object = (JSONObject) array.get(i);
				String dateOfCall = object.get("dateOfCall").toString();
				JSONObject json = JSONObject.fromObject(dateOfCall);

				int year = json.getInt("year");
				int month = json.getInt("month");
				int date = json.getInt("date");

				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, year);
				cal.set(Calendar.MONTH, month);
				cal.set(Calendar.DATE, date);

				String startingTime = object.get("startingTime").toString();

				JSONObject jsonstartTime = JSONObject.fromObject(startingTime);

				Calendar cal2 = CommonUtil.getCalendar(
						jsonstartTime.getInt("year"),
						jsonstartTime.getInt("month"),
						jsonstartTime.getInt("date"),
						jsonstartTime.getInt("hours"),
						jsonstartTime.getInt("minutes"),
						jsonstartTime.getInt("seconds"));

				String start = CommonUtil
						.formatDate(cal2.getTime(), "HH:mm:ss");
				String callDate = CommonUtil.formatDate(cal.getTime(),
						"dd/MM/yyyy");

				duration = object.get("duration").toString();
				calledNumber = object.get("calledNumber").toString();
				type = object.get("communicationType").toString();
				location = object.get("location").toString();
				cost = object.get("cost").toString();
				isPrivate = object.get("privatePurpose").toString();

				Element elementTr = null;
				elementTr = trOdd.first();
				if ((i + 1) % 2 == 0) {
					elementTr = trOdd.first();
				} else {
					elementTr = trEven.first();
				}				

				Elements tds = elementTr.children();

				for (int m = 0; m < tds.size(); m++) {
					Element td = tds.get(m);
					if (m == 0) {
						td.text(callDate);
					} else if (m == 1) {
						td.text(start);
					} else if (m == 2) {
						td.text(duration);
					} else if (m == 3) {
						td.text(calledNumber);
					} else if (m == 4) {
						td.text(type);
					} else if (m == 5) {
						td.text(location);
					} else if (m == 6) {
						td.text(cost);
					} else if (m == 7) {
						String checked = "false";
						td.children().first().attr("checked", "");
						if (Integer.valueOf(isPrivate) == 1) {
							checked = "true";
							td.children().first().attr("checked", checked);
						}
					}
				}
				tbodyhtmlBuilder.append("<tr class= 'gradeA odd'>")
						.append(elementTr.html()).append("</tr>");
			}
		} else {
			tbodyhtmlBuilder
					.append("<tr class='gradeA odd'><td colspan='8'>Sorry, no matching data is found.</td></tr>");
		}
		tbodyhtmlBuilder.append("</tbody>");

		doc.getElementsByTag("tbody").first().remove();
		doc.getElementsByTag("table").first().children().after(tbodyhtmlBuilder.toString());

		pageHtml = doc.html();
		
		return pageHtml;
	}
}