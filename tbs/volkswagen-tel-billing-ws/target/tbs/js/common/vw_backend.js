var backendRoleChecker = {
    "roleArray" : [],
    "contains" : function(targetRole) {
    	if ($.inArray(targetRole, this.roleArray)!=-1) {
    		return true;
    	}
    	return false;
    }
};