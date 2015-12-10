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
 * �ַ�������
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class StringUtil {

	/**
	 * Ĭ�ϵ�����ʱ���ʽ
	 */
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * �ַ�������
	 * @see #checkNumber(String)
	 */
	public static final int TYPE_STRING = 0;
	
	/**
	 * ��������
	 * @see #checkNumber(String)
	 */
	public static final int TYPE_INTEGER = 1;
	
	/**
	 * ��������
	 * @see #checkNumber(String)
	 */
	public static final int TYPE_DOUBLE = 2;

	/**
	 * GBK��ӦUNICODE��
	 */
	public static int codeMapping[] = null;

	/**
	 * ��ʼ�������
	 * 
	 * @throws IOException
	 *             IO�쳣ʱ����
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
	 * ����md5��
	 * @param s ������ַ���
	 * @return md5��
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
	 * ���������ַ����Ƚϴ�С
	 * 
	 * @param str1
	 *            �ַ���1
	 * @param str2
	 *            �ַ���2
	 * @return �ȽϽ����-1/0/1
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
	 * �����ַ����Ƚϴ�С
	 * 
	 * @param str1
	 *            �ַ���1
	 * @param str2
	 *            �ַ���2
	 * @return �ȽϽ����-1/0/1
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
	 * �ж��ַ����Ƿ�Ϊ��(str == null || str.length() == 0)
	 * @param str �ַ���
	 * @return true=�գ�false=��Ϊ��
	 */
	public static boolean isBlank(String str) {
		return (str == null || str.length() == 0);
	}
	
	/**
	 * ���ַ���ת������
	 * @param str �ַ���
	 * @return ����ʱ����Integer, ��С��ʱ����Double����ʽ���󷵻�null
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
	 * �ж��Ƿ�Ϊ����
	 * @param str �ַ���
	 * @return 0=�ַ�����1=������2=��С��
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
	 * ת��ʱ�䵽�ַ���
	 * 
	 * @param format
	 *            ʱ���ʽ
	 * @param m
	 *            ʱ��
	 * @return ת������ַ���
	 */
	public static String getDateTime(String format, long m) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(m);
	}

	/**
	 * ���ַ���ת��Ϊ����
	 * 
	 * @param str
	 *            �ַ���
	 * @param def
	 *            Ĭ��ֵ��strΪ�ջ���ַ�ʱ�ķ���ֵ
	 * @return ת���������
	 */
	public static int parseInt(String str, int def) {
		int v = def;
		if (str != null && str.length() > 0) {
			try {
				v = Integer.parseInt(str);
			} catch (Exception e) {
				// ���쳣��������
			}
		}
		return v;
	}

	/**
	 * ���ַ���ת��Ϊ����
	 * 
	 * @param str
	 *            �ַ���
	 * @param def
	 *            Ĭ��ֵ��strΪ�ջ���ַ�ʱ�ķ���ֵ
	 * @return ת���������
	 */
	public static long parseLong(String str, long def) {
		long v = def;
		if (str != null && str.length() > 0) {
			try {
				v = Long.parseLong(str);
			} catch (Exception e) {
				// ���쳣��������
			}
		}
		return v;
	}

	/**
	 * ���ַ���ת��Ϊ����
	 * 
	 * @param str
	 *            �ַ���
	 * @param def
	 *            Ĭ��ֵ��strΪ�ջ���ַ�ʱ�ķ���ֵ
	 * @return ת���������
	 */
	public static double parseDouble(String str, double def) {
		double v = def;
		if (str != null && str.length() > 0) {
			try {
				v = Double.parseDouble(str);
			} catch (Exception e) {
				// ���쳣��������
			}
		}
		return v;
	}
	
	/**
	 * ת��Ϊhtml������ַ���
	 * 
	 * @param str
	 *            Ҫת��������
	 * @return ת���������
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
	 * ת��Ϊxml������ַ���
	 * 
	 * @param str
	 *            Ҫת��������
	 * @return ת���������
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
	 * ת��Ϊjava������ַ���
	 * 
	 * @param str
	 *            Ҫת��������
	 * @return ת���������
	 */
	public static String escapeJava(String str) {
		return escapeJavaStyleString(str, false);
	}

	/**
	 * ���java������ַ���
	 * 
	 * @param out
	 *            ���
	 * @param str
	 *            Ҫת��������
	 * @throws IOException
	 *             IO�쳣ʱ����
	 */
	public static void escapeJava(Writer out, String str) throws IOException {
		escapeJavaStyleString(out, str, false);
	}

	/**
	 * ת��ΪJavaScript������ַ���
	 * 
	 * @param str
	 *            Ҫת��������
	 * @return ת���������
	 */
	public static String escapeJavaScript(String str) {
		return escapeJavaStyleString(str, true);
	}

	/**
	 * ���ΪJavaScript������ַ���
	 * 
	 * @param out
	 *            ���
	 * @param str
	 *            Ҫת��������
	 * @throws IOException
	 *             IO�쳣ʱ����
	 */
	public static void escapeJavaScript(Writer out, String str)
			throws IOException {
		escapeJavaStyleString(out, str, true);
	}

	/**
	 * ת��Ϊjava������ַ���
	 * 
	 * @param str
	 *            Ҫת��������
	 * @param escapeSingleQuotes
	 *            ת��������
	 * @return ת���������
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
	 * ���java������ַ���
	 * 
	 * @param out
	 *            ���
	 * @param str
	 *            Ҫת��������
	 * @param escapeSingleQuote
	 *            ת��������
	 * @throws IOException
	 *             IO�쳣ʱ����
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
	 * ת��ΪHEX�ַ���
	 * 
	 * @param ch
	 *            Ҫת��������
	 * @return ת���������
	 */
	private static String hex(char ch) {
		return Integer.toHexString(ch).toUpperCase();
	}

	/**
	 * 16��������
	 */
	private final static char hexDigits[] = { '0', '1', '2', '3', '4', '5',
		'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * ת��ΪHEX�ַ���
	 * 
	 * @param b
	 *            Ҫת��������
	 * @return ת���������
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
