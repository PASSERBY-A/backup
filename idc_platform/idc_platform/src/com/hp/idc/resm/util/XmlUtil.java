package com.hp.idc.resm.util;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;

/**
 * XML����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class XmlUtil {

	/**
	 * ת���ַ����еķǷ��ַ���?
	 * 
	 * @param str
	 *            Ҫת�����ַ���
	 * @return ת������ַ���
	 */
	public static String escapeUnformChar1(String str) {
		return escapeUnformChar(str, "?");
	}

	/**
	 * ת���ַ����еķǷ��ַ�
	 * 
	 * @param str
	 *            Ҫת�����ַ���
	 * @param c
	 *            �滻���ַ�
	 * @return ת������ַ���
	 */
	public static String escapeUnformChar(String str, String c) {
		if (str == null || str.length() == 0)
			return str;
		return str.replaceAll("[\u0000-\u0008\u000b\u000c\u000e-\u001f]", c);
	}

	/**
	 * ���ַ���ת��Ϊxml���ĵ�����
	 * 
	 * @param xml
	 *            XML��ʽ���ַ���
	 * @return �ĵ�����
	 * @throws DocumentException
	 *             xml��ʽ����ʱ����
	 */
	public static Document parseString(String xml) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = null;
		doc = reader.read(new StringReader(xml));
		return doc;
	}

	/**
	 * ����dom4j ��Document����
	 * 
	 * @param xml
	 *            xml�ַ���
	 * @param encoding
	 *            �����ʽ�����磺GB2312/UTF-8/GBK�ȵ�
	 * @return Document����
	 * @throws DocumentException
	 *             xml��ʽ����ʱ����
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
	 * ���ļ��ж�ȡ���ݣ�����xml���ĵ�����
	 * 
	 * @param file
	 *            �ļ�
	 * @return �ĵ�����
	 * @throws DocumentException
	 *             xml��ʽ����ʱ����
	 */
	public static Document parseFile(File file) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = null;
		doc = reader.read(file);
		return doc;
	}

	/**
	 * ���ļ��ж�ȡ���ݣ�����xml���ĵ�����
	 * 
	 * @param fileName
	 *            �ļ���
	 * @return �ĵ������ļ�������ʱ������null��
	 * @throws DocumentException
	 *             xml��ʽ����ʱ����
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
	 * ��XML�ĵ�ת��Ϊ�ַ���
	 * 
	 * @param doc
	 *            �ĵ�����
	 * @return XML����
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
	 * ��ʽ���ɱ�׼��ʽ
	 * @param doc
	 *            �ĵ�����
	 * @return ��ʽ������ַ���
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
	 * ��ʽ���ɱ�׼��ʽ
	 * @param data xml����
	 * @return ��ʽ������ַ���
	 * @throws DocumentException xml��ʽ����ʱ����
	 */
	public static String getPrettyXmlString(String data) throws DocumentException {
		Document doc = parseString(data);
		return getPrettyXmlString(doc); 
	}
}
