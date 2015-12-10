package com.hp.idc.resm.cache;

import java.sql.ResultSet;

import com.hp.idc.resm.model.AttributeDefine;
import com.hp.idc.resm.model.AttributeType;
import com.hp.idc.resm.model.StringAttributeDefine;


/**
 * ��Դ���Զ��建��
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class AttributeDefineCache extends CacheBase<AttributeDefine> {

	@Override
	protected AttributeDefine createNewObject(ResultSet rs) throws Exception {
		AttributeDefine ad = null;
		if (rs == null) {
			ad = new StringAttributeDefine();
			return ad;
		}
		String type = rs.getString("type");

		for (AttributeType a : AttributeType.ALL_TYPES) {
			if (a.getId().equals(type)) {
				ad = a.getDefineClass().newInstance();
				break;
			}
		}
		if (ad == null) {
			try {
				@SuppressWarnings("unchecked")
				Class<? extends AttributeDefine> cls = (Class<? extends AttributeDefine>) Class
						.forName(type);
				ad = cls.newInstance();
			} catch (Exception e) {
				throw new Exception("δ֪�����ͣ�" + type);
			}
		}

		ad.readFromResultSet(rs);
		return ad;
	}

	@Override
	public String getCacheName() {
		return "���Զ���";
	}
}
