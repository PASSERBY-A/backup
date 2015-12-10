package com.hp.idc.boss;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.idc.business.dao.ProductCatalogDao;
import com.hp.idc.business.dao.ProductCatalogDtlDao;
import com.hp.idc.business.dao.ProductDao;
import com.hp.idc.business.dao.ServiceDao;
import com.hp.idc.business.dao.ServiceResourceDao;
import com.hp.idc.customer.dao.CusAccountDao;
import com.hp.idc.customer.dao.CusBussinessDao;
import com.hp.idc.customer.dao.CusContactDao;
import com.hp.idc.customer.dao.CusInfoDao;
import com.hp.idc.customer.dao.CusServantDao;
import com.hp.idc.customer.dao.CusServiceDao;

public abstract class BOSSBase implements BOSSCommonInterface{
	
	@Resource
	private ProductCatalogDao productCatalogDao;

	@Resource
	private ProductCatalogDtlDao productCatalogDtlDao;
	
	@Resource
	private ServiceDao serviceDao;
	
	@Resource
	private ProductDao productDao;
	
	@Resource
	private ServiceResourceDao serviceResourceDao;

	// �˻����ñ�dao
	@Resource
	private CusAccountDao cusAccountDao;

	// boss idcҵ����Ϣ��dao
	@Resource
	private CusBussinessDao cusBussinessDao;
	
	// ��ϵ�˱�
	@Resource
	private CusContactDao cusContactDao;
	
	// �ͻ�������Ϣ��dao
	@Resource
	private CusInfoDao cusInfoDao;
	
	// �ͻ����Ѽ�¼��dao
	@Resource
	private CusServiceDao cusServiceDao;

	// �ͻ�������Ϣ��dao
	@Resource
	private CusServantDao cusServantDao;

	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * �ָ���
	 */
	private String separation = "\\|";

	/**
	 * �н�����
	 */
	private static String ending = "";
	
	/**
	 * Ŀ���ļ���
	 */
	private String destFileDIR = "";
	
	/**
	 *�����ļ��� 
	 */
	private String backUpDIR = "";
	
	
	/**
	 * ������Ϣ
	 */
	private String errorMsg = "";
	
	/**
	 * �ļ����ֶ�˳��
	 */
	private String[] rule;
	
	/**
	 * 
	 */
	public abstract FilenameFilter getFilenameFilter();
	
	/**
	 * ����ʱ���ʽ
	 */
	private String timeFormat = "yyyyMMddHHmmss";

	/**
	 * �ļ���Ϣcheck �������ݻ�ŵ�errorMsg����
	 * @return true|false
	 */
	public boolean Validate(String contents) {
		if(contents == null || "".equals(contents)){
			setErrorMsg("�ļ���ʽ������.");
			return false;
		}
		if (contents.split(getSeparation()) == null
				|| contents.split(getSeparation()).length != getRule().length) {
			setErrorMsg("�ļ���ʽ������.");
			return false;
		} else {
			String[] rule = getRule();
			int i = 0;
			for (String key : rule) {
				if (key.contains("date")) {
					String createDate = contents.split(getSeparation())[i];
					DateFormat df = new SimpleDateFormat(timeFormat);
					try {
						df.parse(createDate);
					} catch (Exception e) {
						setErrorMsg("ʱ���ʽ������Ҫ��.");
						return false;
					}
				}
				i++;
			}
			return true;
		}
	}
	
	/**
	 * ִ�ж��ļ��������ݲ���
	 * 
	 * @return �ɹ�|ʧ��
	 */
	public boolean defExecute() {
		return execute(getFilenameFilter());
	}
	
	/**
	 * ִ�ж��ļ��������ݲ���
	 * 
	 * @param fileFilter
	 *            �����ļ�
	 * @return �ɹ�|ʧ��
	 */
	@SuppressWarnings("unchecked")
	public boolean execute(FilenameFilter fileFilter) {
		log.info("�ļ�����ʼ��");
		List<String> fileContentsList = new ArrayList<String>();
		try {
			File DIR = new File(destFileDIR);
			File[] files = DIR.listFiles(fileFilter);
			if (files != null) {
				for (File file : files) {
					try {
						boolean fileCaseFlg = false;
						fileContentsList = FileUtils.readLines(file);
						for (String contents : fileContentsList) {
							if (Validate(contents)) {
								String[] contentsArr = contents
										.split(separation);
								String[] rule = this.rule;
								Map<String, String> result = fillRecord(
										contentsArr, rule);
								log.info("��ʼ�������ݡ�");
								fileCaseFlg = saveResult(result);
								log.info("������������������");
							} else {
								fileCaseFlg = false;
								break;
							}
						}
						if (fileCaseFlg) {
							moveFileToBackUPDIR(file);
						}
					} catch (Exception e) {
						e.printStackTrace();
						errorMsg = e.getMessage();
						log.error(file.getName()+"�ļ�����ʧ�ܡ����顣");
						continue;
					}
				}
			} else {
				errorMsg = "û���ҵ��ļ�.";
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = e.getMessage();
			return false;
		}
		log.info("�ļ����������");
		return true;
	}
    
	/**
	 * ��������
	 * @param result
	 * @throws Exception 
	 */
    public abstract boolean saveResult(Map<String, String> result) throws Exception;
	
	/**
	 * ת���Ѵ�����ļ�
	 * @param file
	 * @throws IOException  
	 */
	private void moveFileToBackUPDIR(File file) throws IOException {
		FileUtils.moveFileToDirectory(file, new File(backUpDIR), false);
	}
	
	
	/**
	 * ����Ϣ����������
	 * 
	 * @param contents ������
	 * @param record  ��¼
	 * @return ��¼
	 */
	private static Map<String, String> fillRecord(String[] contents,
			String[] record) {
		int count = 0;
		Map<String, String> entityMap = new HashMap<String, String>();
		for (String key : record) {
			entityMap.put(key, contents[count]);
			count++;
		}
		return entityMap;
	}


	public String getSeparation() {
		return separation;
	}


	public void setSeparation(String separation) {
		this.separation = separation;
	}


	public static String getEnding() {
		return ending;
	}


	public static void setEnding(String ending) {
		BOSSBase.ending = ending;
	}


	public  String getDestFileDIR() {
		return destFileDIR;
	}


	public  void setDestFileDIR(String destFileDIR) {
		this.destFileDIR = destFileDIR;
	}


	public String getBackUpDIR() {
		return backUpDIR;
	}


	public void setBackUpDIR(String backUpDIR) {
		this.backUpDIR = backUpDIR;
	}


	public String getErrorMsg() {
		return errorMsg;
	}


	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


	public String[] getRule() {
		return rule;
	}


	public void setRule(String[] rule) {
		this.rule = rule;
	}


	public String getTimeFormat() {
		return timeFormat;
	}


	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}


	public ProductCatalogDao getProductCatalogDao() {
		return productCatalogDao;
	}


	public void setProductCatalogDao(ProductCatalogDao productCatalogDao) {
		this.productCatalogDao = productCatalogDao;
	}


	public ProductCatalogDtlDao getProductCatalogDtlDao() {
		return productCatalogDtlDao;
	}


	public void setProductCatalogDtlDao(ProductCatalogDtlDao productCatalogDtlDao) {
		this.productCatalogDtlDao = productCatalogDtlDao;
	}


	public ServiceDao getServiceDao() {
		return serviceDao;
	}


	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}


	public ProductDao getProductDao() {
		return productDao;
	}


	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}


	public ServiceResourceDao getServiceResourceDao() {
		return serviceResourceDao;
	}


	public void setServiceResourceDao(ServiceResourceDao serviceResourceDao) {
		this.serviceResourceDao = serviceResourceDao;
	}


	public CusAccountDao getCusAccountDao() {
		return cusAccountDao;
	}


	public void setCusAccountDao(CusAccountDao cusAccountDao) {
		this.cusAccountDao = cusAccountDao;
	}


	public CusBussinessDao getCusBussinessDao() {
		return cusBussinessDao;
	}


	public void setCusBussinessDao(CusBussinessDao cusBussinessDao) {
		this.cusBussinessDao = cusBussinessDao;
	}


	public CusContactDao getCusContactDao() {
		return cusContactDao;
	}


	public void setCusContactDao(CusContactDao cusContactDao) {
		this.cusContactDao = cusContactDao;
	}


	public CusInfoDao getCusInfoDao() {
		return cusInfoDao;
	}


	public void setCusInfoDao(CusInfoDao cusInfoDao) {
		this.cusInfoDao = cusInfoDao;
	}


	public CusServiceDao getCusServiceDao() {
		return cusServiceDao;
	}


	public void setCusServiceDao(CusServiceDao cusServiceDao) {
		this.cusServiceDao = cusServiceDao;
	}


	public CusServantDao getCusServantDao() {
		return cusServantDao;
	}


	public void setCusServantDao(CusServantDao cusServantDao) {
		this.cusServantDao = cusServantDao;
	}

}
