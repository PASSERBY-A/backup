package com.cmsz.cloudplatform.service.schedule;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.model.TEquipmentenroll;
import com.cmsz.cloudplatform.service.PhysicalResourceSynManager;

@Service(value = "physicalResourceSynSchedule")
public class PhysicalResourceSynSchedule {

	private final Logger log = LoggerFactory.getLogger(PhysicalResourceSynSchedule.class);

	public void syn() {

		log.info("-----------------------------------物理资源同步  begin--------------------------------------------------------");

		List<TEquipmentenroll> equipmentEnrollList = physicalResourceSynManager.findEquipmentEnroll();
		if (equipmentEnrollList == null || equipmentEnrollList.size() == 0) {
			return;
		}

		for (TEquipmentenroll equipEnroll : equipmentEnrollList) {
			try {
				// equipEnroll
				int targetType = equipEnroll.getTargetType();
				String ip = equipEnroll.getIp();
				if (targetType == 0) {
					// 机架式服务器
					physicalResourceSynManager.synX86Rack(ip);
				} else if (targetType == 1) {
					// X86刀片机，刀片式服务器
					physicalResourceSynManager.synX86Host(ip);
				} else if (targetType == 7) {
					// 刀箱
					physicalResourceSynManager.synX86Enclosure(ip);
				} else if (targetType == 8) {
					// HP 小机
					physicalResourceSynManager.synHpHost(ip);
				} else {
					log.error("Invalid target type . It is not supported.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Failure enroll equipment [" + equipEnroll + "]",
									e);
			}
		}

		log.info("-----------------------------------物理资源同步  end--------------------------------------------------------");

	}

	@Autowired
	private PhysicalResourceSynManager physicalResourceSynManager;

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		PhysicalResourceSynSchedule synSchedule = (PhysicalResourceSynSchedule) applicationContext
				.getBean("physicalResourceSynSchedule");
		synSchedule.syn();
	}

}
