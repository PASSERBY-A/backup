package com.hp.idc.itsm.util;

import java.util.List;

import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.configure.FormManager;
import com.hp.idc.itsm.workflow.NodeInfo;
import com.hp.idc.itsm.workflow.WorkflowData;
import com.hp.idc.itsm.workflow.WorkflowInfo;
import com.hp.idc.itsm.workflow.WorkflowManager;

/**
 * 完整性较验
 * @author 梅园
 *
 */
public class Checksum {
	
	/**
	 * 检查流程数据完整性
	 *
	 */
	static void checkWorkflows() {
		System.out.println("检查流程数据完整性...");
		int count = 0;
		List l = WorkflowManager.getWorkflows(true);
		for (int i = 0; i < l.size(); i++) {
			WorkflowInfo info = (WorkflowInfo)l.get(i);
			List l2 = info.getWorkflows();
			System.out.println("  检查流程: " + info.getName() + "(" + info.getOid() + ")");
			for (int j = 0; j < l2.size(); j++) {
				WorkflowData data = (WorkflowData)l2.get(j);
				System.out.println("    检查版本: " + data.getVersionId());
				List l3 = data.getNodes();
				for (int k = 0; k < l3.size(); k++) {
					NodeInfo node = (NodeInfo)l3.get(k);
					System.out.println("      检查节点: " + node.getCaption() + "(" + node.getId() + ")");
					if (FormManager.getFormByOid(Integer.parseInt(node.getFormId())) == null) {
						System.out.println("ERROR: 表单(" + node.getFormId() + ")没有找到!");
						count++;
					}
				}
			}
		}
		System.out.println("错误总数: " + count);
	}
	
	/**
	 * 测试主函数
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
