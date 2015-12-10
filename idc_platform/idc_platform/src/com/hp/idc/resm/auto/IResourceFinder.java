package com.hp.idc.resm.auto;

import java.util.List;

import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.Parameter;

/**
 * 定义了自动发现插件的基本信息，每个插件必须继承此信息
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IResourceFinder {

	/**
	 * 获取插件名称
	 * 
	 * @return 插件名称
	 */
	public String getName();

	/**
	 * 获取插件需要的参数
	 * 
	 * @return 插件需要的参数列表
	 */
	public List<Parameter> getParameters();

	/**
	 * 获取插件应用到的模型
	 * 
	 * @return 插件应用到的模型列表
	 */
	public List<Model> getApplyModels();

	/**
	 * 指示是否可以自动运行
	 * 
	 * @return true为自动运行，false为手动运行
	 */
	public boolean isAutoRun();

	/**
	 * 获取自动运行时间定义
	 * 
	 * @return 自动运行时间定义
	 */
	public String getAutoRunDefine();

	/**
	 * 处理函数
	 */
	public void process();
}
