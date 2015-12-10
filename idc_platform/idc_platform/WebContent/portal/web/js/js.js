function getDom(id){
	return document.getElementById(id);
}
window.onload = function(){
	for(var i = 0;i<9;i++){
		var id = "hrefa"+i;
		var obj = getDom(id);
		var divName = "divMenu0"+(i+1);
		(function(i,obj){
			if(obj == null)
				return;
			obj.onmouseover = function(){
				showMenuHandler(obj,"divMenu0"+(i+1),true);
			};
			obj.onmouseout = function(){
				showMenuHandler(obj,"divMenu0"+(i+1),false);
			};
		})(i,obj);
	}
}; 
function showMenuHandler(em,obj,sd){
	var obj = getDom(obj);
	if(obj){
		if(sd){
			obj.style.display = "";
			em.className = "focus";
			obj.onmouseover = function(){
				this.style.display = "";
				em.className = "focus";
	    	};
	    	obj.onclick = function(){
				this.style.display = "";
				em.className = "focus";
	    	};
	  		obj.onmouseout = function(){
				this.style.display = "none";
				em.className = "";
		 	};
		}else{
		 	obj.style.display = "none";
		 	em.className = "";
		}
	}
} 