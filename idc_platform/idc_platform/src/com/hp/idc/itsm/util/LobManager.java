package com.hp.idc.itsm.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.hp.idc.itsm.dbo.OracleOperation;


public class LobManager {

	static public String getClobById(int oid) {
		return readClobById("" + oid);
	}
	
	static public int updateClob(int oid, String content) throws NumberFormatException, SQLException {
		if (oid == -1)
			return Integer.parseInt(writeClob(null, content));
		updateClob("" + oid, content);
		return oid;
	}

	static public String readClobById(String dc_oid) {
		Connection conn = ConnectManager.getConnection();
		String sql = "select * from swp_lobs t where t.lob_oid=?";
		PreparedStatement ps = null;
		Reader reader = null;
		String content = "";
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1,dc_oid);
			rs = ps.executeQuery();
			if (rs.next()) {
				Clob clob = rs.getClob("lob_content");
				reader = clob.getCharacterStream();
				BufferedReader br = new BufferedReader(reader);
				String temp = br.readLine();
				while (temp != null) {
					content += temp + "\n";
					temp = br.readLine();
				}
			}
			rs.close();
			rs = null;
			ps.close();
			ps = null;
			conn.close();
			conn = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
				rs = null;
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
				ps = null;
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
				conn = null;
			}
		}
		return content;
	}

	static public synchronized String writeClob(String oid,String content) throws SQLException {
		Connection conn = ConnectManager.getConnection();
		ResultSet rs = null;
		if (oid == null || oid.equals("")) {
			long _oid = createId();
			if (_oid != -1)
				oid = "" + _oid;
			else
				oid = "" + System.currentTimeMillis();
		}
		
		String sql = "insert into swp_lobs(lob_oid,lob_content) values(?, empty_clob())";
		String sql1 = "select * from swp_lobs where lob_oid=? for update";
		PreparedStatement ps = null;
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			ps.setString(1,oid);
			ps.execute();
			ps.clearParameters();
			ps = conn.prepareStatement(sql1);
			ps.setString(1,oid);
			rs = ps.executeQuery();
			Clob clob = null;
			if (rs.next()) {
				clob = rs.getClob("lob_content");
				Writer writer = clob.setCharacterStream(0);
				writer.write(content.toCharArray());
				writer.close();
			}
			conn.setAutoCommit(true);
			conn.commit();
			rs.close();
			rs = null;
			ps.close();
			ps = null;
			conn.close();
			conn = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				rs = null;
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				ps = null;
			}
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				conn = null;
			}
		}
		return oid;
	}
	
	static public void updateClob(String oid,String content) throws SQLException {
		deleteClob(oid);
		writeClob(oid,content);
	}
	
	static public void deleteClob(String oid) {
		Connection conn = ConnectManager.getConnection();
		String sql = "delete from swp_lobs where lob_oid=?";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1,oid);
			ps.execute();
			ps.close();
			ps = null;
			conn.close();
			conn = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				ps = null;
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				conn = null;
			}
		}
	}
	
	static private String initXML() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\"?>\n");
		sb.append("<swp-report>");
		sb.append("</swp-report>");
		return sb.toString();
	}
	
	static private String initXSL() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\"?>\n");
		sb.append("<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\n");
		sb.append("<xsl:output method=\"html\" omit-xml-declaration=\"yes\"/>\n");
		sb.append("<xsl:template match=\"/\">\n");
		sb.append("</xsl:template>\n");
		sb.append("</xsl:stylesheet>\n");
		return sb.toString();
		
	}
	
	static public String transform(String xml,String xsl) throws Exception{
		
		if (xml==null || "".equals(xml))
			xml = initXML();
		if (xsl==null || "".equals(xsl))
			xsl = initXSL();
		
		/*String xml1 = "";//"<?xml version=\"1.0\"?>\n";
		xml1 += "<root>\n";
		xml1 += "<dynamic-element name=\"name\" type=\"text\">\n";
		xml1 += "<dynamic-content>Ãû³Æ</dynamic-content>\n";
		xml1 += "</dynamic-element>\n";
		xml1 += "</root>\n";
		
		xsl ="<?xml version=\"1.0\"?>\n";

		xsl += "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\n";
		xsl += "<xsl:output method=\"html\" omit-xml-declaration=\"yes\"/>\n";
		xsl += "<xsl:template match=\"/\">\n";
		xsl += "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">  \n";
		xsl += "<tr>  \n";
		xsl += "<td colspan=\"2\"> \n";
		xsl += "<xsl:value-of disable-output-escaping=\"yes\" select=\"root/dynamic-element[@name='name']/dynamic-content\"/>\n";
		xsl += "</td> \n";
		xsl += "</tr> \n";
		xsl += "</table>\n";
		xsl += "</xsl:template>\n";
		xsl += "</xsl:stylesheet>\n";
		*/

		ByteArrayOutputStream outputBaos = new ByteArrayOutputStream();
		
		
		try {
			StreamSource xmlSource = new StreamSource(new StringReader(xml));
			StreamSource scriptSource = new StreamSource(
				new StringReader(xsl));
			
			TransformerFactory transformerFactory =
				TransformerFactory.newInstance();
			//URIResolver ss = new URIResolver();
			transformerFactory.setURIResolver( null);

			Transformer transformer =
				transformerFactory.newTransformer(scriptSource);

			transformer.transform(xmlSource, new StreamResult(outputBaos));
		}
		catch (TransformerException te) {
			te.printStackTrace();
			
		}

			return outputBaos.toString("utf-8");

	}

	static public long createId() throws SQLException {
		return OracleOperation.getSequence("seq_swp");
	}

	public static void main(String[] args) {
		String xml1 = "<?xml version=\"1.0\" encoding=\"GB2312\"?>\n";
		xml1 += "<root>\n";
		xml1 += "<dynamic-element name=\"name\" type=\"text\">\n";
		xml1 += "<dynamic-content>Ãû³Æ</dynamic-content>\n";
		xml1 += "</dynamic-element>\n";
		xml1 += "</root>\n";
		
		
		String xsl ="<?xml version=\"1.0\" encoding=\"GB2312\"?>\n";

		xsl += "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\n";
		xsl += "<xsl:output method=\"html\" omit-xml-declaration=\"yes\"/>\n";
		xsl += "<xsl:template match=\"/\">\n";
		xsl += "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">  \n";
		xsl += "<tr>  \n";
		xsl += "<td colspan=\"2\"> \n";
		xsl += "<xsl:value-of disable-output-escaping=\"yes\" select=\"root/dynamic-element[@name='name']/dynamic-content\"/>\n";
		xsl += "</td> \n";
		xsl += "</tr> \n";
		xsl += "</table>\n";
		xsl += "</xsl:template>\n";
		xsl += "</xsl:stylesheet>\n";
		

		ByteArrayOutputStream outputBaos = new ByteArrayOutputStream();
		
		
		try {
			StreamSource xmlSource = new StreamSource(new StringReader(xml1));
			StreamSource scriptSource = new StreamSource(
				new StringReader(xsl));
			
			TransformerFactory transformerFactory =
				TransformerFactory.newInstance();
			//URIResolver ss = new URIResolver();
			transformerFactory.setURIResolver( null);

			Transformer transformer =
				transformerFactory.newTransformer(scriptSource);

			transformer.transform(xmlSource, new StreamResult(outputBaos));
		}
		catch (TransformerException te) {
			te.printStackTrace();
			
		}
		
		//String s =outputBaos.toString();
	}
}
