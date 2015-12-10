package com.volkswagen.tel.billing.common.util;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;

public class JndiPropertyConfigurer extends PropertyResourceConfigurer implements BeanFactoryAware, BeanNameAware {

	private static Log log = LogFactory.getLog(JndiPropertyConfigurer.class);

	public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

	public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

	private String placeholderPrefix = DEFAULT_PLACEHOLDER_PREFIX;

	private String placeholderSuffix = DEFAULT_PLACEHOLDER_SUFFIX;

	private boolean ignoreUnresolvablePlaceholders = false;

	private String beanName;

	private BeanFactory beanFactory;

	public void setPlaceholderPrefix(String placeholderPrefix) {
		this.placeholderPrefix = placeholderPrefix;
	}

	public void setPlaceholderSuffix(String placeholderSuffix) {
		this.placeholderSuffix = placeholderSuffix;
	}

	public void setIgnoreUnresolvablePlaceholders(boolean ignoreUnresolvablePlaceholders) {
		this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void setBeanFactory(org.springframework.beans.factory.BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {

		BeanDefinitionVisitor visitor = new PlaceholderResolvingBeanDefinitionVisitor(props);
		String[] beanNames = beanFactoryToProcess.getBeanDefinitionNames();
		for (int i = 0; i < beanNames.length; i++) {
			if (!(beanNames[i].equals(this.beanName) && beanFactoryToProcess.equals(this.beanFactory))) {
				BeanDefinition bd = beanFactoryToProcess.getBeanDefinition(beanNames[i]);
				try {
					visitor.visitBeanDefinition(bd);
				} catch (BeanDefinitionStoreException ex) {
					log.error(ex.getMessage(), ex);
					throw new BeanDefinitionStoreException(bd.getResourceDescription(), beanNames[i], ex.getMessage());
				}
			}
		}
	}

	protected String parseStringValue(String strVal, Properties props, String originalPlaceholder) throws BeanDefinitionStoreException {
		StringBuffer buf = new StringBuffer(strVal);
		int startIndex = buf.indexOf(this.placeholderPrefix);
		while (startIndex != -1) {
			int endIndex = buf.indexOf(this.placeholderSuffix, startIndex + this.placeholderPrefix.length());
			if (endIndex != -1) {
				String placeholder = buf.substring(startIndex + this.placeholderPrefix.length(), endIndex);
				String originalPlaceholderToUse = null;
				if (originalPlaceholder != null) {
					originalPlaceholderToUse = originalPlaceholder;
					if (placeholder.equals(originalPlaceholder)) {
						throw new BeanDefinitionStoreException("Circular placeholder reference '" + placeholder + "' in property definitions");
					}
				} else {
					originalPlaceholderToUse = placeholder;
				}

				String propVal = resolvePlaceholder(placeholder);
				if (propVal != null) {
					propVal = parseStringValue(propVal, props, originalPlaceholderToUse);
					buf.replace(startIndex, endIndex + this.placeholderSuffix.length(), propVal);
					if (log.isDebugEnabled()) {
						log.debug("Resolved placeholder '" + placeholder + "' to value [" + propVal + "]");
					}
					startIndex = buf.indexOf(this.placeholderPrefix, startIndex + propVal.length());
				} else if (this.ignoreUnresolvablePlaceholders) {
					// Proceed with unprocessed value.
					startIndex = buf.indexOf(this.placeholderPrefix, endIndex + this.placeholderSuffix.length());
				} else {
					throw new BeanDefinitionStoreException("Could not resolve placeholder '" + placeholder + "'");
				}
			} else {
				startIndex = -1;
			}
		}

		return buf.toString();
	}

	protected String resolvePlaceholder(String placeholder) {
		InitialContext initialContext = null;
		try {

			initialContext = new InitialContext();
			try {
				return (String) initialContext.lookup("java:comp/env/" + placeholder);
			} catch (NameNotFoundException e) {
				log.info("error finding a name in JNDI: " + placeholder);
			}
		} catch (NamingException e) {
			if (!ignoreUnresolvablePlaceholders) {
				log.error("error binding JNDI", e);
				throw new RuntimeException(e);
			} else {
				log.info("error binding JNDI");
			}
		} finally {
			if (initialContext != null) {
				try {
					initialContext.close();
				} catch (NamingException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		return null;
	}

	private class PlaceholderResolvingBeanDefinitionVisitor extends BeanDefinitionVisitor {
		private final Properties props;

		public PlaceholderResolvingBeanDefinitionVisitor(Properties props) {
			this.props = props;
		}

		@Override
		protected String resolveStringValue(String strVal) {
			return parseStringValue(strVal, this.props, null);
		}
	}

}

