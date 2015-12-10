package com.hp.idc.portal.mgr;

import java.beans.PropertyVetoException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.LobHandler;

import com.hp.idc.portal.bean.*;
import com.hp.idc.portal.util.DBUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ViewNodeMgr {
	private static String SELECT_BY_OID = "SELECT * FROM tf_pt_viewnode WHERE oid = ? order by oid desc";
	private static String INSERT_REGULAR = "insert into tf_pt_viewnode (oid,name,backcolor,forecolor,width,height,advprop,creator,createtime,path,type) values (TF_PT_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?)";
	private static String UPDATE_REGULAR = "update tf_pt_viewnode set name = ?,backcolor = ?,forecolor = ?,width = ?,height = ?,advprop = ?,creator = ?,createtime = ?,path = ?,type = ? where oid = ?";
	private static String DELETE_REGULAR = "delete from tf_pt_viewnode where oid = ?";

	/**
	 * 添加
	 * 
	 * @param bean
	 * @return
	 */
	public boolean add(final ViewNode bean) {
		boolean isSuccess = false;
		int ret = DBUtil.getJdbcTemplate().update(INSERT_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				// oid,name,backcolor,forecolor,width,height,advprop,role,creator,createtime,path,type
				ps.setString(1, bean.getName());
				ps.setString(2, bean.getBackColor());
				ps.setString(3, bean.getForeColor());
				ps.setString(4, bean.getWidth());
				ps.setString(5, bean.getHeight());
				LobHandler lobHandler = DBUtil.getLobHandler();
				lobHandler.getLobCreator().setClobAsString(ps, 6, bean.getAdvProp());
				ps.setInt(7, bean.getCreator());
				ps.setTimestamp(8, new Timestamp(bean.getCreateTime().getTime()));
				ps.setString(9, bean.getPath());
				ps.setString(10, bean.getType());
			}
		});
		if (ret > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 修改
	 * 
	 * @param bean
	 * @return
	 */
	public boolean update(final ViewNode bean) {
		boolean isSuccess = false;
		int ret = DBUtil.getJdbcTemplate().update(UPDATE_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, bean.getName());
				ps.setString(2, bean.getBackColor());
				ps.setString(3, bean.getForeColor());
				ps.setString(4, bean.getWidth());
				ps.setString(5, bean.getHeight());
				LobHandler lobHandler = DBUtil.getLobHandler();
				lobHandler.getLobCreator().setClobAsString(ps, 6, bean.getAdvProp());
				ps.setInt(7, bean.getCreator());
				ps.setTimestamp(8, new Timestamp(bean.getCreateTime().getTime()));
				ps.setString(9, bean.getPath());
				ps.setString(10, bean.getType());
				ps.setString(11, bean.getOid());
			}
		});
		if (ret > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 删除
	 * 
	 * @param oid
	 * @return
	 */
	public boolean delete(final String oid) {
		boolean isSuccess = false;
		int ret = DBUtil.getJdbcTemplate().update(DELETE_REGULAR, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, oid);
			}
		});
		if (ret > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 根据oid获取Node
	 * 
	 * @param oid
	 * @return
	 */
	public ViewNode getBeanById(String oid) {
		ViewNode ret = DBUtil.getJdbcTemplate().query(SELECT_BY_OID, new Object[] { oid },
				new ResultSetExtractor<ViewNode>() {
					public ViewNode extractData(ResultSet rs) throws SQLException, DataAccessException {
						ViewNode temp = new ViewNode();
						if (rs.next()) {
							// //oid,name,backcolor,forecolor,width,height,advprop,role,creator,createtime,path,type
							temp.setOid(rs.getString("oid"));
							temp.setName(rs.getString("name"));
							temp.setBackColor(rs.getString("backcolor"));
							temp.setForeColor(rs.getString("forecolor"));
							temp.setWidth(rs.getString("width"));
							temp.setHeight(rs.getString("height"));
							LobHandler lobHandler = DBUtil.getLobHandler();
							temp.setAdvProp(lobHandler.getClobAsString(rs, "advprop"));
							temp.setCreator(rs.getInt("creator"));
							temp.setCreateTime(rs.getTimestamp("createtime"));
							temp.setPath(rs.getString("path"));
							temp.setType(rs.getString("type"));
							return temp;
						}
						return null;
					}
				});
		return ret;
	}

	/**
	 * 根据所有Node
	 * 
	 * @param userId
	 * @return
	 */
	public List<ViewNode> getBeans() {
		String sql = "SELECT * FROM tf_pt_viewnode order by oid desc";
		List<ViewNode> list = DBUtil.getJdbcTemplate().query(sql, new Object[] {}, new NodeRowMapper());
		return list;
	}

	/**
	 * 根据用户ID查询所有Node
	 * 
	 * @param userId
	 *            用户名
	 * @param type
	 *            菜单类型
	 * @return
	 */
	public List<ViewNode> getBeanByFilter(int userId, String type) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tf_pt_viewnode t where t.creator = ?");
		sql.append(" order by t.oid asc");
		List<ViewNode> list = DBUtil.getJdbcTemplate()
				.query(sql.toString(), new Object[] { type }, new NodeRowMapper());
		return list;
	}

	private class NodeRowMapper implements RowMapper<ViewNode> {
		public ViewNode mapRow(ResultSet rs, int rowNum) throws SQLException {
			ViewNode temp = new ViewNode();

			temp.setOid(rs.getString("oid"));
			temp.setName(rs.getString("name"));
			temp.setBackColor(rs.getString("backcolor"));
			temp.setForeColor(rs.getString("forecolor"));
			temp.setWidth(rs.getString("width"));
			temp.setHeight(rs.getString("height"));
			LobHandler lobHandler = DBUtil.getLobHandler();
			temp.setAdvProp(lobHandler.getClobAsString(rs, "advprop"));
			temp.setCreator(rs.getInt("creator"));
			temp.setCreateTime(rs.getTimestamp("createtime"));
			temp.setPath(rs.getString("path"));
			temp.setType(rs.getString("type"));
			return temp;
		}
	}

	/**
	 * 解析附加属性，放入map中
	 * 
	 * @param xml
	 * @return
	 */
	public Map<String, String> parseNodeAdvProp(String xml) {
		Document d = null;
		try {
			d = DocumentHelper.parseText(xml);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		Element e = d.getRootElement();
		List<?> list = e.selectNodes("propertie");
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			Element element = (Element) list.get(i);
			String key = element.attributeValue("id");
			String val = element.attributeValue("value");
			map.put(key, val);
		}
		return map;
	}

	/**
	 * Single Series Charts 数据查询
	 * map中需有driverClass，jdbcUrl，user，password，sql五个值
	 * @param map
	 * @return
	 * @throws PropertyVetoException
	 */
	public static Map<String, String> geSingleSeriesChartsData(Map<String, String> map) throws PropertyVetoException {
		String driverClass = map.get("driverClass") == null ? "oracle.jdbc.driver.OracleDriver" : map.get("driverClass");
		String jdbcUrl = map.get("jdbcUrl");
		String user = map.get("user");
		String password = map.get("password");
		String sql = map.get("sql");
		if (jdbcUrl == null || user == null || password == null || sql == null)
			return null;
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(driverClass);
		dataSource.setJdbcUrl(jdbcUrl);
		dataSource.setUser(user);
		dataSource.setPassword(password);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		final Map<String, String> ret = new HashMap<String, String>();
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				ret.put(rs.getString(1), rs.getString(2));
			}
		});

		return ret;
	}
	
	/**
	 * Column 2D Chart
	 * @param map key-value : label-value
	 * @param chartMap chart节点里的属性
	 * @return
	 */
	public static String getColumn2DXML(Map<String, String> map,Map<String, String> chartMap){
		String result = null;
		
		Document document = DocumentHelper.createDocument();
		// 生成根结点chart
		Element chart = document.addElement("chart");
		// 生成根节点的属性
		if(null!=chartMap.get("caption")&&!"".equals(chartMap.get("caption")))
			chart.addAttribute("caption", chartMap.get("caption"));
		if(null!=chartMap.get("xAxisName")&&!"".equals(chartMap.get("xAxisName")))
			chart.addAttribute("xAxisName", chartMap.get("xAxisName"));
		if(null!=chartMap.get("yAxisName")&&!"".equals(chartMap.get("yAxisName")))
			chart.addAttribute("yAxisName", chartMap.get("yAxisName"));
		chart.addAttribute("baseFontSize", "12");
		chart.addAttribute("showValues", "0");
		chart.addAttribute("palette", "4");
		chart.addAttribute("useRoundEdges", "1");
		// 生成set结点
		Element set = null;
		for(Iterator<Entry<String, String>> iter=map.entrySet().iterator(); iter.hasNext();){
			Entry<String, String> entry = iter.next();
			set = chart.addElement("set");
			set.addAttribute("label", entry.getKey());
			set.addAttribute("value", entry.getValue());
		}
		
		if(null!=chartMap.get("trendLines")&&!"".equals(chartMap.get("trendLines"))){
			Element trendLines = chart.addElement("trendLines");
			Element line = trendLines.addElement("line");
			line.addAttribute("startValue", chartMap.get("trendLines"));
			line.addAttribute("color", "#009933");
			line.addAttribute("displayvalue", "趋势线");
		}
		result = document.asXML().replaceAll("\n", "");
		result = result.replaceAll("\"", "'");
		return result;
	}
	
	/**
	 * Pie 2D Chart
	 * @param map key-value : label-value
	 * @param chartMap chart节点里的属性
	 * @return
	 */
	public static String getPie2DXML(Map<String, String> map,Map<String, String> chartMap){
		String result = null;
		
		Document document = DocumentHelper.createDocument();
		// 生成根结点chart
		Element chart = document.addElement("chart");
		// 生成根节点的属性
		if(null!=chartMap.get("caption")&&!"".equals(chartMap.get("caption")))
			chart.addAttribute("caption", chartMap.get("caption"));
		chart.addAttribute("baseFontSize", "12");
		chart.addAttribute("showValues", "0");
		chart.addAttribute("showLabels", "1");
		chart.addAttribute("showLegend", "1");
		chart.addAttribute("showPercentageInLabel", "1");
		// 生成set结点
		Element set = null;
		for(Iterator<Entry<String, String>> iter=map.entrySet().iterator(); iter.hasNext();){
			Entry<String, String> entry = iter.next();
			set = chart.addElement("set");
			set.addAttribute("label", entry.getKey());
			set.addAttribute("value", entry.getValue());
		}
		result = document.asXML().replaceAll("\n", "");
		result = result.replaceAll("\"", "'");
		return result;
	}
	
	/**
	 * Multi-Series Charts 数据查询
	 * map中需有driverClass，jdbcUrl，user，password，sql五个值
	 * @param map
	 * @return
	 * @throws PropertyVetoException
	 */
	public static Map<String, Map<String,String>> geMultiSeriesChartsData(Map<String, String> map) throws PropertyVetoException {
		String driverClass = map.get("driverClass") == null ? "oracle.jdbc.driver.OracleDriver" : map.get("driverClass");
		String jdbcUrl = map.get("jdbcUrl");
		String user = map.get("user");
		String password = map.get("password");
		String sql = map.get("sql");
		if (jdbcUrl == null || user == null || password == null || sql == null)
			return null;
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(driverClass);
		dataSource.setJdbcUrl(jdbcUrl);
		dataSource.setUser(user);
		dataSource.setPassword(password);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		final Map<String, Map<String,String>> ret = new HashMap<String, Map<String,String>>();
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				String category = rs.getString("category");
				String seriesName = rs.getString("seriesName");
				String value = rs.getString("value");
				Map<String,String> tmp = ret.get(category);
				if(tmp==null){
					tmp = new HashMap<String, String>();
				}
				tmp.put(seriesName, value);
				ret.put(category, tmp);
			}
		});

		return ret;
	}
	
	/**
	 * Multi Column 2D
	 * @param map key-<key,value> : category-<seriesName,value>
	 * @param chartMap chart节点里的属性
	 * @return
	 */
	public static String getMultiColumn2DXML(Map<String, Map<String,String>> map,Map<String, String> chartMap){
		String result = null;
		
		Document document = DocumentHelper.createDocument();
		// 生成根结点chart
		Element chart = document.addElement("chart");
		// 生成根节点的属性
		if(null!=chartMap.get("caption")&&!"".equals(chartMap.get("caption")))
			chart.addAttribute("caption", chartMap.get("caption"));
		chart.addAttribute("baseFontSize", "12");
		chart.addAttribute("shownames", "1");
		chart.addAttribute("showvalues", "0");
		chart.addAttribute("decimals", "0");
		chart.addAttribute("useRoundEdges", "1");
		chart.addAttribute("legendBorderAlpha", "0");
		List<String> categoryList = new ArrayList<String>();
		Set<String> seriesNameSet = new HashSet<String>();
		
		// 生成categories结点
		Element categorys = chart.addElement("categories");
		for(Iterator<Entry<String, Map<String,String>>> iter=map.entrySet().iterator(); iter.hasNext();){
			Entry<String, Map<String,String>> entry = iter.next();
			Element category = categorys.addElement("category");
			category.addAttribute("label", entry.getKey());
			categoryList.add(entry.getKey());
			for(Iterator<Entry<String,String>> iter1=entry.getValue().entrySet().iterator(); iter1.hasNext();){
				Entry<String,String> entry1 = iter1.next();
				seriesNameSet.add(entry1.getKey());
			}
		}
		
		for(Iterator<String> iter = seriesNameSet.iterator();iter.hasNext();){
			Element dataset = chart.addElement("dataset");
			String seriesName = iter.next();
			dataset.addAttribute("seriesName", seriesName);
			dataset.addAttribute("showValues", "0");
			for(String category : categoryList){
				Element set = dataset.addElement("set");
				String value = map.get(category).get(seriesName);
				if(value==null||value.equals("")){
					value = "0";
				}
				set.addAttribute("value", value);
			}
		}
		
		result = document.asXML().replaceAll("\n", "");
		result = result.replaceAll("\"", "'");
		return result;
	}
}
