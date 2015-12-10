/**   
 * @Title: knowledgeAction.java 
 * @Description: TODO
 * @date 2011-7-12 ����05:57:55   
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
 * ֪ʶ�����Action��
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
	 * @function ����֪ʶ�����ҳ��
	 * @return
	 */
	public String toKnowledgeManage() {
		return SUCCESS;
	}

	/**
	 * @function ��ѯ֪ʶ�б�
	 * @return
	 */
	public String queryKnowledge() {
		try {
			// String key = this.getRequest().getParameter("keywords");
			// ��װ��ѯ����
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// ֪ʶ���
			if (id != Const.KBM_CATE_EVENT && id != Const.KBM_CATE_CASE) {
				paramMap.put("categoryId", Long.valueOf(id));
			} else if (id == Const.KBM_CATE_EVENT) {
				paramMap.put("cateId", Integer.valueOf(Const.KBM_BASETYPE_EVENT));
			} else if (id == Const.KBM_CATE_CASE) {
				paramMap.put("cateId", Integer.valueOf(Const.KBM_BASETYPE_CASE));
			}
			// �ؼ���
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
			// �Ƿ����,Ĭ�� 0 ����Ч
			paramMap.put("isRetired", isRetired);

			LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
			Page<KbmKnowledge> page = knowledgeService.queryKnowledgePage(
					paramMap, sortMap, start / limit + 1, limit);
			List<KbmKnowledge> list = page.getResult();

			/** ��װ����jsonObject */
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
						.format(event.getCreateDate()).toString());// ��������
				json.put("updateDate", event.getUpdateDate() == null ? "" : df
						.format(event.getUpdateDate()).toString());// �޸�����
				json.put("cateId", event.getCategoryId().getId());// ֪ʶ����id
				json.put("categoryId", event.getCategoryId().getCategoryName());// ֪ʶ��������
				json.put("creator", PersonManager.getPersonById(event
						.getCreator()) == null ? "" : PersonManager
						.getPersonById(event.getCreator()).getName());// ����������
				json.put("creatorId",
						event.getCreator() == null ? "" : event.getCreator());// ������id
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
	 * @function ��װ֪ʶ������
	 * @return category Tree node data
	 */
	public String loadCateTree() {

		jsonArray = new JSONArray();
		JsonConfig jsonConfig = new JsonConfig();
		List<KbmCategory> categoryList = null;
		List<TreeNode> basList = new ArrayList<TreeNode>();
		// List<Base> baseList;

		try {
			// ��ѯ֪ʶ����list
			categoryList = knowledgeService.loadCategoryList();
			// baseList = du.getBaseList();

			// ��װ�����ڵ�:����֪ʶ��Ͱ���֪ʶ��
			TreeNode gzNode = new TreeNode();
			TreeNode alNode = new TreeNode();
			gzNode.setId(Const.KBM_CATE_EVENT);// ���_��Ҏ�t,�_�J���Hֵ
			gzNode.setText("����֪ʶ��");
			gzNode.setParent(Const.KBM_ROOT_ID);
			gzNode.setBaseType(Const.KBM_BASETYPE_EVENT);
			gzNode.setLeaf(false);
			basList.add(gzNode);

			alNode.setId(Const.KBM_CATE_CASE);
			alNode.setText("����֪ʶ��");
			alNode.setParent(Const.KBM_ROOT_ID);
			alNode.setBaseType(Const.KBM_BASETYPE_CASE);
			alNode.setLeaf(false);
			basList.add(alNode);

			// ��װ���H����ڵ�
			for (KbmCategory category : categoryList) {
				TreeNode tn = new TreeNode();
				tn.setId(category.getId());
				tn.setText(category.getCategoryName());
				tn.setDes(category.getCategoryName());
				tn.setBaseType(category.getBaseType());
				// �ж��Ƿ�Ϊ��һ�㼶��Ч����
				if (category.getParentCategoryId() == -1) {
					// �ж���֪ʶ�� or ����
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
	 * @function ��װÿ���ڵ���ӽڵ�
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
	 * �����༭--����֪ʶ����Ϣ
	 * 
	 * @author Fancy
	 * @return
	 */
	public String saveKnowledge() {
		String retMsg = "";
		boolean ret = true;
		try {

			/** ����Ñ���,���yһ�����M�b */
			// ActionContext ctx = ActionContext.getContext();
			// Map<String, Object> session = ctx.getSession();
			// String userid = (String)session.get(Constant.SESSION_LOGIN);
			// PersonInfo sysuser = PersonManager.getPersonById(userid);
			// SysUser user = new SysUser();
			// user.setId(10);
			/*********************/

			if (kbknowledge != null) {
				if (kbknowledge.getId() == 0) {
					// ��������ʱ,��װ��������Ϣ
					kbknowledge.setCreator(getLoginUserId());// ��ȡ��¼��ID
					kbknowledge.setCreateDate(util.findSystemDate());
				} else {
					// �༭ʱ������������
					kbknowledge.setCreateDate(Date.valueOf(createdate));
				}

				kbknowledge.setUpdater(getLoginUserId());
				kbknowledge.setUpdateDate(util.findSystemDate());

				// baseType =1 :֪ʶ�� , 2: ����
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
	 * ���永������
	 * 
	 * @author Fancy
	 * @return
	 */
	public String saveCase() {
		return SUCCESS;
	}

	/**
	 * �����༭-����֪ʶ���
	 * 
	 * @return
	 */
	public String saveCategory() {
		String retMsg = "";
		boolean ret = true;
		KbmCategory cate = null;
		try {
			cate = knowledgeService.saveKbmCategory(kbcate);
			// ��װ���ص�����ʾ����Ϣ
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
	 * ɾ��--ɾ��֪ʶ�������ڵ�
	 * 
	 * @return
	 */
	public String deleteCategory() {

		String retMsg = "";
		boolean ret = true;

		try {
			// �жϸýڵ��Ƿ�ΪҶ�ӽڵ�
			if (isleaf == false) {
				ret = false;
				retMsg = "�÷��������������࣬�޷�ɾ��!";
			}
			// �жϸ÷������Ƿ�����ϸ
			else if (knowledgeService.findKnowledgeByParentId(id)) {
				ret = false;
				retMsg = "�÷������ж�Ӧ��ϸ���޷�ɾ��!";
			} else {
				// ����idɾ���÷���
				try {
					knowledgeService.removeCategoryById(id);
				} catch (ObjectRetrievalFailureException ex) {
					ret = false;
					retMsg = "�÷����ѱ�ɾ����������б�!";
				}
				// //�ж�
				// if(knowledgeService.findCategoryById(id)){
				// // ����idɾ���÷���
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
	 * �h��--ɾ��֪ʶ����ϸ
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
					// ����idɾ����֪ʶ��(�߼�ɾ��,���Ƿ����=1)
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
	 * ���ݱ������һ��֪ʶ����Ϣ(δʹ��)
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
			// // ��װ��������
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