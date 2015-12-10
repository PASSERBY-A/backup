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
 * 动态代码类
 * @author 梅园
 */
public class Runtime {

	/**
	 * 存放 类名->Class对象 的表
	 */
	static protected Map map = new HashMap();

	/**
	 * 临时文件存放的路径
	 */
	protected String tempPath = ".";

	/**
	 * 插入时所用的 SQL 语句
	 */
	protected static final String INSERT_SQL = "insert into ITSM_CFG_DYNCODE(dc_classname, dc_source, dc_binary, dc_imports, dc_jars) values( ?, empty_blob(), empty_blob(), ?, ?)";

	/**
	 * 删除时所用的 SQL 语句
	 */
	protected static final String DELETE_SQL = "delete from ITSM_CFG_DYNCODE where dc_classname=?";

	/**
	 * 查询时所用的 SQL 语句
	 */
	protected static final String SELECT_SQL = "select dc_source, dc_binary from ITSM_CFG_DYNCODE where dc_classname=?";

	/**
	 * 用指定的临时路径和指定引用 jar 包的路径构造新的 Runtime 对象
	 * @param path 临时文件的生成路径
	 */
	public Runtime(String path) {
		this.tempPath = path;
		if (!this.tempPath.endsWith("/") && !this.tempPath.endsWith("\\"))
			this.tempPath = this.tempPath + "/";
	}
	
	/**
	 * 构造新的 Runtime 对象
	 */
	public Runtime() {
		// Nothing to do here
	}

	/**
	 * 根据类名查找类的 Class 
	 * @param name 指定的类名
	 * @return 返回相应的 Class
	 * @throws ClassNotFoundException 找不到相应的类时抛出 ClassNotFoundException 异常
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
	 * 编译 java 代码，并将编译结果入库保存
	 * @param imports0 引用的包
	 * @param jars0 导入的包,以";"分隔
	 * @param src java 源文件的内容
	 * @return 编译成功时返回编辑好的类名
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
			throw new IOException("更新数据库失败。");
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
			throw new Exception("加载类" + name + "失败。");
		map.put(name, cl);
		return name;
	}

	/**
	 * 获取类名对应的二进制代码
	 * @param name 指定的类名
	 * @return 返回对应的二进制代码
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
	 * 获取类名对应的源代码
	 * @param name 指定的类名
	 * @return 返回对应的源代码
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
