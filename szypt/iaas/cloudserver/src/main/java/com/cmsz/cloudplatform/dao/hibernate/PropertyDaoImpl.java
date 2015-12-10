package com.cmsz.cloudplatform.dao.hibernate;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cmsz.cloudplatform.dao.PropertyDao;
import com.cmsz.cloudplatform.model.Property;
import com.hp.core.dao.hibernate.GenericDaoImpl;
import com.hp.util.StringUtil;

@Repository("propertyDao")
public class PropertyDaoImpl extends GenericDaoImpl<Property, Long> implements PropertyDao {

	public PropertyDaoImpl(Class<Property> clazz) {
		super(clazz);
	}

	public PropertyDaoImpl() {
		super(Property.class);
	}

	private void addLikeCriteria(DetachedCriteria detachedCriteria, String fieldName, String criteria) {
		if (StringUtils.isNotBlank(criteria) && StringUtils.isNotBlank(criteria)) {
			detachedCriteria.add(Restrictions.like(fieldName, "%" + criteria + "%"));
		}
	}

	@SuppressWarnings("unchecked")
	public List<Property> findPropertyListByExample(final Property example, final int pageNumber, final int pageSize) {
		List<Property> results = null;

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Property.class);

		addLikeCriteria(detachedCriteria, "type", example.getType());
		addLikeCriteria(detachedCriteria, "vendor", example.getVendor());
		addLikeCriteria(detachedCriteria, "model", example.getModel());
		addLikeCriteria(detachedCriteria, "code", example.getCode());
		addLikeCriteria(detachedCriteria, "serial_num", example.getSerial_num());
		addLikeCriteria(detachedCriteria, "status", example.getStatus());
		addLikeCriteria(detachedCriteria, "owner", example.getOwner());

		// case "0":return '机架式X86服务器'; case "1":return
		// * '刀片式X86服务器';
		// */
		// case "2":
		// return '小型机';
		// case "3":
		// return '交换机';
		// case "4":
		// return '路由器';
		// case "5":
		// return 'SAN Switch';
		// case "6":
		// return '存储设备';
		// id : '7',
		// description : '刀箱'
		String section = example.getSection();
		if (StringUtil.isNotEmpty(section)) {
			String[] values = null;
			if (section.equals("propertys")) {
				values = new String[] { "2", "3", "4", "5", "6" };
			} else if (section.equals("x86")) {
				values = new String[] { "0" };
			} else if (section.equals("x96")) {
				values = new String[] { "1" };
			} else if (section.equals("box")) {
				values = new String[] { "7" };
			}
			detachedCriteria.add(Restrictions.in("type", values));
		}

		// String belong = this.request.getParameter("belong_to");
		// String room = this.request.getParameter("room");
		// String machineCabinet = this.request.getParameter("machine_cabinet");
		//
		// addEqualQueryCriteria(detachedCriteria, "belong_to",belong);
		// addEqualQueryCriteria(detachedCriteria, "room", room);
		// addEqualQueryCriteria(detachedCriteria, "machine_cabinet",
		// machineCabinet);

		if (null != example.getStart_date()) {
			if (null != example.getEnd_date()) {
				detachedCriteria.add(Restrictions.between("start_date", example.getStart_date(), example.getEnd_date()));
			} else {
				detachedCriteria.add(Restrictions.gt("start_date", example.getStart_date()));
			}
		} else if (null != example.getEnd_date()) {
			detachedCriteria.add(Restrictions.lt("start_date", example.getEnd_date()));
		}

		results = (List<Property>) this.findByCriteria(detachedCriteria, pageNumber, pageSize);

		if (null == results) {
			results = Collections.emptyList();
		}

		return results;
	}

}
