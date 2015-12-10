package com.volkswagen.tel.billing.pdf;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.PDFEncryption;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.util.UUID;



@Component
public class PDFGenerator extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(PDFGenerator.class);
	
	public static ServletContext context = null;
	
	@Autowired
	HTMLParser parser;

	/**
	 * generate pdf file using html code
	 * @param obj
	 * @param telephoneNumber
	 * @param year
	 * @param month
	 * @param monthPkg
     *@param dataBoPkg
     * @param smsBoPkg
     * @param roamingBoPkg
     * @param request  @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws URISyntaxException
	 * @throws DocumentException
	 */
	public String htmlCodeToPdf(JSONObject obj, String telephoneNumber, int year, int month, String monthPkg, String dataBoPkg, String smsBoPkg, String roamingBoPkg, HttpServletRequest request) throws
			IOException, ParserConfigurationException, SAXException,
			URISyntaxException, DocumentException {
		log.info("---------- htmlCodeToPdf start.");
		
		String filename = UUID.randomUUID().toString();
		String htmlCode = parser.getHTMLCode(obj, telephoneNumber, year, month,  monthPkg, dataBoPkg, smsBoPkg, roamingBoPkg,
                request);
		log.info(">>> generated html content: " + htmlCode);

		// StringBuffer buf = new StringBuffer();
		// buf.append(htmlCode);

		context = request.getSession().getServletContext();

		PDFDeleter.deleteTempPDF(request, context.getRealPath("/tmp/"),
				filename + ".pdf");

		File tmpFile = new File(context.getRealPath("/tmp/") + "/" + filename
				+ ".htm");
		log.info(">>> Temp file path: " + tmpFile.getAbsolutePath());

		FileWriter writer = new FileWriter(tmpFile,false);
		writer.write(htmlCode);
		writer.flush();
		writer.close();
								
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(tmpFile);
								
		String outputFile = new File(context.getRealPath("/tmp/")+"/"+filename+".pdf").getAbsolutePath();
		OutputStream os = new FileOutputStream(outputFile);
		
		renderer.layout();
		
		PDFEncryption pdfEncryption = new PDFEncryption();
		
		pdfEncryption.setEncryptionType(PdfWriter.ALLOW_PRINTING);
		pdfEncryption.setEncryptionType(PdfWriter.ALLOW_COPY);
				
		renderer.setPDFEncryption(pdfEncryption);
				
		renderer.createPDF(os, true);
		
		renderer.finishPDF();

		os.close();
						
		tmpFile.delete();
			
		return filename+".pdf";
	}
}