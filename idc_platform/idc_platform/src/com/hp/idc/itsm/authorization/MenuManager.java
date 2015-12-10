package com.hp.idc.itsm.authorization;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.hp.idc.context.util.ContextUtil;
import com.hp.idc.itsm.ci.CIManager;
import com.hp.idc.itsm.ci.CodeInfo;
import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.configure.ModuleName;
import com.hp.idc.itsm.configure.ViewInfo;
import com.hp.idc.itsm.configure.ViewManager;
import com.hp.idc.itsm.dbo.ColumnData;
import com.hp.idc.itsm.dbo.OracleOperation;
import com.hp.idc.itsm.dsm.DSMCenter;
import com.hp.idc.itsm.security.RuleManager;

/**
 * �˵������࣬������
 * 1���������ý����Ĳ˵�
 * 2������ȥ��ȡ�ģ�������ͼ��������ͼ�����Ǳ����ģ�
 * 3������ģ��ע������ģ����籨��Ϊ���Ա���ķ��������ԭ�д������뿪����
 * @author FluteD
 *
 */
public class MenuManager {
	
	public static void loadMenus() throws SQLException{
		System.out.println("loading menus...");
		Cache.Menus = new HashMap();

		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			String sql = "select * from ITSM_CFG_MENUS";
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				MenuInfo mi = new MenuInfo();
				mi.setId(rs.getString("ID"));
				mi.setName(rs.getString("NAME"));
				mi.setParentId(rs.getString("PARENT_ID"));
				mi.setDisplayIndex(rs.getInt("DISPLAY_INDEX"));
				mi.setRuleStr(rs.getString("RULE"));
				mi.setLeaf(new Boolean(rs.getString("LEAF")).booleanValue());
				mi.setHref(rs.getString("HREF"));
				mi.setScript(rs.getString("SCRIPT"));
				
				Cache.Menus.put(mi.getId(), mi);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}
	
	public static void reloadMenu(String id) throws SQLException{

		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			String sql = "select * from ITSM_CFG_MENUS WHERE ID=?";
			ps = u.getSelectStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				MenuInfo mi = new MenuInfo();
				mi.setId(rs.getString("ID"));
				mi.setName(rs.getString("NAME"));
				mi.setParentId(rs.getString("PARENT_ID"));
				mi.setDisplayIndex(rs.getInt("DISPLAY_INDEX"));
				mi.setRuleStr(rs.getString("RULE"));
				mi.setLeaf(new Boolean(rs.getString("LEAF")).booleanValue());
				mi.setHref(rs.getString("HREF"));
				mi.setScript(rs.getString("SCRIPT"));
				
				Cache.Menus.put(mi.getId(), mi);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}
	
	public static MenuInfo getMenuInfo(String menuId){
		Object ret = Cache.Menus.get(menuId);
		return ret == null?null:(MenuInfo)ret;
	}
	
	/**
	 * ��ȡģ��˵���û�и����ģ�
	 * @return
	 */
	public static List getAllMenus(){
		List retList = new ArrayList();
		retList.addAll(Cache.Menus.values());
		orderMenus(retList);
		return retList;
	}
	
	/**
	 * ��ȡ�û��Ĳ˵�
	 * @param userId
	 * @return
	 */
	public static List getMenusOfUser(String userId){
		List retList = new ArrayList();
		retList.addAll(getOtherMenus(userId));
		Collection vals = Cache.Menus.values();
		for (Iterator ite = vals.iterator();ite.hasNext();){
			MenuInfo mi = (MenuInfo)ite.next();
			if (RuleManager.valid(userId, mi.getRuleStr(), true)){
				retList.add(mi.getClone());
			}
		}
		retList = rebuildMenuTree(retList,"");
		return retList;
	}
	
	private static List rebuildMenuTree(List menuList,String parentId){
		List retList = new ArrayList();
		for (Iterator ite = menuList.iterator();ite.hasNext();){
			MenuInfo mi = (MenuInfo)ite.next();
			if (mi.getParentId().equals(parentId)){
				if (!mi.isLeaf()){
					List subList =  new ArrayList();
					subList.addAll(mi.getSubMenus());
					subList.addAll(rebuildMenuTree(menuList,mi.getId()));
					mi.setSubMenus(subList);
				}
				retList.add(mi);
			}
		}
		orderMenus(retList);
		return retList;
	}
	
	/**
	 * ��ȡ�ڵ������е��ӽڵ�
	 * @param parentId ��ID
	 * @return
	 */
	public static List getSubMenus(String parentId){
		List retList = new ArrayList();
		Collection vals = Cache.Menus.values();
		for (Iterator ite = vals.iterator();ite.hasNext();){
			MenuInfo mi = (MenuInfo)ite.next();
			if (mi.getParentId().equals(parentId)){
				retList.add(mi);
			}
		}
		return retList;
	}
	
	/**
	 * ���Ӳ˵���
	 * @param mi
	 * @param operName
	 * @throws Exception
	 */
	public static void addMenu(MenuInfo mi,String operName) throws Exception{
		if (Cache.Menus.get(mi.getId())!=null)
			throw new Exception("ID�ظ�");
		
		ResultSet rs = null;
		OracleOperation u = new OracleOperation("ITSM_CFG_MENUS", operName);

		try {
			u.setColumnData("ID", new ColumnData(ColumnData.TYPE_STRING,
					mi.getId()));
			u.setColumnData("NAME", new ColumnData(ColumnData.TYPE_STRING,
					mi.getName()));
			u.setColumnData("PARENT_ID", new ColumnData(ColumnData.TYPE_STRING,
					mi.getParentId()));
			u.setColumnData("DISPLAY_INDEX", new ColumnData(ColumnData.TYPE_INTEGER,
					new Integer(mi.getDisplayIndex())));
			u.setColumnData("RULE", new ColumnData(ColumnData.TYPE_STRING,
					mi.getRuleStr()));
			u.setColumnData("LEAF", new ColumnData(ColumnData.TYPE_STRING,
					mi.isLeaf()+""));
			u.setColumnData("HREF", new ColumnData(ColumnData.TYPE_STRING,
					mi.getHref()));
			u.setColumnData("SCRIPT", new ColumnData(ColumnData.TYPE_STRING,
					mi.getScript()));

			rs = u.getResultSet(null);
			u.executeInsert(rs);
			
		} catch (Exception e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		Cache.Menus.put(mi.getId(), mi);
		DSMCenter dsmc = (DSMCenter)ContextUtil.getBean("DSMCenter");
		dsmc.publishEvent("MENU", mi.getId());
	}
	
	/**
	 * ���²˵���
	 * @param mi
	 * @param operName
	 * @throws Exception
	 */
	public static void updateMenu(MenuInfo mi,String operName) throws Exception{
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation("ITSM_CFG_MENUS", operName);

		try {
			u.setColumnData("NAME", new ColumnData(ColumnData.TYPE_STRING,
					mi.getName()));
			u.setColumnData("PARENT_ID", new ColumnData(ColumnData.TYPE_STRING,
					mi.getParentId()));
			u.setColumnData("DISPLAY_INDEX", new ColumnData(ColumnData.TYPE_INTEGER,
					new Integer(mi.getDisplayIndex())));
			u.setColumnData("RULE", new ColumnData(ColumnData.TYPE_STRING,
					mi.getRuleStr()));
			u.setColumnData("LEAF", new ColumnData(ColumnData.TYPE_STRING,
					mi.isLeaf()+""));
			u.setColumnData("HREF", new ColumnData(ColumnData.TYPE_STRING,
					mi.getHref()));
			u.setColumnData("SCRIPT", new ColumnData(ColumnData.TYPE_STRING,
					mi.getScript()));

			ps = u.getStatement("ID=?");
			ps.setString(1, mi.getId());
			rs = ps.executeQuery();
			u.executeUpdate(rs);
			
		} catch (Exception e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		Cache.Menus.put(mi.getId(), mi);
		DSMCenter dsmc = (DSMCenter)ContextUtil.getBean("DSMCenter");
		dsmc.publishEvent("MENU", mi.getId());
	}
	
	public static void deleteMenu(String menuId,String operName) throws Exception{
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation("ITSM_CFG_MENUS", operName);
		try {
			ps = u.getStatement("ID=?");
			ps.setString(1, menuId);
			rs = ps.executeQuery();
			u.executeDelete(rs);
		} catch (Exception e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}
	
	/**
	 * �Բ˵��б���displayIndex��С�����������
	 * @param ml
	 */
	public static void orderMenus(List miList){
		for (int i = 0; i < miList.size()-1; i++){
			for (int j = 1; j < miList.size() - i; j++){
				MenuInfo m1 = (MenuInfo)miList.get(j-1);
				MenuInfo m2 = (MenuInfo)miList.get(j);
				if (m1.getDisplayIndex() > m2.getDisplayIndex()){
					miList.remove(j);
					miList.add(j-1,m2);
				}
			}
		}
	}
	
	/**
	 * ��ȡ�����˵���������ͼ�ģ���ͼ���ò��ڲ˵����ã�������˵��������������������û�У��������У�
	 * @return
	 */
	private static List getOtherMenus(String userId){
		List retList = new ArrayList();
		retList.addAll(getViewMenus(userId,"task"));
		return retList;
	}
	
	private static List getViewMenus(String userId,String pMenuId){
		List retList = new ArrayList();
		List modules = CIManager.getCodesByTypeOid(Consts.CODETYPE_MODULE);
		if (modules!=null)
		for (int i = 0; i < modules.size(); i++) {
			CodeInfo cc = (CodeInfo)modules.get(i);
			List viewML2 = new ArrayList();;
			try {
				viewML2 = ViewManager.getViewsOfModule(cc.getOid()+"",false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			List viewML = new ArrayList();
			for (int j = 0; j < viewML2.size(); j++) {
				ViewInfo vInfo = (ViewInfo)viewML2.get(j);
				if (RuleManager.valid(userId,vInfo.getRule(),true)) {
					viewML.add(vInfo);
				} else {
					if (userId.equals(vInfo.getAttribute("create_by")))
						viewML.add(vInfo);
				}
			}
			//����displayNo����ͼ������Ŵ����ǰ��
			for (int j = viewML.size()-1; j >0; j--) {
				for (int m = 1; m <= j;m++) {
					ViewInfo vInfo1 = (ViewInfo)viewML.get(m-1);
					ViewInfo vInfo2 = (ViewInfo)viewML.get(m);
					String displayNo1 = vInfo1.getAttribute("displayNo");
					String displayNo2 = vInfo2.getAttribute("displayNo");

					if (displayNo1.equals(""))
						displayNo1 = "-1";
					if (displayNo2.equals(""))
						displayNo2 = "-1";
					if (Integer.parseInt(displayNo1)<Integer.parseInt(displayNo2)) {
						viewML.remove(m);
						viewML.add(m-1,vInfo2);
					}
				}
			}
			
			if (viewML.size()==0)
				continue;
			
			MenuInfo moduleMenu = new MenuInfo();
			if (cc.getOid() != ModuleName.ALL){
				moduleMenu.setId(cc.getOid()+"");
				moduleMenu.setName(cc.getName());
				moduleMenu.setParentId(pMenuId);
				moduleMenu.setLeaf(false);
				retList.add(moduleMenu);
			}
			for (int j = 0; j < viewML.size(); j++) {
				ViewInfo vInfo = (ViewInfo)viewML.get(j);
				String beforeDay = vInfo.getAttribute("beforeDayNo");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String defaultBegin = "";
				if (beforeDay != null && !beforeDay.equals("")) {
					long before = (long)24000*3600*Integer.parseInt(beforeDay);
					defaultBegin = sdf.format(new java.util.Date(System.currentTimeMillis()-before));
				}
				String defaultEnd = sdf.format(new java.util.Date(System.currentTimeMillis()));
				String defaultDisplay = "";
				if (!defaultBegin.equals(""))
					defaultDisplay = "   (Ĭ��:"+defaultBegin+"~"+defaultEnd+")";
				
				MenuInfo viewMenu = new MenuInfo();
				viewMenu.setId("view_"+vInfo.getOid());
				viewMenu.setName(vInfo.getName());
				viewMenu.setDisplayText(defaultDisplay);
				viewMenu.setLeaf(true);
				String url = "";
				if (vInfo.getType() == ViewInfo.TYPE_STANDARD)
					url = Consts.ITSM_HOME+"/configure/view/viewTemplate.jsp?viewOid="+vInfo.getOid();
				else if (vInfo.getType() == ViewInfo.TYPE_LINKED)
					url = vInfo.getUrl();
				viewMenu.setHref(url);
				viewMenu.setScript(vInfo.getScript());
				if (cc.getOid() != ModuleName.ALL)
					//viewMenu.setParentId(cc.getOid()+"");
					moduleMenu.addSubMenus(viewMenu);
				else {
					viewMenu.setParentId(pMenuId);
					retList.add(viewMenu);
				}

			}
			
		}
		return retList;
	}
	
}
