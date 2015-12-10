package com.hp.idc.cas.common;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class StringUtil {
	
	public static int parseInt(String str, int def) {
		int v = def;
		if (str != null && str.length() > 0) {
			try {
				v = Integer.parseInt(str);
			}
			catch (Exception e) {
			}
		}
		return v;
	}

	public static long parseLong(String str, long def) {
		long v = def;
		if (str != null && str.length() > 0) {
			try {
				v = Long.parseLong(str);
			}
			catch (Exception e) {
			}
		}
		return v;
	}
	
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
	
	public static String escapeJava(String str) {
        return escapeJavaStyleString(str, false);
    }

    public static void escapeJava(Writer out, String str) throws IOException {
        escapeJavaStyleString(out, str, false);
    }

    public static String escapeJavaScript(String str) {
        return escapeJavaStyleString(str, true);
    }

    public static void escapeJavaScript(Writer out, String str) throws IOException {
        escapeJavaStyleString(out, str, true);
    }

    private static String escapeJavaStyleString(String str, boolean escapeSingleQuotes) {
        if (str == null) {
            return null;
        }
        try {
            StringWriter writer = new StringWriter(str.length() * 2);
            escapeJavaStyleString(writer, str, escapeSingleQuotes);
            return writer.toString();
        } catch (IOException ioe) {
            // this should never ever happen while writing to a StringWriter
            ioe.printStackTrace();
            return null;
        }
    }

    private static void escapeJavaStyleString(Writer out, String str, boolean escapeSingleQuote) throws IOException {
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
                    default :
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
                        if (escapeSingleQuote) out.write('\\');
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
                    default :
                        out.write(ch);
                        break;
                }
            }
        }
    }

    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase();
    }
    
    
    public static String getGBStr(String str){  
		try{
			String temp_p=str;
			byte[] temp_t=temp_p.getBytes("ISO8859-1");
			String temp=new String(temp_t);
			return temp;
		}
		catch(Exception e){}
			return "NULL";
	}


}
