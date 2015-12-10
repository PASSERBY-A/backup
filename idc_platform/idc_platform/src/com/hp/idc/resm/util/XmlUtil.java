package com.hp.idc.resm.util;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;

/**
 * XML工具
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class XmlUtil {

	/**
	 * 转换字符串中的非法字符到?
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return 转换后的字符串
	 */
	public static String escapeUnformChar1(String str) {
		return escapeUnformChar(str, "?");
	}

	/**
	 * 转换字符串中的非法字符
	 * 
	 * @param str
	 *            要转换的字符串
	 * @param c
	 *            替换的字符
	 * @return 转换后的字符串
	 */
	public static String escapeUnformChar(String str, String c) {
		if (str == null || str.length() == 0)
			return str;
		return str.replaceAll("[\u0000-\u0008\u000b\u000c\u000e-\u001f]", c);
	}

	/**
	 * 将字符串转换为xml的文档对象
	 * 
	 * @param xml
	 *            XML格式的字符串
	 * @return 文档对象
	 * @throws DocumentException
	 *             xml格式错误时发生
	 */
	public static Document parseString(String xml) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = null;
		doc = reader.read(new StringReader(xml));
		return doc;
	}

	/**
	 * 创建dom4j 的Document对象
	 * 
	 * @param xml
	 *            xml字符串
	 * @param encoding
	 *            编码格式，例如：GB2312/UTF-8/GBK等等
	 * @return Document对象
	 * @throws DocumentException
	 *             xml格式错误时发生
	 */
	public static Document parseString(String xml, String encoding)
			throws DocumentException {
		SAXReader reader = new SAXReader();
		reader.setEncoding(encoding);
		Document doc = null;
		doc = reader.read(new StringReader(xml));
		return doc;
	}

	/**
	 * 从文件中读取内容，生成xml的文档对象
	 * 
	 * @param file
	 *            文件
	 * @return 文档对象
	 * @throws DocumentException
	 *             xml格式错误时发生
	 */
	public static Document parseFile(File file) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = null;
		doc = reader.read(file);
		return doc;
	}

	/**
	 * 从文件中读取内容，生成xml的文档对象
	 * 
	 * @param fileName
	 *            文件名
	 * @return 文档对象。文件不存在时，返回null。
	 * @throws DocumentException
	 *             xml格式错误时发生
	 */
	public static Document parseFile(String fileName) throws DocumentException {
		File f = new File(fileName);
		if (!f.exists())
			return null;

		SAXReader reader = new SAXReader();
		Document doc = null;
		doc = reader.read(f);
		return doc;
	}

	/**
	 * 将XML文档转换为字符串
	 * 
	 * @param doc
	 *            文档对象
	 * @return XML内容
	 */
	public static String getXmlString(Document doc) {
		StringWriter writer = new StringWriter();
		org.dom4j.io.XMLWriter xmlwriter = new org.dom4j.io.XMLWriter(writer);
		try {
			xmlwriter.write(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}
	
	/**
	 * 格式化成标准格式
	 * @param doc
	 *            文档对象
	 * @return 格式化后的字符串
	 */
	public static String getPrettyXmlString(Document doc)  {
		StringWriter writer = new StringWriter();
		OutputFormat of = new OutputFormat();   
		of.setIndent(true);
		of.setNewlines(true);
		of.setIndent("\t");
		of.setNewLineAfterDeclaration(false);
		of.setTrimText(false);
		org.dom4j.io.XMLWriter xmlwriter = new org.dom4j.io.XMLWriter(writer, of);
		try {
			xmlwriter.write(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}
	
	/**
	 * 格式化成标准格式
	 * @param data xml数据
	 * @return 格式化后的字符串
	 * @throws DocumentException xml格式错误时发生
	 */
	public static String getPrettyXmlString(String data) throws DocumentException {
		Document doc = parseString(data);
		return getPrettyXmlString(doc); 
	}
}
