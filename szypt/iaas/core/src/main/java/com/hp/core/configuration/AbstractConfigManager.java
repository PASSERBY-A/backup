/**
 * 
 */
package com.hp.core.configuration;


/**
 * 
 * @author Zhefang Chen
 *
 */
public abstract class AbstractConfigManager extends AbstractConfig implements ConfigManager {
	
	public AbstractConfigManager() {
		super();
	}

	public abstract void load();

	public abstract void reload();
}
