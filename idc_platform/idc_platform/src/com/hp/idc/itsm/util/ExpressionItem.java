package com.hp.idc.itsm.util;

public class ExpressionItem {
	
	public static final String OPERATORS = "()*/%+-&|=";
	
	/**
	 * 操作符优先级常量表，按照：
	 * CONSTANT[0]='*';
	 * CONSTANT[1]='/';
	 * CONSTANT[2]='%';
	 * CONSTANT[3]='+';
	 * CONSTANT[4]='-';
	 * CONSTANT[5]='&';
	 * CONSTANT[6]='|';
	 * CONSTANT[7]='=';
	 * 排列出来的二维表
	 */
	private static final int CONSTANT[][]= {{0 , 0 , 0 , 1 , 1 , 0 , 0 , 1},
									  		{0 , 0 , 0 , 1 , 1 , 0 , 0 , 1},
									  		{0 , 0 , 0 , 1 , 1 , 0 , 0 , 1},
									  		{-1,-1, -1 , 0 , 0 , 0 , 0 , 1},
									  		{-1,-1, -1 , 0 , 0 , 0 , 0 , 1},
									  		{0 , 0 , 0 , 0 , 0 , 0 , 1, -1},
									  		{0 , 0 , 0 , 0 , 0, -1 , 0, -1},
									  		{-1,-1, -1, -1, -1 , 1 , 1 , 0}
									  		};
	

	/**
	 * 操作符，就是一个字符OPERATORS内一个字符
	 */
	private String item = "";
	
	private ExpressionItem leftItem = null;
	private ExpressionItem rightItem = null;
	
	/**
	 * 父节点
	 */
	private ExpressionItem parent = null;
	
	/**
	 * 是否是子串
	 */
	private boolean subExpression = false;
	
	public int getPriority(char c){
		switch(c) {
			case '*':
				return	0;
			case '/':
				return	1;
			case '%':
				return	2;
			case '+':
				return	3;
			case '-':
				return	4;
			case '&':
				return	5;
			case '|':
				return	6;
			case '=':
				return	7;
			default:
				return -1;
		}
	}
	
	public ExpressionItem getRoot(){
		if (parent == null)
			return this;
		else
			return parent.getRoot();
	}
	
	public ExpressionItem getParent() {
		return parent;
	}

	public void setParent(ExpressionItem parent) {
		this.parent = parent;
	}

	public String getValue(){
		if (leftItem == null || rightItem == null)
			return item;
		else{
			if (item.equals("|") || item.equals("&")){
				boolean bleft = Boolean.valueOf(leftItem.getValue()).booleanValue();
				boolean bright = Boolean.valueOf(rightItem.getValue()).booleanValue();
				if (item.equals("|")){
					return (bleft||bright)+"";
				}
				else if (item.equals("&")){
					return (bleft&&bright)+"";
				}
			} else {
				String leftV = leftItem.getValue();
				String rightV = rightItem.getValue();
				if (item.equals("=")) {
					return (leftV.equals(rightV))+"";
				} else {
					boolean isFloat = false;
					if (leftV.indexOf(".")!=-1 || rightV.indexOf(".")!=-1)
						isFloat = true;
					float fleft = Float.parseFloat(leftV);
					float fright = Float.parseFloat(rightV);
					float ret = 0;;
					if (item.equals("*")){
						ret = fleft*fright;
					}
					else if (item.equals("/")){
						ret = fleft/fright;
					}
					else if (item.equals("%")){
						ret = fleft%fright;
					}
					else if (item.equals("+")){
						ret = fleft+fright;
					}
					else if (item.equals("-")){
						ret = fleft-fright;
					}
					if (!isFloat) {
						return (int)ret+"";
					} else
						return ret+"";
				}
			}
			
		}
		return "";
	}
	
	public int hasPriority(ExpressionItem oper){
		if (this.subExpression)
			return 1;
		if (oper.subExpression)
			return -1;
		int i1 = getPriority(this.item.charAt(0));
		int i2 = getPriority(oper.getItem().charAt(0));
		return CONSTANT[i1][i2];
		
	}
	public ExpressionItem getLeftItem() {
		return leftItem;
	}

	public void setLeftItem(ExpressionItem leftItem) {
		this.leftItem = leftItem;
	}

	public ExpressionItem getRightItem() {
		return rightItem;
	}

	public void setRightItem(ExpressionItem rightItem) {
		this.rightItem = rightItem;
	}

	public boolean isOperator(){
		if (OPERATORS.indexOf(item)!=-1)
			return true;
		return false;
	}

	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}

	public boolean isSubExpression() {
		return subExpression;
	}

	public void setSubExpression(boolean subExpression) {
		this.subExpression = subExpression;
	}
	
	public void print(){
		System.out.println(item);
		if (this.leftItem!=null)
			System.out.println(this.leftItem.getItem());
		if (this.rightItem!=null)
			System.out.println(this.rightItem.getItem());
	}
	
	
}
