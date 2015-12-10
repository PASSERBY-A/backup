/**
 * 
 */
package com.hp.core;

/**
 * @author 907948
 *
 */
public enum UserStatus {
	ACTIVE("active"),INACTIVE("inactive");
	private String value;

	
	private UserStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static String getName(String _val) {
		String rslt = null;
		if (_val==null){
			return rslt;
		}
		
		UserStatus [] _enums = UserStatus.values();
		for (UserStatus _enum : _enums) {
			if (_val.equals(_enum.getValue())){
				rslt = _enum.name();
				break;
			}
		}
		return rslt;
	}
}
