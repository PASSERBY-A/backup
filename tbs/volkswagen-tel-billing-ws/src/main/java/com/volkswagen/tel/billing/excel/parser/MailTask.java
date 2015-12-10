package com.volkswagen.tel.billing.excel.parser;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.volkswagen.tel.billing.billcall.jpa.domain.NotifyEntity;
import com.volkswagen.tel.billing.billcall.jpa.service.NotifyDaoService;
import com.volkswagen.tel.billing.common.EmailSender.MailSenderInfo;
import com.volkswagen.tel.billing.common.EmailSender.SimpleMailSender;
import com.volkswagen.tel.billing.common.GenericConfig;

public class MailTask {
	private static final Logger log = LoggerFactory.getLogger(MailTask.class);

	@Autowired
	private NotifyDaoService notifyDaoService;

	private boolean sendMail = false;

	private MailSenderInfo mailInfo;

	public MailTask() throws Exception {

		sendMail = Boolean
				.parseBoolean(GenericConfig.get("system.sendmail") == null ? "false"
						: GenericConfig.get("system.sendmail"));

		mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(GenericConfig.get("mail.smtp.host"));
		mailInfo.setMailServerPort(GenericConfig.get("mail.smtp.port"));
		mailInfo.setValidate(GenericConfig.get("mail.smtp.auth").equals("true"));
		mailInfo.setUserName(GenericConfig.get("mail.username"));
		mailInfo.setPassword(GenericConfig.get("mail.password"));// 您的邮箱密码
		mailInfo.setFromAddress(GenericConfig.get("mail.from.address"));

	}

	public void check() {

			log.info("mail service running.....");
			
			List<NotifyEntity> list = notifyDaoService.findBySendFlagFalse();

			if (list.size() != 0) {

				for (NotifyEntity e : list) {

					sendMail(e);
					notifyDaoService.setSendFlagAndSendsendTime(e.getId());
					log.info("mail to: " + e.getMail());
				}
 
			} 

			 log.info("mail count: "+list.size());

	}

	private void sendMail(NotifyEntity e) {
		MailSenderInfo info = new MailSenderInfo();

		try {
			BeanUtils.copyProperties(info, this.mailInfo);
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		info.setSubject("Reminder from the Telephone Billing System");

		// info.setSubject("Reminder from the Telephone Billing System");

		info.setContent(e.getContent());
		// 这个类主要来发送邮件 

		SimpleMailSender sms = new SimpleMailSender();

		try {

			if (StringUtils.isNotBlank(e.getMail())) {
				log.info(e + "--------------------------");
				info.setToAddress(e.getMail());
				sms.sendHtmlMail(info); 
			}

		} catch (MessagingException e1) {
			log.error(e1.getMessage());
		}

		// 发送html格式

	}

}
