<%@ page import="java.util.*"%>
<%@ page import="java.util.regex.*"%>

<%!
/**
	boolean[0]:节点是否是作废的，
	boolean[1]:节点是否可以点击，例如：如果是按路径过滤的，只允许选择路径末端的节点下的值，那么中间节点此属性返回false
**/
  public boolean[] isWaster(String id,String[] filter,String nodePath){
 		boolean[] ret = new boolean[2];
 		ret[0] = false;
 		ret[1] = true;
  	if (nodePath == null || filter == null)
  		return ret;
  	int nodeIndex = nodePath.split("/").length;
		ret[0] = true;
  	for(int fi = 0; fi < filter.length; fi++){
  		if (filter[fi] == null || filter[fi].equals(""))
  			continue;
			if (filter[fi].startsWith("_PATH_")){

				String fs = filter[fi].substring(6);
				if (fs.length()>0 && fs.charAt(0)!='/')
					fs = "/" + fs;
				if (fs.startsWith("/_/"))
					fs = "/-1/"+fs.substring(3);
				if (!fs.startsWith("/-1/"))
					fs = "/-1" + fs;
				fs = fs+"/";

				if (fs.startsWith(nodePath+"/"+id+"/") || fs.startsWith(nodePath+"/*/")){
					ret[0] = false;
					int endbegin = 0;
					if (fs.startsWith(nodePath+"/"+id+"/"))
						endbegin = (nodePath+"/"+id+"/").length();
					if (fs.startsWith(nodePath+"/*/"))
						endbegin = (nodePath+"/*/").length();
					String endStr = fs.substring(endbegin);
					endStr = endStr.replaceAll("/","");
					if (!endStr.equals(""))
						ret[1] = false;
				}
			} else {
				if (filter[fi].length()>0 && filter[fi].charAt(0)!='/')
					filter[fi] = "/" + filter[fi];
				if (filter[fi].startsWith("/_/"))
					filter[fi] = "/-1/"+filter[fi].substring(3);
				if (!filter[fi].startsWith("/-1/"))
					filter[fi] = "/-1" + filter[fi];

				String[] fs_ = filter[fi].split("/");
				if (nodeIndex < fs_.length && fs_[nodeIndex] != null && !fs_[nodeIndex].equals("*") && !fs_[nodeIndex].equals("")){
					Pattern p = Pattern.compile(fs_[nodeIndex]);
					Matcher m = p.matcher(id);
					if (m.find()) {
						ret[0] = false;
						ret[1] = true;
					}
				} else {
					ret[0] = false;
					ret[1] = true;
				}
			}
		}

		return ret;
  }
  
%>