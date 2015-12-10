package com.hp.idc.resm.util;

import java.io.UnsupportedEncodingException;

/**
 * 转换字符串的编码
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class CharsetUtil {
	/** 7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁块 */
	public static final String US_ASCII = "US-ASCII";

	/** ISO拉丁字母表 No.1，也叫做ISO-LATIN-1 */
	public static final String ISO_8859_1 = "ISO-8859-1";

	/** 8 位 UCS 转换格式 */
	public static final String UTF_8 = "UTF-8";

	/** 16 位 UCS 转换格式，Big Endian(最低地址存放高位字节）字节顺序 */
	public static final String UTF_16BE = "UTF-16BE";

	/** 16 位 UCS 转换格式，Litter Endian（最高地址存放地位字节）字节顺序 */
	public static final String UTF_16LE = "UTF-16LE";

	/** 16 位 UCS 转换格式，字节顺序由可选的字节顺序标记来标识 */
	public static final String UTF_16 = "UTF-16";

	/** 中文超大字符集 **/
	public static final String GBK = "GBK";

	/** GB2312中文字符集 **/
	public static final String GB2312 = "GB2312";

	/**
	 * 将字符编码转换成US-ASCII码
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return 转换后的字符串
	 * @throws UnsupportedEncodingException
	 *             编码不支持时发生
	 */
	public static String toASCII(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, US_ASCII);
	}

	/**
	 * 将字符编码转换成ISO-8859-1
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return 转换后的字符串
	 * @throws UnsupportedEncodingException
	 *             编码不支持时发生
	 */
	public static String toISO_8859_1(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, ISO_8859_1);
	}

	/**
	 * 将字符编码转换成UTF-8
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return 转换后的字符串
	 * @throws UnsupportedEncodingException
	 *             编码不支持时发生
	 */
	public static String toUTF_8(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, UTF_8);
	}

	/**
	 * 将字符编码转换成UTF-16BE
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return 转换后的字符串
	 * @throws UnsupportedEncodingException
	 *             编码不支持时发生
	 */
	public static String toUTF_16BE(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, UTF_16BE);
	}

	/**
	 * 将字符编码转换成UTF-16LE
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return 转换后的字符串
	 * @throws UnsupportedEncodingException
	 *             编码不支持时发生
	 */
	public static String toUTF_16LE(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, UTF_16LE);
	}

	/**
	 * 将字符编码转换成UTF-16
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return 转换后的字符串
	 * @throws UnsupportedEncodingException
	 *             编码不支持时发生
	 */
	public static String toUTF_16(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, UTF_16);
	}

	/**
	 * 将字符编码转换成GBK
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return 转换后的字符串
	 * @throws UnsupportedEncodingException
	 *             编码不支持时发生
	 */
	public static String toGBK(String str) throws UnsupportedEncodingException {
		return changeCharset(str, GBK);
	}

	/**
	 * 将字符编码转换成GB2312
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return 转换后的字符串
	 * @throws UnsupportedEncodingException
	 *             编码不支持时发生
	 */
	public static String toGB2312(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, GB2312);
	}

	/**
	 * 字符串编码转换的实现方法
	 * 
	 * @param str
	 *            要转换的字符串
	 * @param newCharset
	 *            目标编码
	 * @return 转换后的字符串
	 * @throws UnsupportedEncodingException
	 *             编码不支持时发生
	 */
	public static String changeCharset(String str, String newCharset)
			throws UnsupportedEncodingException {
		if (str != null) {
			// 用默认字符编码解码字符串。与系统相关，中文windows默认为GB2312
			byte[] bs = str.getBytes();
			return new String(bs, newCharset); // 用新的字符编码生成字符串
		}
		return null;
	}

	/**
	 * 字符串编码转换的实现方法
	 * 
	 * @param str
	 *            要转换的字符串
	 * @param oldCharset
	 *            源字符集
	 * @param newCharset
	 *            目标编码
	 * @return 转换后的字符串
	 * @throws UnsupportedEncodingException
	 *             编码不支持时发生
	 */
	public static String changeCharset(String str, String oldCharset,
			String newCharset) throws UnsupportedEncodingException {
		if (str != null) {
			// 用源字符编码解码字符串
			byte[] bs = str.getBytes(oldCharset);
			return new String(bs, newCharset);
		}
		return null;
	}

	/**
	 * 测试函数
	 * 
	 * @param args
	 *            参数
	 * @throws UnsupportedEncodingException
	 *             UnsupportedEncodingException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		String str = "This is a 中文的 String!";
		System.out.println("str：" + str);

		String gbk = toGBK(str);
		System.out.println("转换成GBK码：" + gbk);
		System.out.println();

		String ascii = toASCII(str);
		System.out.println("转换成US-ASCII：" + ascii);
		System.out.println();

		String iso88591 = toISO_8859_1(str);
		System.out.println("转换成ISO-8859-1码：" + iso88591);
		System.out.println();

		gbk = changeCharset(iso88591, ISO_8859_1, GBK);
		System.out.println("再把ISO-8859-1码的字符串转换成GBK码：" + gbk);
		System.out.println();

		String utf8 = toUTF_8(str);
		System.out.println();
		System.out.println("转换成UTF-8码：" + utf8);
		String utf16be = toUTF_16BE(str);
		System.out.println("转换成UTF-16BE码：" + utf16be);
		gbk = changeCharset(utf16be, UTF_16BE, GBK);
		System.out.println("再把UTF-16BE编码的字符转换成GBK码：" + gbk);
		System.out.println();

		String utf16le = toUTF_16LE(str);
		System.out.println("转换成UTF-16LE码：" + utf16le);
		gbk = changeCharset(utf16le, UTF_16LE, GBK);
		System.out.println("再把UTF-16LE编码的字符串转换成GBK码：" + gbk);
		System.out.println();

		String utf16 = toUTF_16(str);
		System.out.println("转换成UTF-16码：" + utf16);
		String gb2312 = changeCharset(utf16, UTF_16, GB2312);
		System.out.println("再把UTF-16编码的字符串转换成GB2312码：" + gb2312);
	}

}
