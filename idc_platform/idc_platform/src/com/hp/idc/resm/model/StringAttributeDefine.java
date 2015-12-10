package com.hp.idc.resm.model;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.json.JSONObject;
import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.StringAttribute;
import com.hp.idc.resm.service.CachedAttributeService;
import com.hp.idc.resm.service.ServiceManager;

/**
 * �ַ�����Դ���Զ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public final class StringAttributeDefine extends AttributeDefine {

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = 6684545192930735356L;

	/**
	 * ����������
	 */
	private static AttributeOperator[] OPERATORS = {
		new AttributeOperator("!=", "������", "$0 != \"$1\""),
		new AttributeOperator("==", "����", "$0 == \"$1\""),
		new AttributeOperator("sub", "����", "$0.indexOf(\"$1\") != -1"),
		new AttributeOperator("nsub", "������", "$0.indexOf(\"$1\") == -1"),
		new AttributeOperator("starts", "��...��ʼ", "$0.startsWith(\"$1\")"),
		new AttributeOperator("ends", "��...����", "$0.endsWith(\"$1\")"),
		new AttributeOperator("isnull", "Ϊ��", "$null.isNull($0)"),
		new AttributeOperator("notnull", "��Ϊ��", "$null.isNotNull($0)")
	};
	
	/**
	 * ��������
	 */
	private static AttributeConst[] CONSTS = {
	};
	
	/**
	 * �������ͣ�0=��ͨ�ַ�����1=ѡ���ͣ�2=����ѡ��, 3=�ı���
	 */
	private int codeType = 0;

	/**
	 * ����id
	 */
	private String codeId = null;

	/**
	 * �����Ĵ����б�ֻ��Ϊ�����л�ʹ�õģ��˱���������ֵ
	 */
	private List<Code> codes = null;
	
	
	/**
	 * ��ȡ��������
	 * 
	 * @return codeType �������ͣ�0=��ͨ�ַ�����1=ѡ���ͣ�2=����ѡ��
	 * @see #codeType
	 */
	public int getCodeType() {
		return this.codeType;
	}

	/**
	 * ���ô�������
	 * 
	 * @param codeType
	 *            �������ͣ�0=��ͨ�ַ�����1=ѡ���ͣ�2=����ѡ��
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #codeType
	 */
	public void setCodeType(int codeType) throws CacheException {
		checkSet();
		this.codeType = codeType;
	}

	/**
	 * ��ȡ����id
	 * 
	 * @return ����id
	 * @see #codeId
	 */
	public String getCodeId() {
		return this.codeId;
	}

	/**
	 * ���ô���id
	 * 
	 * @param codeId
	 *            ����id
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #codeId
	 */
	public void setCodeId(String codeId) throws CacheException {
		checkSet();
		this.codeId = codeId;
	}

	/**
	 * ��ȡ�����Ĵ���
	 * 
	 * @return codes �����б�
	 * @see #codes
	 */
	public List<Code> getCodes() {
		CachedAttributeService cas = (CachedAttributeService) ServiceManager
				.getAttributeService();
		return cas.getCodeCache().getCodeList(this.codeId);
	}

	/**
	 * ���ù����Ĵ��룬�˷�����Ч����Դ���Զ����Ǵӻ����ж�ȡ��
	 * 
	 * @param codes
	 *            �����Ĵ���
	 * @see #codes
	 */
	public void setCodes(List<Code> codes) {
		// �պ���
	}

	@Override
	public void setArguments(String arguments) throws CacheException {
		if (arguments == null) {
			arguments = "{ }";
		}
		try {
			JSONObject json = new JSONObject(arguments);
			setCodeType(Integer.parseInt(json.optString("codeType", "0")));
			json.put("codeType", this.codeType);
			setCodeId(json.optString("codeId", ""));
			json.put("codeId", this.codeId);
			super.setArguments(arguments);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CacheException("������ʽ����: " + this.getId() + "/" + this.getName());
		}
	}

	@Override
	public String valid(String value, String value1, String attrId, String op) {
		if (value == null || value.length() == 0)
			return null;
		if (this.codeType > 0) {
			for (Code c : this.codes) {
				if (value.equals(c.getName()))
					return null;
			}
		}
		if (this.codeType == 2) { // ���ͽṹ�����������ӽڵ�
			List<Code> list = new ArrayList<Code>();
			for (Code c : this.codes) {
				if (c.getChilds() != null)
					list.addAll(c.getChilds());
			}
			while (list.size() > 0) {
				Code c = list.remove(list.size() - 1);
				if (value.equals(c.getName()))
					return null;
				if (c.getChilds() != null)
					list.addAll(c.getChilds());
			}
		}
		return "���ԡ�" + this.getName() + "����Ч��ȡֵ:" + value;
	}
	
	@Override
	public String getDataType() {
		return "varchar2";
	}

	@Override
	public int getLength() {
		return this.length;
	}
	
	@Override
	protected AttributeBase createInstance() {
		return new StringAttribute();
	}
	
	@Override
	public List<AttributeOperator> getOperators() {
		List<AttributeOperator> list = new ArrayList<AttributeOperator>(
				OPERATORS.length);
		for (AttributeOperator op : OPERATORS)
			list.add(op);
		return list;
	}
	
	@Override
	public List<AttributeConst> getConsts() {
		List<AttributeConst> list = new ArrayList<AttributeConst>(
				CONSTS.length);
		for (AttributeConst c : CONSTS)
			list.add(c);
		return list;
	}
}
