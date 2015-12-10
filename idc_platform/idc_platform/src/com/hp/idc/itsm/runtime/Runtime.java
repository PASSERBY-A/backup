/**
 * 
 */
package com.hp.idc.itsm.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.hp.idc.itsm.util.ConnectManager;
import com.hp.idc.itsm.util.ItsmUtil;

/**
 * ��̬������
 * @author ÷԰
 */
public class Runtime {

	/**
	 * ��� ����->Class���� �ı�
	 */
	static protected Map map = new HashMap();

	/**
	 * ��ʱ�ļ���ŵ�·��
	 */
	protected String tempPath = ".";

	/**
	 * ����ʱ���õ� SQL ���
	 */
	protected static final String INSERT_SQL = "insert into ITSM_CFG_DYNCODE(dc_classname, dc_source, dc_binary, dc_imports, dc_jars) values( ?, empty_blob(), empty_blob(), ?, ?)";

	/**
	 * ɾ��ʱ���õ� SQL ���
	 */
	protected static final String DELETE_SQL = "delete from ITSM_CFG_DYNCODE where dc_classname=?";

	/**
	 * ��ѯʱ���õ� SQL ���
	 */
	protected static final String SELECT_SQL = "select dc_source, dc_binary from ITSM_CFG_DYNCODE where dc_classname=?";

	/**
	 * ��ָ������ʱ·����ָ������ jar ����·�������µ� Runtime ����
	 * @param path ��ʱ�ļ�������·��
	 */
	public Runtime(String path) {
		this.tempPath = path;
		if (!this.tempPath.endsWith("/") && !this.tempPath.endsWith("\\"))
			this.tempPath = this.tempPath + "/";
	}
	
	/**
	 * �����µ� Runtime ����
	 */
	public Runtime() {
		// Nothing to do here
	}

	/**
	 * ��������������� Class 
	 * @param name ָ��������
	 * @return ������Ӧ�� Class
	 * @throws ClassNotFoundException �Ҳ�����Ӧ����ʱ�׳� ClassNotFoundException �쳣
	 */
	public Class findClass(String name) throws ClassNotFoundException {
		Object obj = map.get(name);
		if (obj != null)
			return (Class) obj;

		RuntimeClassLoader rcl = new RuntimeClassLoader(this.getClass().getClassLoader());
		byte[] data = getClassData(name);
		Class cl = rcl.load(name, data);
		if (cl == null)
			throw new ClassNotFoundException(name);
		map.put(name, cl);
		return cl;
	}

	/**
	 * ���� java ���룬������������Ᵽ��
	 * @param imports0 ���õİ�
	 * @param jars0 ����İ�,��";"�ָ�
	 * @param src java Դ�ļ�������
	 * @return ����ɹ�ʱ���ر༭�õ�����
	 * @throws Exception 
	 */
	public String compile(String imports0, String jars0, String src) throws Exception {

		int id = ItsmUtil.getSequence("code");
		String name = "DynCode_" + id;
		
		return compile(imports0, jars0, src, name);
	}
	
	/**
	 * 
	 * @param imports0
	 * @param jars0
	 * @param src
	 * @param className
	 * @return
	 * @throws Exception
	 */
	public String compile(String imports0, String jars0, String src,String name) throws Exception {
		String jars1 = jars0;
		String imports1 = imports0;
		if (jars1 != null) {
			String OS = System.getProperty("os.name").toLowerCase();
			if (OS.indexOf("windows") > -1) {
				jars1 = jars1.replace(':', ';');
			} else {
				jars1 = jars1.replace(';', ':');
			}
		}
		if (jars1 != null && jars1.length() == 0)
			jars1 = null;

		if (imports1 == null)
			imports1 = "";

		String className = name;
		String src2 = imports1 + "\n"
			+ "public class " +  className + "{\n"
			+ src
			+ "}\n";
		if (className.lastIndexOf('.') != -1)
			className = className.substring(className.lastIndexOf('.') + 1);

		// write source to a temp file
		String fileName = this.tempPath + className + ".java";
		File fin = new File(fileName);
		PrintWriter pw = new PrintWriter(new FileWriter(fin));
		pw.println(src2);
		pw.close();

		StringWriter sw = new StringWriter();
		pw = new PrintWriter(sw);

		// compile file
		String[] cpargs = new String[] { "-d", this.tempPath, fileName };
		if (jars1 != null) {
			cpargs = new String[] { "-classpath", jars1, "-d", this.tempPath, fileName };
		}
		int status = -1;
		try {
			status = com.sun.tools.javac.Main.compile(cpargs, pw);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		fin.delete();
		if (status != 0) {
			// compile failed
			throw new Exception(sw.toString());
		}

		// read output file
		String outFilePath = name;
		outFilePath = outFilePath.replace('.', '/');
		outFilePath = this.tempPath + outFilePath + ".class";
		//System.out.println(outFilePath);
		File fout = new File(outFilePath);
		long len = fout.length();
		byte[] data = new byte[(int) len];
		FileInputStream foutstream = new FileInputStream(fout);
		foutstream.read(data);
		foutstream.close();
		fout.delete();

		// save src/binary into database
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectManager.getConnection();
			// delete old one, and insert empty lobs
			String sql = "begin " + DELETE_SQL + ";" + INSERT_SQL
					+ "; commit; end;";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.setString(2, name);
			stmt.setString(3, imports1);
			stmt.setString(4, jars1);
			stmt.executeUpdate();
			stmt.close();
			stmt = null;
			
			// lock the record
			stmt = conn.prepareStatement(SELECT_SQL + " for update");
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			rs.next();
			
			// write lob data
			java.io.OutputStream os;
			os = rs.getBlob(1).setBinaryStream(0);
			os.write(src.getBytes("UTF-8"));
			os.close();
			os = rs.getBlob(2).setBinaryStream(0);
			os.write(data);
			os.close();
			conn.commit();

			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
			conn.close();
			conn = null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("�������ݿ�ʧ�ܡ�");
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					;
				}
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					;
				}
				stmt = null;
			}
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					;
				}
				try {
					conn.close();
				} catch (SQLException e) {
					;
				}
				conn = null;
			}
		}

		RuntimeClassLoader rcl = new RuntimeClassLoader(this.getClass().getClassLoader());
		Class cl = rcl.load(name, data);

		if (cl == null)
			throw new Exception("������" + name + "ʧ�ܡ�");
		map.put(name, cl);
		return name;
	}

	/**
	 * ��ȡ������Ӧ�Ķ����ƴ���
	 * @param name ָ��������
	 * @return ���ض�Ӧ�Ķ����ƴ���
	 */
	public byte[] getClassData(String name) {
		// save src/binary into database
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		byte[] data = null;
		try {
			conn = ConnectManager.getConnection();
			stmt = conn.prepareStatement(SELECT_SQL);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Blob blob = rs.getBlob(2);
				data = blob.getBytes(1, (int)blob.length());
			}
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
			conn.close();
			conn = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					;
				}
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					;
				}
				stmt = null;
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					;
				}
				conn = null;
			}
		}

		return data;
	}

	/**
	 * ��ȡ������Ӧ��Դ����
	 * @param name ָ��������
	 * @return ���ض�Ӧ��Դ����
	 */
	public String getClassSource(String name) {
		// save src/binary into database
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String src = null;

		try {
			conn = ConnectManager.getConnection();
			stmt = conn.prepareStatement(SELECT_SQL);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Blob blob = rs.getBlob(1);
				java.io.InputStream bs = blob.getBinaryStream();
				if (blob.length() > 0) {
					byte[] data = new byte[(int)blob.length()];
					bs.read(data);
					src = new String(data, "UTF-8");
				}
				bs.close();
			}
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
			conn.close();
			conn = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					;
				}
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					;
				}
				stmt = null;
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					;
				}
				conn = null;
			}
		}
		return src;
	}
}
