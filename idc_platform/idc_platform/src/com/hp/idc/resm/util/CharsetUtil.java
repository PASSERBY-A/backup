package com.hp.idc.resm.util;

import java.io.UnsupportedEncodingException;

/**
 * ת���ַ����ı���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class CharsetUtil {
	/** 7λASCII�ַ���Ҳ����ISO646-US��Unicode�ַ����Ļ��������� */
	public static final String US_ASCII = "US-ASCII";

	/** ISO������ĸ�� No.1��Ҳ����ISO-LATIN-1 */
	public static final String ISO_8859_1 = "ISO-8859-1";

	/** 8 λ UCS ת����ʽ */
	public static final String UTF_8 = "UTF-8";

	/** 16 λ UCS ת����ʽ��Big Endian(��͵�ַ��Ÿ�λ�ֽڣ��ֽ�˳�� */
	public static final String UTF_16BE = "UTF-16BE";

	/** 16 λ UCS ת����ʽ��Litter Endian����ߵ�ַ��ŵ�λ�ֽڣ��ֽ�˳�� */
	public static final String UTF_16LE = "UTF-16LE";

	/** 16 λ UCS ת����ʽ���ֽ�˳���ɿ�ѡ���ֽ�˳��������ʶ */
	public static final String UTF_16 = "UTF-16";

	/** ���ĳ����ַ��� **/
	public static final String GBK = "GBK";

	/** GB2312�����ַ��� **/
	public static final String GB2312 = "GB2312";

	/**
	 * ���ַ�����ת����US-ASCII��
	 * 
	 * @param str
	 *            Ҫת�����ַ���
	 * @return ת������ַ���
	 * @throws UnsupportedEncodingException
	 *             ���벻֧��ʱ����
	 */
	public static String toASCII(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, US_ASCII);
	}

	/**
	 * ���ַ�����ת����ISO-8859-1
	 * 
	 * @param str
	 *            Ҫת�����ַ���
	 * @return ת������ַ���
	 * @throws UnsupportedEncodingException
	 *             ���벻֧��ʱ����
	 */
	public static String toISO_8859_1(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, ISO_8859_1);
	}

	/**
	 * ���ַ�����ת����UTF-8
	 * 
	 * @param str
	 *            Ҫת�����ַ���
	 * @return ת������ַ���
	 * @throws UnsupportedEncodingException
	 *             ���벻֧��ʱ����
	 */
	public static String toUTF_8(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, UTF_8);
	}

	/**
	 * ���ַ�����ת����UTF-16BE
	 * 
	 * @param str
	 *            Ҫת�����ַ���
	 * @return ת������ַ���
	 * @throws UnsupportedEncodingException
	 *             ���벻֧��ʱ����
	 */
	public static String toUTF_16BE(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, UTF_16BE);
	}

	/**
	 * ���ַ�����ת����UTF-16LE
	 * 
	 * @param str
	 *            Ҫת�����ַ���
	 * @return ת������ַ���
	 * @throws UnsupportedEncodingException
	 *             ���벻֧��ʱ����
	 */
	public static String toUTF_16LE(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, UTF_16LE);
	}

	/**
	 * ���ַ�����ת����UTF-16
	 * 
	 * @param str
	 *            Ҫת�����ַ���
	 * @return ת������ַ���
	 * @throws UnsupportedEncodingException
	 *             ���벻֧��ʱ����
	 */
	public static String toUTF_16(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, UTF_16);
	}

	/**
	 * ���ַ�����ת����GBK
	 * 
	 * @param str
	 *            Ҫת�����ַ���
	 * @return ת������ַ���
	 * @throws UnsupportedEncodingException
	 *             ���벻֧��ʱ����
	 */
	public static String toGBK(String str) throws UnsupportedEncodingException {
		return changeCharset(str, GBK);
	}

	/**
	 * ���ַ�����ת����GB2312
	 * 
	 * @param str
	 *            Ҫת�����ַ���
	 * @return ת������ַ���
	 * @throws UnsupportedEncodingException
	 *             ���벻֧��ʱ����
	 */
	public static String toGB2312(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, GB2312);
	}

	/**
	 * �ַ�������ת����ʵ�ַ���
	 * 
	 * @param str
	 *            Ҫת�����ַ���
	 * @param newCharset
	 *            Ŀ�����
	 * @return ת������ַ���
	 * @throws UnsupportedEncodingException
	 *             ���벻֧��ʱ����
	 */
	public static String changeCharset(String str, String newCharset)
			throws UnsupportedEncodingException {
		if (str != null) {
			// ��Ĭ���ַ���������ַ�������ϵͳ��أ�����windowsĬ��ΪGB2312
			byte[] bs = str.getBytes();
			return new String(bs, newCharset); // ���µ��ַ����������ַ���
		}
		return null;
	}

	/**
	 * �ַ�������ת����ʵ�ַ���
	 * 
	 * @param str
	 *            Ҫת�����ַ���
	 * @param oldCharset
	 *            Դ�ַ���
	 * @param newCharset
	 *            Ŀ�����
	 * @return ת������ַ���
	 * @throws UnsupportedEncodingException
	 *             ���벻֧��ʱ����
	 */
	public static String changeCharset(String str, String oldCharset,
			String newCharset) throws UnsupportedEncodingException {
		if (str != null) {
			// ��Դ�ַ���������ַ���
			byte[] bs = str.getBytes(oldCharset);
			return new String(bs, newCharset);
		}
		return null;
	}

	/**
	 * ���Ժ���
	 * 
	 * @param args
	 *            ����
	 * @throws UnsupportedEncodingException
	 *             UnsupportedEncodingException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		String str = "This is a ���ĵ� String!";
		System.out.println("str��" + str);

		String gbk = toGBK(str);
		System.out.println("ת����GBK�룺" + gbk);
		System.out.println();

		String ascii = toASCII(str);
		System.out.println("ת����US-ASCII��" + ascii);
		System.out.println();

		String iso88591 = toISO_8859_1(str);
		System.out.println("ת����ISO-8859-1�룺" + iso88591);
		System.out.println();

		gbk = changeCharset(iso88591, ISO_8859_1, GBK);
		System.out.println("�ٰ�ISO-8859-1����ַ���ת����GBK�룺" + gbk);
		System.out.println();

		String utf8 = toUTF_8(str);
		System.out.println();
		System.out.println("ת����UTF-8�룺" + utf8);
		String utf16be = toUTF_16BE(str);
		System.out.println("ת����UTF-16BE�룺" + utf16be);
		gbk = changeCharset(utf16be, UTF_16BE, GBK);
		System.out.println("�ٰ�UTF-16BE������ַ�ת����GBK�룺" + gbk);
		System.out.println();

		String utf16le = toUTF_16LE(str);
		System.out.println("ת����UTF-16LE�룺" + utf16le);
		gbk = changeCharset(utf16le, UTF_16LE, GBK);
		System.out.println("�ٰ�UTF-16LE������ַ���ת����GBK�룺" + gbk);
		System.out.println();

		String utf16 = toUTF_16(str);
		System.out.println("ת����UTF-16�룺" + utf16);
		String gb2312 = changeCharset(utf16, UTF_16, GB2312);
		System.out.println("�ٰ�UTF-16������ַ���ת����GB2312�룺" + gb2312);
	}

}
