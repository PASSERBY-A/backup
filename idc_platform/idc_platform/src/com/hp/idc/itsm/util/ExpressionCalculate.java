package com.hp.idc.itsm.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public class ExpressionCalculate {
	
	/**
	 *  ������ʽ��boolֵ,ֻ֧�ּ򵥵ı��ʽ(b1|b2|b3)&b4&(b5|b6)
	 * ������ʽ����û�б�����ȫ��true����false����params��null����
	 * @param params
	 * @param expression "||"��|�����"&&"��"&"���
	 * @return
	 * @throws Exception
	 */
	public static boolean calculateBoolean(Map params,String expression) throws Exception{
		String ret = calculate(params,expression);
		return new Boolean(ret).booleanValue();
		
	}
	/**
	 * �������ʽ������в������Ѳ����滻��ֵ
	 * @param params ����hash��
	 * @param expression ��׼�ı��ʽ
	 * @return
	 * @throws Exception
	 */
	public static String calculate(Map params,String expression) throws Exception{
//		System.out.println("map---:"+params.toString());
//		System.out.println("exp---:"+expression);
		expression = (expression==null||expression.trim().equals(""))?"":expression;
		int i = 0;
		int pos = 0;
		Stack parmStack = new Stack();
		Stack operStack = new Stack();
		while (i < expression.length()){
			char c = expression.charAt(i);
			if (ExpressionItem.OPERATORS.indexOf(c+"")!=-1){
				if (pos !=i){
					String v = expression.substring(pos, i);
					v = v.trim();
					if (params!=null && params.get(v)!=null)
						v = (String)params.get(v);
					if (v==null || v.equals(""))
						v = "";
					parmStack.push(v);
				}
				String oper = expression.substring(i, i+1);
				operStack.push(oper);
				pos = i+1;
			} else if (i == expression.length()-1){
				String v = expression.substring(pos, expression.length());
				if (params!=null && params.get(v)!=null)
					v = (String)params.get(v);
				parmStack.push(v);
			}
			i++;
		}

		//String oper = "";
		
		String ret = "";
		if (operStack.size()>0){
			ExpressionItem root = analyzeOperator(operStack);
			String[] value = new String[parmStack.size()];
			int index = parmStack.size()-1;
			while(parmStack.size()>0){
				value[index] = (String)parmStack.pop();
				index--;
			}
			if (root!=null){
				insertValue(0,value,root);
				ret = root.getValue();
			}
		} else if (parmStack.size()>0){
			ret = (String)parmStack.get(0);
		}
		//ret.add(oper);
		//ret.add(value);
		//System.out.println(operStack.toString());
		
		return ret;
	}
	
	/**
	 * �ѱ��ʽ�еĲ��������������ȼ������ж���������
	 * @param expression
	 * @return
	 * @throws Exception
	 */
	private static ExpressionItem analyzeOperator(Stack expression) throws Exception{
		ExpressionItem expItem = null;
		int i = 0;
		
		Stack parmStack = new Stack();
		while (i < expression.size()){
			Object c = (Object)expression.get(i);
			//�Ӵ��������ڵı��ʽ������һ���Ӵ���
			if (c instanceof ExpressionItem){
				ExpressionItem newExpItem =(ExpressionItem)c;
				if (expItem==null){
					expItem = newExpItem;
				} else if (expItem.hasPriority(newExpItem)>=0){
					ExpressionItem parent = expItem;
					//ѭ�������ϴ��ڵ����ȼ������ڱ��������Ľڵ�
					while (parent!=null){
						if (parent.hasPriority(newExpItem)<0){
							break;
						}else if (parent.getParent()!=null)
							parent = parent.getParent();
						else
							break;
					}
					if (parent.getParent()==null){
						parent.setParent(newExpItem);
						newExpItem.setLeftItem(parent);
					}else {
						newExpItem.setLeftItem(parent.getRightItem());
						parent.setRightItem(newExpItem);
						newExpItem.setParent(parent);
					}
					expItem = newExpItem;
				} else{
					newExpItem.setLeftItem(expItem.getRightItem());
					expItem.setRightItem(newExpItem);
					newExpItem.setParent(expItem);
					expItem = newExpItem;
				}
			} else {
				String oper = (String)expression.get(i);
				if (ExpressionItem.OPERATORS.indexOf(oper)!=-1){
					//��������ţ��������ڵĴ�����һ���Ӵ�������
					if(oper.equals("(")){
						parmStack.push(oper);
					}else if (oper.equals(")")){
						Object op = (Object)parmStack.pop();
						Stack subStr = new Stack();
						while(!op.equals("(")){
							subStr.add(0, op);
							op = (Object)parmStack.pop();
						}
						ExpressionItem subExpItem = analyzeOperator(subStr);
						subExpItem.setSubExpression(true);
						//�������Ӵ����������λ�û����������ڲ��������ѹ���Ӵ�ջ������ֱ�Ӳ������Ĺ���
						if (parmStack.size() !=0)
							parmStack.push(subExpItem);
						else {
							if (expItem == null)
								expItem = subExpItem;
							else{
								expItem.setRightItem(subExpItem);
								subExpItem.setParent(expItem);
								expItem = subExpItem;
							}
						}
					} else if (parmStack.size()==0){
						ExpressionItem newExpItem = new ExpressionItem();
						newExpItem.setItem(oper);
						if (expItem==null){
							expItem = newExpItem;
						} else if (expItem.hasPriority(newExpItem)>=0){
							ExpressionItem parent = expItem;
							//ѭ�������ϴ��ڵ����ȼ������ڱ��������Ľڵ�
							while (parent!=null){
								if (parent.hasPriority(newExpItem)<0){
									break;
								}else if (parent.getParent()!=null)
									parent = parent.getParent();
								else
									break;
							}
							if (parent.getParent()==null){
								parent.setParent(newExpItem);
								newExpItem.setLeftItem(parent);
							}else {
								newExpItem.setLeftItem(parent.getRightItem());
								parent.setRightItem(newExpItem);
								newExpItem.setParent(parent);
							}
							expItem = newExpItem;
						} else{
							newExpItem.setLeftItem(expItem.getRightItem());
							expItem.setRightItem(newExpItem);
							newExpItem.setParent(expItem);
							expItem = newExpItem;
						}
					} else
						parmStack.push(oper);
				}
			}
			i++;
		}
		if (expItem!=null)
			return expItem.getRoot();
		else return null;
	}
	
	/**
	 * �Լӹ����Ķ������������ҽ���ֵ���
	 * @param index
	 * @param value
	 * @param root
	 * @return
	 */
	private static int insertValue(int index ,String[] value,ExpressionItem root){
		if (root.getLeftItem() == null){
			if (index >= value.length) {
				return index;
			}
			ExpressionItem item = new ExpressionItem();
			//System.out.println(root.getItem()+"/left��"+value[index]);
			item.setItem(value[index]);
			root.setLeftItem(item);
			index++;
		} else {
			index = insertValue(index,value,root.getLeftItem());
		}
		
		if (root.getRightItem() == null){
			//System.out.println(root.getItem()+"/right��"+value[index]);
			if (index >= value.length) {
				return index;
			}
			ExpressionItem item = new ExpressionItem();
			item.setItem(value[index]);
			root.setRightItem(item);
			index++;
		} else {
			index = insertValue(index,value,root.getRightItem());
		}
		return index;
	}
	
	public static void main(String[] args){
		String expression = "1+2=3";
		try {
			Map m = new HashMap();
			m.put("task_wf_super_edit",false+"");
			m.put("execute_group_user",true+"");
			boolean b = ExpressionCalculate.calculateBoolean(m,expression);
			System.out.println(ExpressionCalculate.calculate(m,expression));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
