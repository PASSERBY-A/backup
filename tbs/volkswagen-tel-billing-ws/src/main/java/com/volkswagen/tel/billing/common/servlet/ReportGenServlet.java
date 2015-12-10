package com.volkswagen.tel.billing.common.servlet;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.ServiceException;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.PDFEncryption;

import com.ibm.itim.ws.exceptions.WSInvalidLoginException;
import com.ibm.itim.ws.exceptions.WSLoginServiceException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;
import com.volkswagen.tel.billing.billcall.biz.ReportBizService;
import com.volkswagen.tel.billing.billcall.biz.ReportLocalBizService;
import com.volkswagen.tel.billing.billcall.jpa.domain.CostCenterEmployeeReportEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.CostCenterReportEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.Report;
import com.volkswagen.tel.billing.billcall.jpa.domain.TelephoneBillEntity;
import com.volkswagen.tel.billing.billcall.jpa.service.CostCenterEmployeeReportDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.CostCenterReportDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.TelephoneBillDaoService;
import com.volkswagen.tel.billing.common.GenerateAllCostCenterReport;
import com.volkswagen.tel.billing.common.TBSPersonInfo;
import com.volkswagen.tel.billing.ldap.TIMService;

public class ReportGenServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HttpServletRequest req;
	private HttpServletResponse resp;
	private WebApplicationContext context;
	private TelephoneBillDaoService telephoneBillDaoService;
	private TIMService t;
	private ReportBizService reportBizService;
	private ReportLocalBizService reportLocalBizService;
	
	private CostCenterReportDaoService costCenterReportDaoService;
	private CostCenterEmployeeReportDaoService costCenterEmployeeReportDaoService;
	
	private String[] months = new String[] {"","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	private GenerateAllCostCenterReport g;
	
	private Log log = LogFactory.getLog(ReportGenServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.req = req;
		this.resp =resp;
		String type = req.getParameter("type");
		if ("report1".equals(type)) {
			try {
				report1();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else if("report2".equals(type))
		{
			
			try {
				report2();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}else if("report3".equals(type))
		{
			
			try {
				report3();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		else if("report4".equals(type))
		{
			
			try {
				report4();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		

	}

	private void report4() throws IOException {
		
		byte[] b = null;
		
		OutputStream out = resp.getOutputStream();
		
		String year = req.getParameter("year");
		
		String month=req.getParameter("month");
		
		String roleName=req.getParameter("roleName");
		
		
		
		
		
		
		if(StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month))
		{
			
			if(roleName.contains("all"))
			{
			
				String tmpPath = FileUtils.getTempDirectoryPath();
				
				String filePath = tmpPath+File.separator+UUID.randomUUID().toString()+".zip";
				try {
					//String filePath = "d://"+UUID.randomUUID().toString()+".zip";
					

					
					OutputStream f = new FileOutputStream(filePath);
					
					//OutputStream f = new FileOutputStream(filePath+File.separator+UUID.randomUUID().toString()+".zip");
					
					 f = g.generateZip(f, Integer.parseInt(year),Integer.parseInt(month));
					 
					 
					IOUtils.closeQuietly(f);
					
					
					 
					 InputStream in = new FileInputStream(filePath);					 
					byte [] b1 =  IOUtils.toByteArray(in);
					 
					 //String filenamedisplay = URLEncoder.encode("report4_"+UUID.randomUUID().toString()+".zip", "UTF-8");
					resp.addHeader("Pragma", "public");
					resp.addHeader("Cache-Control", "max-age=0");
					//resp.setContentType("application/x-download");//
					//resp.reset();
					resp.addHeader("Content-Type", "application/octet-stream");
					resp.addHeader("Content-Disposition","attachment; filename=\" "+year+month+".zip\"");
					resp.addHeader("Content-Transfer-Encoding","binary");
					IOUtils.write(b1, out);
   					 

				} catch (Exception e) {
					log.error(e.fillInStackTrace());
				}finally{
					FileUtils.deleteQuietly(new File(filePath)); 
					out.flush(); 
					out.close();
				}
				
			}
			else{
				
				String costCenterType = roleName.substring(0,roleName.indexOf("-"));
				
				HSSFWorkbook wb = g.generate(costCenterType,Integer.parseInt(year),Integer.parseInt(month));
				
				try{
					
					resp.setContentType("application/x-download");//

					String filenamedisplay = URLEncoder.encode("report4_"+UUID.randomUUID().toString()+".xls", "UTF-8");

					resp.addHeader("Content-Disposition", "attachment;filename="+ filenamedisplay);

					
				}finally{
					wb.write(out);
					out.flush();
					out.close();
					
				}
				
				
				
				
			}
			
			
			
			
			
		}
		
		
		
		
		
		
	}

	private void report3() throws IOException {
		
		byte[] b = null;
		
		OutputStream out = resp.getOutputStream();
		
		String year = req.getParameter("year");
		
		String month=req.getParameter("month");
		
		String costcenter  = req.getParameter("costcenter");
		
		System.out.println(year);
		
		System.out.println(month);
		
		System.out.println(costcenter);
		 List<CostCenterEmployeeReportEntity> user = Collections.EMPTY_LIST;
		
		if(StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month) && StringUtils.isNotBlank(costcenter))
		{
			
			try {
				user = costCenterEmployeeReportDaoService.findByCostCenter(costcenter.trim(), Integer.parseInt(year), Integer.parseInt(month));
			
			//JSONArray jsonArray = reportBizService.costCenterToJsonArray(user, Integer.parseInt(year), Integer.parseInt(month),true);
			
			JSONArray jsonArray = reportLocalBizService.allemployeeCostCenterToJsonArray(user,Integer.parseInt(year), Integer.parseInt(month),true);
			
			List<Map<String,String>> result = new LinkedList<Map<String,String>>();
			
			Iterator it = jsonArray.iterator();
			
			while(it.hasNext())
			{
				//"Staff Code", "Name", "Fix Cost", "Cell Cost"
				Map<String,String> map = new LinkedHashMap<String, String>();
				JSONArray s= (JSONArray) it.next();
				map.put("staffCode", s.get(0) instanceof JSONNull?"  ":(String)s.get(0));
				map.put("name",  s.get(1) instanceof JSONNull?"  ":(String)s.get(1));
				map.put("fixCost", s.get(2) instanceof JSONNull?"  ":(String)s.get(2));
				map.put("cellCost",   s.get(3) instanceof JSONNull?"  ":(String)s.get(3));
				result.add(map);
			}
			
			Map<String,Double> m = reportBizService.computeCostCenterTelephoneAndMobileTotalByMonth(costcenter, Integer.parseInt(year), Integer.parseInt(month));
			
			String html = fillReport3Template("report3.htm",costcenter,year+"/"+month,String.valueOf(m.get("fixTotal")),String.valueOf(m.get("cellTotal")), result);
			
			b = genPdf(html);
			
			resp.setContentType("application/x-download");//

			String filenamedisplay = URLEncoder.encode("report3_"+UUID.randomUUID().toString()+".pdf", "UTF-8");

			resp.addHeader("Content-Disposition", "attachment;filename="+ filenamedisplay);

			out.write(b);
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				
				out.flush();
				out.close();
				
			}
			
			
			
		}
		
		
		
		
		
		
		
		
		
		
	}

	private void report2() throws IOException, DocumentException {
		
		

		String year = req.getParameter("year");
		
		String month=req.getParameter("month");
		
		String vendorName=req.getParameter("sel");
		
		byte[] b = null;
		
		OutputStream out = null; 
		
		
		if(StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month) && StringUtils.isNotBlank(vendorName))
		{
			try{
				out = resp.getOutputStream();
				
				int y = Integer.parseInt(year);
				
				int m = Integer.parseInt(month);
				
				List<Map<String,String>> result = new LinkedList<Map<String,String>>();
	 			
				Long count = telephoneBillDaoService.countOpenBillsByType(vendorName,y,m)==0?1:telephoneBillDaoService.countOpenBillsByType(vendorName,y,m);
				
				//telephoneBillDaoService.findAllOpenBills(type, year, month, 0, size, direction, properties)
				
				JSONObject jsonobj =  reportBizService.findAllOpenBills(vendorName, y, m,1,count.intValue());
				
				JSONArray array  = (JSONArray) jsonobj.get("resultList") ;
				
				Iterator it = array.iterator();
				while(it.hasNext())
				{
					Map<String,String> map = new LinkedHashMap<String, String>();
					JSONArray s= (JSONArray) it.next();
					map.put("EmployeeNumber", s.get(0) instanceof JSONNull?"  ":(String)s.get(0));
					map.put("Name",  s.get(1) instanceof JSONNull?"  ":(String)s.get(1));
					map.put("PhoneNumber", s.get(2) instanceof JSONNull?"  ":(String)s.get(2));
					map.put("Email",   s.get(3) instanceof JSONNull?"  ":(String)s.get(3));
					map.put("VendorName",s.get(4) instanceof JSONNull?"  ":(String)s.get(4));
					result.add(map);
				}
				
				String html = fillReport2Template("report2.htm",year,months[Integer.parseInt(month)],vendorName.contains("Mobile")?"mobile":"telephone", result);
				
				b = genPdf(html);
				
				resp.setContentType("application/x-download");//

				String filenamedisplay = URLEncoder.encode("report1_"+UUID.randomUUID().toString()+".pdf", "UTF-8");

				resp.addHeader("Content-Disposition", "attachment;filename="+ filenamedisplay);

				out.write(b);
				
				
			}
			
			finally{
				
				out.flush();
				out.close();
			}  
			
		}
	}

	private void report1() throws IOException, DocumentException {
		
		String userId = req.getParameter("userId");
		
		String firstName=req.getParameter("firstName");
		
		String lastName=req.getParameter("lastName");
		
		String staffCode=req.getParameter("staffCode");
		
		if(""==userId)
		{
			
			return;
		}
		
		byte[] b = null;
		
		OutputStream out = null;
		
		try {
			out = resp.getOutputStream();
			List<Report> reports = new LinkedList<Report>();
			List<String> cloumns = new LinkedList<String>();
			cloumns.add("Called Number");
			cloumns.add("Date");

			Map<String,List<TelephoneBillEntity>> map = telephoneBillDaoService.findNoSendBillByUser(userId.toUpperCase());
			
			Collection<List<TelephoneBillEntity>> list =  map.values();
			
			//Iterator<List<TelephoneBillEntity>> it = list.iterator();
			
			for(List<TelephoneBillEntity> entity:list)
			{
				
				for(TelephoneBillEntity te:entity)
				{
					
					Report report = new Report();
					
					report.setPhoneNumber(te.getTelephoneNumber());
					
					report.setDate(te.getYear()+"-"+te.getMonth());
					
					reports.add(report);
					
				}
				
				
				
				
			}/**/
			
			String html = fillReport1Template("report1.htm", firstName,lastName,staffCode, reports);

			b = genPdf(html);

			resp.setContentType("application/x-download");//

			String filenamedisplay = URLEncoder.encode("report1_"+userId+".pdf", "UTF-8");

			resp.addHeader("Content-Disposition", "attachment;filename="+ filenamedisplay);

			out.write(b);

			

		}finally{
			
			out.flush();
			out.close();
		}  
		
		
		
		
	}
	 
	
	
	
	
	
	

	private byte[] genPdf(String content) throws IOException, DocumentException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		byte[] result = null;

		ITextRenderer renderer = new ITextRenderer();

		renderer.setDocumentFromString(content);

		renderer.layout();

		PDFEncryption pdfEncryption = new PDFEncryption();

		pdfEncryption.setEncryptionType(PdfWriter.ALLOW_PRINTING);

		pdfEncryption.setEncryptionType(PdfWriter.ALLOW_COPY);

		renderer.setPDFEncryption(pdfEncryption);

		renderer.createPDF(out, true);

		renderer.finishPDF();

		result = out.toByteArray();

		out.close();

		return result;

	}

	private synchronized String fillTemplate(String templateFile,VelocityContext context) throws IOException {

		Properties prop = new Properties();

		prop.put("file.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		Velocity.init(prop);



		Template template = null;

		try {
			template = Velocity.getTemplate(templateFile, "UTF-8");
		} catch (ResourceNotFoundException rnfe) {
			System.out.println("Example : error : cannot find template "
					+ templateFile);
		} catch (ParseErrorException pee) {
			System.out.println("Example : Syntax error in template "
					+ templateFile + ":" + pee);
		}

		/*
		 * Now have the template engine process your template using the data
		 * placed into the context. Think of it as a 'merge' of the template and
		 * the data to produce the output stream.
		 */

		ByteArrayOutputStream byeout = new ByteArrayOutputStream();

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				byeout));

		if (template != null)
			template.merge(context, writer);

		/*
		 * flush and cleanup
		 */

		writer.flush();

		writer.close();

		String result = byeout.toString();
		System.out.println(result);
		return result;

	}
	
	
	public String fillReport1Template(String templateFile, String firstName,String lastName,String staffCode, List<Report> report) throws IOException
	{
		
		VelocityContext context = new VelocityContext();

		// context.put("logopath",
		// this.getServletContext().getRealPath("/images/")+File.separator+"logo_01.jpg");

		context.put("logopath", "http://" + this.req.getLocalAddr() + ":" + this.req.getLocalPort() + this.req.getContextPath() + "/images/logo_01.jpg");

		context.put("firstName", firstName);
		
		context.put("lastName", lastName);
		
		context.put("staffCode", staffCode);

		context.put("data", report);
		
		return fillTemplate(templateFile,context);
		
		
		
	}
	
	public String fillReport2Template(String templateFile,String year,String month,String type,List<Map<String,String>> data) throws IOException
	{
		VelocityContext context = new VelocityContext();

		// context.put("logopath",
		// this.getServletContext().getRealPath("/images/")+File.separator+"logo_01.jpg");

		context.put("logopath", "http://" + this.req.getLocalAddr() + ":" + this.req.getLocalPort() + this.req.getContextPath() + "/images/logo_01.jpg");

		context.put("year", year);
		context.put("month", month);
		context.put("type", type);
		context.put("data", data);
		
		return fillTemplate(templateFile,context);
		
	}

	public String fillReport3Template(String templateFile,String costCenter,String date,String fixTotal,String cellTotal,List<Map<String,String>> data) throws IOException
	{
		VelocityContext context = new VelocityContext();

		// context.put("logopath",
		// this.getServletContext().getRealPath("/images/")+File.separator+"logo_01.jpg");

		context.put("logopath", "http://" + this.req.getLocalAddr() + ":" + this.req.getLocalPort() + this.req.getContextPath() + "/images/logo_01.jpg");
		context.put("costCenter", costCenter);
		context.put("date", date);
		context.put("fixTotal", fixTotal);
		context.put("cellTotal", cellTotal);
		context.put("data", data);
		
		return fillTemplate(templateFile,context);
		
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		this.doGet(req, resp);
	}

	@Override
	public void init() throws ServletException {
		
		context = (WebApplicationContext) this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		
		telephoneBillDaoService = (TelephoneBillDaoService) context.getBean("telephoneBillDaoService");
		
		reportBizService = context.getBean(ReportBizService.class);
		
		reportLocalBizService = context.getBean(ReportLocalBizService.class);
		
		costCenterReportDaoService = context.getBean(CostCenterReportDaoService.class);
		
		costCenterEmployeeReportDaoService = context.getBean(CostCenterEmployeeReportDaoService.class);
		
		t = context.getBean(TIMService.class);
		
		g = context.getBean(GenerateAllCostCenterReport.class);
		
		
		super.init();
	}

	
	
	
	
}
