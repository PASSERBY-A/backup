package com.hp.idc.resm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;

/**
 * 字符串工具
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class StringUtil {

	/**
	 * 默认的日期时间格式
	 */
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 字符串类型
	 * @see #checkNumber(String)
	 */
	public static final int TYPE_STRING = 0;
	
	/**
	 * 整数类型
	 * @see #checkNumber(String)
	 */
	public static final int TYPE_INTEGER = 1;
	
	/**
	 * 数字类型
	 * @see #checkNumber(String)
	 */
	public static final int TYPE_DOUBLE = 2;

	/**
	 * GBK对应UNICODE表
	 */
	public static int codeMapping[] = null;

	/**
	 * 初始化代码表
	 * 
	 * @throws IOException
	 *             IO异常时发生
	 */
	public static void initCodePage() throws IOException {
		System.out.println("loading codepage 936 ...");
		int[] codeMapping1 = new int[65536];
		for (int i = 0; i < codeMapping1.length; i++)
			codeMapping1[i] = 0;
		InputStream inp = StringUtil.class
				.getResourceAsStream("/META-INF/CP936.TXT");
		BufferedReader bufferedreader = new BufferedReader(
				new InputStreamReader(inp));
		String s;
		while ((s = bufferedreader.readLine()) != null) {
			String[] ss = s.split("\t");
			if (ss.length >= 2 && ss[0].startsWith("0x")
					&& ss[1].startsWith("0x")) {
				int n1 = Integer.parseInt(ss[0].substring(2), 16);
				int n2 = Integer.parseInt(ss[1].substring(2), 16);
				codeMapping1[n2] = n1;
			}
		}
		codeMapping = codeMapping1;
	}
	
	/**
	 * 生成md5码
	 * @param s 编码的字符串
	 * @return md5码
	 */
	public final static String MD5(String s) {
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			StringBuilder sb = new StringBuilder(33);
			for (int i = 0; i < j; i++) {
				Byte byte0 = md[i];
				sb.append(hex(byte0));
			}
			return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 进行中文字符串比较大小
	 * 
	 * @param str1
	 *            字符串1
	 * @param str2
	 *            字符串2
	 * @return 比较结果，-1/0/1
	 */
	public static int compareChinese(String str1, String str2) {
		if (codeMapping == null) {
			try {
				initCodePage();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (str1 == null) {
			if (str2 == null)
				return 0;
			return -1;
		} else if (str2 == null)
			return 1;
		int ret = 0, pos = 0;
		int p1, p2;
		int n1 = str1.length();
		int n2 = str2.length();
		for (;; pos++) {
			p1 = (pos < n1) ? codeMapping[str1.charAt(pos)] : 0;
			p2 = (pos < n2) ? codeMapping[str2.charAt(pos)] : 0;
			ret = p1 - p2;
			if (ret != 0 || pos == n2)
				break;
		}
		if (ret < 0)
			ret = -1;
		else if (ret > 0)
			ret = 1;
		return ret;
	}

	/**
	 * 进行字符串比较大小
	 * 
	 * @param str1
	 *            字符串1
	 * @param str2
	 *            字符串2
	 * @return 比较结果，-1/0/1
	 */
	public static int compare(String str1, String str2) {
		if (str1 == null) {
			if (str2 == null)
				return 0;
			return -1;
		} else if (str2 == null)
			return 1;
		return str1.compareTo(str2);
	}
	
	/**
	 * 判断字符串是否为空(str == null || str.length() == 0)
	 * @param str 字符串
	 * @return true=空，false=不为空
	 */
	public static boolean isBlank(String str) {
		return (str == null || str.length() == 0);
	}
	
	/**
	 * 将字符串转成数字
	 * @param str 字符串
	 * @return 整数时返回Integer, 带小数时返回Double，格式错误返回null
	 */
	public static Object parseNumber(String str) {
		int n = checkNumber(str);
		if (n == TYPE_INTEGER)
			return Integer.parseInt(str);
		if (n == TYPE_DOUBLE)
			return Double.parseDouble(str);
		return null;
	}
	/**
	 * 判断是否为数字
	 * @param str 字符串
	 * @return 0=字符串。1=整数，2=带小数
	 */
	public static int checkNumber(String str) {
		if (str == null || str.length() == 0)
			return TYPE_STRING;
		int pos = 0;
		int ret = TYPE_STRING;
		if (str.charAt(0) == '-') {
			pos++;
		}
		while (pos < str.length() && str.charAt(pos) >= '0' && str.charAt(pos) <= '9') {
			pos++;
			ret = TYPE_INTEGER;
		}
		if (pos == str.length())
			return ret;
		if (str.charAt(pos) == '.') {
			if (ret == TYPE_INTEGER)
				ret = TYPE_DOUBLE;
			pos++;
		}
		while (pos < str.length() && str.charAt(pos) >= '0' && str.charAt(pos) <= '9') {
			pos++;
			ret = TYPE_DOUBLE;
		}
		if (pos == str.length())
			return ret;
		return TYPE_STRING;
	}

	/**
	 * 转换时间到字符串
	 * 
	 * @param format
	 *            时间格式
	 * @param m
	 *            时间
	 * @return 转换后的字符串
	 */
	public static String getDateTime(String format, long m) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(m);
	}

	/**
	 * 将字符串转换为数字
	 * 
	 * @param str
	 *            字符串
	 * @param def
	 *            默认值，str为空或非字符时的返回值
	 * @return 转换后的数字
	 */
	public static int parseInt(String str, int def) {
		int v = def;
		if (str != null && str.length() > 0) {
			try {
				v = Integer.parseInt(str);
			} catch (Exception e) {
				// 此异常不做处理
			}
		}
		return v;
	}

	/**
	 * 将字符串转换为数字
	 * 
	 * @param str
	 *            字符串
	 * @param def
	 *            默认值，str为空或非字符时的返回值
	 * @return 转换后的数字
	 */
	public static long parseLong(String str, long def) {
		long v = def;
		if (str != null && str.length() > 0) {
			try {
				v = Long.parseLong(str);
			} catch (Exception e) {
				// 此异常不做处理
			}
		}
		return v;
	}

	/**
	 * 将字符串转换为数字
	 * 
	 * @param str
	 *            字符串
	 * @param def
	 *            默认值，str为空或非字符时的返回值
	 * @return 转换后的数字
	 */
	public static double parseDouble(String str, double def) {
		double v = def;
		if (str != null && str.length() > 0) {
			try {
				v = Double.parseDouble(str);
			} catch (Exception e) {
				// 此异常不做处理
			}
		}
		return v;
	}
	
	/**
	 * 转换为html编码的字符串
	 * 
	 * @param str
	 *            要转换的内容
	 * @return 转换后的内容
	 */
	public static String escapeHtml(String str) {
		if (str == null)
			return "";
		int len = str.length();
		if (len == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			if (c == '"')
				sb.append("&quot;");
			else if (c == '&')
				sb.append("&amp;");
			else if (c == '<')
				sb.append("&lt;");
			else if (c == '>')
				sb.append("&gt;");
			else
				sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * 转换为xml编码的字符串
	 * 
	 * @param str
	 *            要转换的内容
	 * @return 转换后的内容
	 */
	public static String escapeXml(String str) {
		if (str == null)
			return "";
		int len = str.length();
		if (len == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			if (c == '\'')
				sb.append("&apos;");
			else if (c == '"')
				sb.append("&quot;");
			else if (c == '&')
				sb.append("&amp;");
			else if (c == '<')
				sb.append("&lt;");
			else if (c == '>')
				sb.append("&gt;");
			else
				sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * 转换为java编码的字符串
	 * 
	 * @param str
	 *            要转换的内容
	 * @return 转换后的内容
	 */
	public static String escapeJava(String str) {
		return escapeJavaStyleString(str, false);
	}

	/**
	 * 输出java编码的字符串
	 * 
	 * @param out
	 *            输出
	 * @param str
	 *            要转换的内容
	 * @throws IOException
	 *             IO异常时发生
	 */
	public static void escapeJava(Writer out, String str) throws IOException {
		escapeJavaStyleString(out, str, false);
	}

	/**
	 * 转换为JavaScript编码的字符串
	 * 
	 * @param str
	 *            要转换的内容
	 * @return 转换后的内容
	 */
	public static String escapeJavaScript(String str) {
		return escapeJavaStyleString(str, true);
	}

	/**
	 * 输出为JavaScript编码的字符串
	 * 
	 * @param out
	 *            输出
	 * @param str
	 *            要转换的内容
	 * @throws IOException
	 *             IO异常时发生
	 */
	public static void escapeJavaScript(Writer out, String str)
			throws IOException {
		escapeJavaStyleString(out, str, true);
	}

	/**
	 * 转换为java编码的字符串
	 * 
	 * @param str
	 *            要转换的内容
	 * @param escapeSingleQuotes
	 *            转换单引号
	 * @return 转换后的内容
	 */
	private static String escapeJavaStyleString(String str,
			boolean escapeSingleQuotes) {
		if (str == null) {
			return "";
		}
		try {
			StringWriter writer = new StringWriter(str.length() * 2);
			escapeJavaStyleString(writer, str, escapeSingleQuotes);
			return writer.toString();
		} catch (IOException ioe) {
			// this should never ever happen while writing to a StringWriter
			ioe.printStackTrace();
			return "";
		}
	}

	/**
	 * 输出java编码的字符串
	 * 
	 * @param out
	 *            输出
	 * @param str
	 *            要转换的内容
	 * @param escapeSingleQuote
	 *            转换单引号
	 * @throws IOException
	 *             IO异常时发生
	 */
	private static void escapeJavaStyleString(Writer out, String str,
			boolean escapeSingleQuote) throws IOException {
		if (out == null) {
			throw new IllegalArgumentException("The Writer must not be null"); //$NON-NLS-1$
		}
		if (str == null) {
			return;
		}
		int sz;
		sz = str.length();
		for (int i = 0; i < sz; i++) {
			char ch = str.charAt(i);

			// handle unicode
			if (ch > 0xfff) {
				out.write("\\u" + hex(ch)); //$NON-NLS-1$
			} else if (ch > 0xff) {
				out.write("\\u0" + hex(ch)); //$NON-NLS-1$
			} else if (ch > 0x7f) {
				out.write("\\u00" + hex(ch)); //$NON-NLS-1$
			} else if (ch < 32) {
				switch (ch) {
				case '\b':
					out.write('\\');
					out.write('b');
					break;
				case '\n':
					out.write('\\');
					out.write('n');
					break;
				case '\t':
					out.write('\\');
					out.write('t');
					break;
				case '\f':
					out.write('\\');
					out.write('f');
					break;
				case '\r':
					out.write('\\');
					out.write('r');
					break;
				default:
					if (ch > 0xf) {
						out.write("\\u00" + hex(ch)); //$NON-NLS-1$
					} else {
						out.write("\\u000" + hex(ch)); //$NON-NLS-1$
					}
					break;
				}
			} else {
				switch (ch) {
				case '\'':
					if (escapeSingleQuote)
						out.write('\\');
					out.write('\'');
					break;
				case '"':
					out.write('\\');
					out.write('"');
					break;
				case '\\':
					out.write('\\');
					out.write('\\');
					break;
				default:
					out.write(ch);
					break;
				}
			}
		}
	}

	/**
	 * 转换为HEX字符串
	 * 
	 * @param ch
	 *            要转换的内容
	 * @return 转换后的内容
	 */
	private static String hex(char ch) {
		return Integer.toHexString(ch).toUpperCase();
	}

	/**
	 * 16进制数组
	 */
	private final static char hexDigits[] = { '0', '1', '2', '3', '4', '5',
		'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 转换为HEX字符串
	 * 
	 * @param b
	 *            要转换的内容
	 * @return 转换后的内容
	 */
	public static String hex(byte b) {
		char c1 = hexDigits[b >>> 4 & 0xf];
		char c2 = hexDigits[b & 0xf];
		return "" + c1 + c2;
	}


	/**
	 * TEST
	 * @param args TEST
	 */
	public static void main(String[] args) {
		System.out.println(MD5("1"));
	}
}
