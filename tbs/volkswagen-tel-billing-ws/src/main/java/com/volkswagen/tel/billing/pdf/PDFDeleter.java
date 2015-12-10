package com.volkswagen.tel.billing.pdf;

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("serial")
public class PDFDeleter implements HttpSessionBindingListener, Serializable {
	
	private static final Logger log = LoggerFactory.getLogger(PDFDeleter.class);

	@SuppressWarnings("rawtypes")
	private List pdfNames = new java.util.ArrayList();

	public String tmpdir = "";

	public PDFDeleter() {
		super();
	}

	/**
	 * Add a pdf to be deleted when the session expires
	 * 
	 * @param filename
	 *            the name of the chart in the temporary directory to be
	 *            deleted.
	 */
	@SuppressWarnings("unchecked")
	public void addPDFFileName(String filename) {
		this.pdfNames.add(filename);
	}

	/**
	 * add a chart file tmp dir to be deleted when the session expires
	 * 
	 * @param tmpdir
	 *            the dir of the chart in the temporary directory to be deleted.
	 */
	public void addTempdir(String tmpdir) {
		this.tmpdir = tmpdir;
	}

	/**
	 * Checks to see if a pdf is in the list of pdfs to be deleted
	 * 
	 * @param filename
	 *            the name of the pdf in the temporary directory.
	 * 
	 * @return A boolean value indicating whether the pdf is present in the
	 *         list.
	 */
	public boolean isPDFAvailable(String filename) {
		return (this.pdfNames.contains(filename));
	}

	/**
	 * invoke this method when pdf generated
	 * 
	 * @param request
	 * @param tmpdir
	 * @param fileName
	 */
	public static void deleteTempPDF(HttpServletRequest request, String tmpdir,
			String fileName) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			// get session listener
			PDFDeleter pdfDeleter = (PDFDeleter) session
					.getAttribute("pdfDeleter");

			if (pdfDeleter == null) {
				pdfDeleter = new PDFDeleter();
				session.setAttribute("pdfDeleter", pdfDeleter);

			}

			// check if in the session listener deletion list
			boolean isPDFInUserList = false;
			isPDFInUserList = pdfDeleter.isPDFAvailable(fileName);

			// if not in the session listener deletion list, add to list
			if (!isPDFInUserList) {

				pdfDeleter.addPDFFileName(fileName);
				pdfDeleter.addTempdir(tmpdir);

			}
		}
	}

	/**
	 * valueUnbound will be invoked by the following method a. run
	 * session.setAttribute("pdf_Deleter",pdf_Deleter)。 b. run
	 * session.removeAttribute("pdf_Deleter")。 c. run session.invalidate()。 d.
	 * session timeout。
	 **/
	public void valueUnbound(HttpSessionBindingEvent event) {

		@SuppressWarnings("rawtypes")
		Iterator iter = this.pdfNames.listIterator();

		while (iter.hasNext()) {

			String filename = (String) iter.next();
			File file = new File(tmpdir, filename);

			if (file.exists()) {
				file.delete();
				log.info("file: " + filename +" is deleted");
			}
		}
		return;
	}

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub
	}
}