	Ext.form.NumberField.prototype.size = 20;  
	Ext.form.NumberField.prototype.initValue = function() {  
	    if (this.value !== undefined) {  
	        this.setValue(this.value);  
	    } else if (this.el.dom.value.length > 0) {  
	        this.setValue(this.el.dom.value);  
	    }  
	    this.el.dom.size = this.size;  
	    if (!isNaN(this.maxLength) && (this.maxLength * 1) > 0  
	            && (this.maxLength != Number.MAX_VALUE)) {  
	        this.el.dom.maxLength = this.maxLength * 1;  
	    }  
	};
	
	Ext.form.TextField.prototype.size = 20;  
	Ext.form.TextField.prototype.initValue = function() {  
	    if (this.value !== undefined) {  
	        this.setValue(this.value);  
	    } else if (this.el.dom.value.length > 0) {  
	        this.setValue(this.el.dom.value);  
	    }  
	    this.el.dom.size = this.size;  
	    if (!isNaN(this.maxLength) && (this.maxLength * 1) > 0  
	            && (this.maxLength != Number.MAX_VALUE)) {  
	        this.el.dom.maxLength = this.maxLength * 1;  
	    }  
	};