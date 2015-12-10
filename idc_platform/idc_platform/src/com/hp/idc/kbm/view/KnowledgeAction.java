/**   
 * @Title: knowledgeAction.java 
 * @Description: TODO
 * @date 2011-7-12 下午05:57:55   
 * @version 1.0  
 */
package com.hp.idc.kbm.view;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.ObjectRetrievalFailureException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.hp.idc.cas.auc.PersonInfo;
import com.hp.idc.cas.auc.PersonManager;
import com.hp.idc.common.Const;
import com.hp.idc.common.Constant;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.view.AbstractAction;
import com.hp.idc.common.util.DataCtrl;
import com.hp.idc.kbm.bo.TreeNode;
import com.hp.idc.kbm.entity.KbmCategory;
import com.hp.idc.kbm.entity.KbmKnowledge;
import com.hp.idc.kbm.service.KnowledgeService;
import com.hp.idc.system.security.entity.SysUser;
import com.opensymphony.xwork2.ActionContext;

/**
 * 知识库管理Action类
 * 
 * @author Fancy
 * @version 1.0, 9:55:54 PM Jul 14, 2011
 * 
 */
public class KnowledgeAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7500403480320587744L;

	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private KbmCategory kbcate;
	private KbmKnowledge kbknowledge;

	@Resource
	private KnowledgeService knowledgeService;
	private long id;
	private String knowledgeId;
	private String title;
	private String keywords;
	private String search;
	private int isRetired = 0;
	private boolean isleaf;
	private int baseType;
	private String ids;
	private String createdate;
	PersonManager personmanager;

	List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
	DataCtrl util = new DataCtrl();

	/**
	 * @function 进入知识库管理页面
	 * @return
	 */
	public String toKnowledgeManage() {
		return SUCCESS;
	}

	/**
	 * @function 查询知识列表
	 * @return
	 */
	public String queryKnowledge() {
		try {
			// String key = this.getRequest().getParameter("keywords");
			// 组装查询条件
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 知识类别
			if (id != Const.KBM_CATE_EVENT && id != Const.KBM_CATE_CASE) {
				paramMap.put("categoryId", Long.valueOf(id));
			} else if (id == Const.KBM_CATE_EVENT) {
				paramMap.put("cateId", Integer.valueOf(Const.KBM_BASETYPE_EVENT));
			} else if (id == Const.KBM_CATE_CASE) {
				paramMap.put("cateId", Integer.valueOf(Const.KBM_BASETYPE_CASE));
			}
			// 关键字
			if (search != null && !search.equals("")) {
				paramMap.put("search", search);
			}
			if (keywords != null && !keywords.equals("")) {
				paramMap.put("keywords", keywords);
			}
			if(knowledgeId!=null && !"".equals(knowledgeId)){
				paramMap.put("id", Long.parseLong(knowledgeId));
			}
			if(title!=null && !"".equals(title)){
				paramMap.put("title", title);
			}
			// 是否废弃,默认 0 ：有效
			paramMap.put("isRetired", isRetired);

			LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
			Page<KbmKnowledge> page = knowledgeService.queryKnowledgePage(
					paramMap, sortMap, start / limit + 1, limit);
			List<KbmKnowledge> list = page.getResult();

			/** 组装返回jsonObject */
			jsonObject = new JSONObject();
			jsonArray = new JSONArray();

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[] { "createDate", "updateDate",
					"categoryId", "creator", "updater" });
			// jsonConfig.registerJsonValueProcessor(java.util.Date.class,
			// new DateJsonValueProcessor("yyyy-MM-dd"));

			for (KbmKnowledge event : list) {
				JSONObject json = JSONObject.fromObject(event, jsonConfig);

				json.put("createDate", event.getCreateDate() == null ? "" : df
						.format(event.getCreateDate()).toString());// 创建日期
				json.put("updateDate", event.getUpdateDate() == null ? "" : df
						.format(event.getUpdateDate()).toString());// 修改日期
				json.put("cateId", event.getCategoryId().getId());// 知识分类id
				json.put("categoryId", event.getCategoryId().getCategoryName());// 知识分类名称
				json.put("creator", PersonManager.getPersonById(event
						.getCreator()) == null ? "" : PersonManager
						.getPersonById(event.getCreator()).getName());// 创建人姓名
				json.put("creatorId",
						event.getCreator() == null ? "" : event.getCreator());// 创建人id
				jsonArray.add(json);

			}
			jsonObject.put(KnowledgeAction.JSON_RESULT, jsonArray.toString());
			jsonObject.put(KnowledgeAction.JSON_TOTAL_COUNT,
					page.getTotalSize());
		} catch (Exception e) {
			logError("KnowledgeAction-queryKnowledge", e);
		}
		return SUCCESS;
	}

	/**
	 * @function 组装知识分类树
	 * @return category Tree node data
	 */
	public String loadCateTree() {

		jsonArray = new JSONArray();
		JsonConfig jsonConfig = new JsonConfig();
		List<KbmCategory> categoryList = null;
		List<TreeNode> basList = new ArrayList<TreeNode>();
		// List<Base> baseList;

		try {
			// 查询知识分类list
			categoryList = knowledgeService.loadCategoryList();
			// baseList = du.getBaseList();

			// 组装基础节点:故障知识库和案例知识库
			TreeNode gzNode = new TreeNode();
			TreeNode alNode = new TreeNode();
			gzNode.setId(Const.KBM_CATE_EVENT);// 待確定規則,確認實際值
			gzNode.setText("故障知识库");
			gzNode.setParent(Const.KBM_ROOT_ID);
			gzNode.setBaseType(Const.KBM_BASETYPE_EVENT);
			gzNode.setLeaf(false);
			basList.add(gzNode);

			alNode.setId(Const.KBM_CATE_CASE);
			alNode.setText("案例知识库");
			alNode.setParent(Const.KBM_ROOT_ID);
			alNode.setBaseType(Const.KBM_BASETYPE_CASE);
			alNode.setLeaf(false);
			basList.add(alNode);

			// 组装實際分类节点
			for (KbmCategory category : categoryList) {
				TreeNode tn = new TreeNode();
				tn.setId(category.getId());
				tn.setText(category.getCategoryName());
				tn.setDes(category.getCategoryName());
				tn.setBaseType(category.getBaseType());
				// 判断是否为第一层级有效分类
				if (category.getParentCategoryId() == -1) {
					// 判断是知识点 or 案例
					if (category.getBaseType() == Const.KBM_BASETYPE_EVENT) {
						tn.setParent(Const.KBM_CATE_EVENT);
					} else {
						tn.setParent(Const.KBM_CATE_CASE);
					}

				}

				else {
					tn.setParent(category.getParentCategoryId());
				}
				treeNodeList.add(tn);
			}

			for (TreeNode tn : basList) {
				tn.setChildren(loadChildren(tn));
				JSONObject json = JSONObject.fromObject(tn, jsonConfig);
				jsonArray.add(json);
			}

		} catch (Exception e) {
			logError("KnowledgeAction-loadCateTree", e);
		}
		return SUCCESS;
	}

	/**
	 * @function 组装每个节点的子节点
	 * @param self
	 * @return
	 */
	private List<TreeNode> loadChildren(TreeNode self) {
		List<TreeNode> children = new ArrayList<TreeNode>();
		for (TreeNode tn : treeNodeList) {
			if (tn.getParent() == self.getId()) {
				children.add(tn);
				tn.setChildren(loadChildren(tn));
				if (tn.getChildren() != null && tn.getChildren().size() > 0) {
					tn.setLeaf(false);
				} else {
					tn.setLeaf(true);
				}
			}
		}
		return children;
	}

	/**
	 * 新增编辑--保存知识点信息
	 * 
	 * @author Fancy
	 * @return
	 */
	public String saveKnowledge() {
		String retMsg = "";
		boolean ret = true;
		try {

			/** 登陸用戶人,待統一方法組裝 */
			// ActionContext ctx = ActionContext.getContext();
			// Map<String, Object> session = ctx.getSession();
			// String userid = (String)session.get(Constant.SESSION_LOGIN);
			// PersonInfo sysuser = PersonManager.getPersonById(userid);
			// SysUser user = new SysUser();
			// user.setId(10);
			/*********************/

			if (kbknowledge != null) {
				if (kbknowledge.getId() == 0) {
					// 新增操作时,组装创建人信息
					kbknowledge.setCreator(getLoginUserId());// 获取登录人ID
					kbknowledge.setCreateDate(util.findSystemDate());
				} else {
					// 编辑时处理日期数据
					kbknowledge.setCreateDate(Date.valueOf(createdate));
				}

				kbknowledge.setUpdater(getLoginUserId());
				kbknowledge.setUpdateDate(util.findSystemDate());

				// baseType =1 :知识点 , 2: 案例
				knowledgeService.saveKnowledge(baseType, kbknowledge);
				retMsg = Const.KBM_MSG_SAVE_KNOWLEDGE;
			}

		} catch (Exception e) {
			ret = false;
			retMsg = Const.KBM_MSG_ERROR;
			logError("KnowledgeAction-saveKnowledge", e);
		} finally {
			jsonObject = new JSONObject();
			jsonObject.put(KnowledgeAction.JSON_RET_SUCCESS, ret);
			jsonObject.put(KnowledgeAction.JSON_RET_MESSAGE, retMsg);
			jsonObject = JSONObject.fromObject(jsonObject);
		}

		return SUCCESS;
	}

	/**
	 * 保存案例分析
	 * 
	 * @author Fancy
	 * @return
	 */
	public String saveCase() {
		return SUCCESS;
	}

	/**
	 * 新增编辑-保存知识类别
	 * 
	 * @return
	 */
	public String saveCategory() {
		String retMsg = "";
		boolean ret = true;
		KbmCategory cate = null;
		try {
			cate = knowledgeService.saveKbmCategory(kbcate);
			// 组装返回弹出提示框信息
			if (kbcate.getBaseType() == Const.KBM_BASETYPE_EVENT) {
				retMsg = Const.KBM_MSG_SAVE_EVENT;
			} else if (kbcate.getBaseType() == Const.KBM_BASETYPE_CASE) {
				retMsg = Const.KBM_MSG_SAVE_CASE;
			}

		} catch (Exception e) {
			ret = false;
			retMsg = Const.KBM_MSG_ERROR;
			e.printStackTrace();
			// logError("KnowledgeAction-loadCateTree", e);
		} finally {
			jsonObject = new JSONObject();
			jsonObject.put(KnowledgeAction.JSON_RET_SUCCESS, ret);
			jsonObject.put(KnowledgeAction.JSON_RET_MESSAGE, retMsg);
			// jsonObject.put("path", "loadCateTree.action");
			String newPath = this.getRequest().getParameter("path") + '/'
					+ cate.getId();
			jsonObject.put("path", newPath);
			jsonObject = JSONObject.fromObject(jsonObject);
		}

		return SUCCESS;
	}

	/**
	 * 删除--删除知识分类树节点
	 * 
	 * @return
	 */
	public String deleteCategory() {

		String retMsg = "";
		boolean ret = true;

		try {
			// 判断该节点是否为叶子节点
			if (isleaf == false) {
				ret = false;
				retMsg = "该分类下有其它分类，无法删除!";
			}
			// 判断该分类下是否有明细
			else if (knowledgeService.findKnowledgeByParentId(id)) {
				ret = false;
				retMsg = "该分类下有对应明细，无法删除!";
			} else {
				// 根据id删除该分类
				try {
					knowledgeService.removeCategoryById(id);
				} catch (ObjectRetrievalFailureException ex) {
					ret = false;
					retMsg = "该分类已被删除，请更新列表!";
				}
				// //判断
				// if(knowledgeService.findCategoryById(id)){
				// // 根据id删除该分类
				// knowledgeService.removeCategoryById(id);
				// }

			}

		} catch (Exception e) {
			ret = false;
			retMsg = Const.KBM_MSG_ERROR;
			e.printStackTrace();
			// logError("KnowledgeAction-loadCateTree", e);
		} finally {
			this.setJSONResponse(ret, retMsg);
		}

		return SUCCESS;

	}

	/**
	 * 刪除--删除知识点明细
	 * 
	 * @return
	 */
	public String deleteKnowledge() {

		String retMsg = null;
		boolean ret = true;

		try {
			String[] idArry = ids.split(",");
			if (idArry != null && idArry.length > 0) {
				for (String knowledgeId : idArry) {
					// 根据id删除该知识点(逻辑删除,置是否废弃=1)
					knowledgeService.removeKnowledgeById(Long
							.valueOf(knowledgeId));
				}
			}
			retMsg = Const.KBM_MSG_DEL_SUCCESS;
		} catch (Exception e) {
			ret = false;
			retMsg = Const.KBM_MSG_DEL_ERROR;
			logError("KnowledgeAction-deleteKnowledge", e);
		} finally {
			this.setJSONResponse(ret, retMsg);
		}

		return SUCCESS;

	}

	/**
	 * 根据编号载入一条知识点信息(未使用)
	 * 
	 * @author Fancy
	 * @return
	 */
	public String loadKnowledge() {
		boolean ret = true;
		try {
			jsonObject = new JSONObject();
			jsonArray = new JSONArray();

			kbknowledge = knowledgeService.findKnowledgeById(id);

			JsonConfig jsonConfig = new JsonConfig();
			// jsonConfig.setExcludes(new String[] { "createDate", "updateDate",
			// "categoryId", "creator", "updater" });

			jsonObject = JSONObject.fromObject(kbknowledge);
			// // 组装日期数据
			// json.put("createDate", kbknowledge.getCreateDate().toString());
			// json.put("updateDate", kbknowledge.getUpdateDate().toString());
			// json.put("categoryId", kbknowledge.getCategoryId()
			// .getCategoryName());
			// json.put("creator", kbknowledge.getCreator().getActualName());

			// jsonObject.put(KnowledgeAction.JSON_RESULT,
			// jsonArray.toString());
			// this.setJSONResponse(ret);
		} catch (Exception e) {
			logError("KnowledgeAction-queryKnowledge", e);
		}
		return SUCCESS;
	}

	public KbmCategory getKbcate() {
		return kbcate;
	}

	public void setKbcate(KbmCategory kbcate) {
		this.kbcate = kbcate;
	}

	public KbmKnowledge getKbknowledge() {
		return kbknowledge;
	}

	public void setKbknowledge(KbmKnowledge kbknowledge) {
		this.kbknowledge = kbknowledge;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKeywords() {
		return keywords == null ? "" : keywords.toString();
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public int getIsRetired() {
		return isRetired;
	}

	public void setIsRetired(int isRetired) {
		this.isRetired = isRetired;
	}

	public boolean isIsleaf() {
		return isleaf;
	}

	public void setIsleaf(boolean isleaf) {
		this.isleaf = isleaf;
	}

	public int getBaseType() {
		return baseType;
	}

	public void setBaseType(int baseType) {
		this.baseType = baseType;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	@Required
	public KnowledgeService getKnowledgeService() {
		return knowledgeService;
	}

	public void setKnowledgeService(KnowledgeService knowledgeService) {
		this.knowledgeService = knowledgeService;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(String knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}



}