var a = "a=1,b=2";
var arr = a.split(",");
var temp="";
for(var i=0;i<arr.size;i++){
	temp += arr[i].split(",")[0]+",";
}