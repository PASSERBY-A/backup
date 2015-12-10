package com.hp.idc.resm.service;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.model.AttributeDefine;
import com.hp.idc.resm.model.AttributeType;
import com.hp.idc.resm.model.Code;
import com.hp.idc.resm.model.ModelAttribute;
import com.hp.idc.resm.util.ResmConfig;

/**
 * 资源属性远程服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RemoteAttributeService extends
		RemoteServiceBasic<IAttributeService> implements IAttributeService {

	@Override
	protected String getRemoteServiceURL() {
		return "http://" + ResmConfig.server + "/hessian/AttributeService";
	}

	public AttributeDefine getAttributeById(String id) {
		return getRemoteService().getAttributeById(id);
	}

	public List<AttributeDefine> getAllAttributes() {
		return getRemoteService().getAllAttributes();
	}

	public List<AttributeType> getAllAttributeTypes() {
		List<AttributeType> list = new ArrayList<AttributeType>();
		for (AttributeType a : AttributeType.ALL_TYPES)
			list.add(a);
		return list;
	}

	public AttributeType getAttributeTypeById(String id) {
		for (AttributeType a : AttributeType.ALL_TYPES) {
			if (a.getId().equals(id)) {
				return a;
			}
		}
		return null;
	}

	public List<AttributeDefine> getAllDimensions() {
		return getRemoteService().getAllDimensions();
	}
	
	public List<ModelAttribute> getDimensionsByModelId(String modelId) {
		return getRemoteService().getDimensionsByModelId(modelId);
	}

	/**
	 * TEST
	 * @param args TEST
	 */
	public static void main(String[] args) {
		ResmConfig.server = "127.0.0.1:10096";
		RemoteAttributeService s = new RemoteAttributeService();
		AttributeType t = s.getRemoteService().getAllAttributeTypes().get(0);
		System.out.println(t.getName());
		//System.out.println(t.getJavaClass().getName());
	}

	@Override
	public List<AttributeDefine> getAttributesForAdd(String parentModelId) {
		return getRemoteService().getAttributesForAdd(parentModelId);
	}

	@Override
	public List<Code> getAllCodes() {
		return getRemoteService().getAllCodes();
	}

}
