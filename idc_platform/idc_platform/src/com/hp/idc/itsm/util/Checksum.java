package com.hp.idc.itsm.util;

import java.util.List;

import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.configure.FormManager;
import com.hp.idc.itsm.workflow.NodeInfo;
import com.hp.idc.itsm.workflow.WorkflowData;
import com.hp.idc.itsm.workflow.WorkflowInfo;
import com.hp.idc.itsm.workflow.WorkflowManager;

/**
 * �����Խ���
 * @author ÷԰
 *
 */
public class Checksum {
	
	/**
	 * �����������������
	 *
	 */
	static void checkWorkflows() {
		System.out.println("�����������������...");
		int count = 0;
		List l = WorkflowManager.getWorkflows(true);
		for (int i = 0; i < l.size(); i++) {
			WorkflowInfo info = (WorkflowInfo)l.get(i);
			List l2 = info.getWorkflows();
			System.out.println("  �������: " + info.getName() + "(" + info.getOid() + ")");
			for (int j = 0; j < l2.size(); j++) {
				WorkflowData data = (WorkflowData)l2.get(j);
				System.out.println("    ���汾: " + data.getVersionId());
				List l3 = data.getNodes();
				for (int k = 0; k < l3.size(); k++) {
					NodeInfo node = (NodeInfo)l3.get(k);
					System.out.println("      ���ڵ�: " + node.getCaption() + "(" + node.getId() + ")");
					if (FormManager.getFormByOid(Integer.parseInt(node.getFormId())) == null) {
						System.out.println("ERROR: ��(" + node.getFormId() + ")û���ҵ�!");
						count++;
					}
				}
			}
		}
		System.out.println("��������: " + count);
	}
	
	/**
	 * ����������
	 * @param args
	 */
	public static void main (String[] args) {
		ConnectManager.setJdbcMode("192.168.11.25:1521:toptea","utoptea", "utopteaabc");
		//ConnectManager.setJdbcMode("10.154.54.11:1521:bomc","utoptea", "utopteaabc");
		try {
			new Cache().init();
			checkWorkflows();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
