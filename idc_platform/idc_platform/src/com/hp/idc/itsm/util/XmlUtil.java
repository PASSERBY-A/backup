package com.hp.idc.itsm.util;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.Document;

public class XmlUtil {

	public static String escapeUnformChar(String str) {
		return escapeUnformChar(str, "?");
	}
	
	public static String escapeUnformChar(String str, String c) {
		if (str == null || str.length() == 0)
			return str;
		return str.replaceAll("[\u0000-\u0008\u000b\u000c\u000e-\u001f]", c);
	}
	
	public static Document parseString(String xml) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document doc2 = null;
		doc2 = reader.read(new StringReader(xml));
		return doc2;
	}
	/**
	 * 创建dom4j 的Document对象
	 * @param xml xml字符串
	 * @param encoding 编码格式，例如：GB2312/UTF-8/GBK等等
	 * @return
	 * @throws DocumentException
	 */
	public static Document parseString(String xml,String encoding) throws DocumentException {
        SAXReader reader = new SAXReader();
        reader.setEncoding(encoding);
        Document doc2 = null;
		doc2 = reader.read(new StringReader(xml));
		return doc2;
	}
	
	public static Document parseFile(File file) throws DocumentException, MalformedURLException {
	   SAXReader reader = new SAXReader();
       Document doc2 = null;
       doc2 = reader.read(file);
       return doc2;
	}
	
	public static Document parseFile(String fileName) throws MalformedURLException, DocumentException {
	   File f = new File(fileName);
	   if (!f.exists())
	      return null;
	   
	   SAXReader reader = new SAXReader();
       Document doc2 = null;
       doc2 = reader.read(f);
       return doc2;
	}

	public static String getXmlString(Document doc2) {
        StringWriter writer = new StringWriter();
        org.dom4j.io.XMLWriter xmlwriter = new org.dom4j.io.XMLWriter(writer);
        try {
            xmlwriter.write(doc2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer.toString();
	}
}
