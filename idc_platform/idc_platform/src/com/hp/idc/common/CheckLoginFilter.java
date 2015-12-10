/*
 * @(#)CheckLoginFilter.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.common;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;

/**
 * get by http://gundumw100.iteye.com/blog/698750
 * 
 * 用于检测用户是否登陆的过滤器，如果未登录，则重定向到指的登录页面 
 * 配置参数 checkSessionKey 需检查的在 Session 中保存的关键字
 * redirectURL 如果用户未登录，则重定向到指定的页面，URL不包括 ContextPath notCheckURLList
 * 不做检查的URL列表，以分号分开，并且 URL 中不包括 ContextPath
 */
public class CheckLoginFilter implements Filter {
	
	protected FilterConfig filterConfig = null;

	private String redirectURL = null;
	
	private List<String> notCheckURLList = new ArrayList<String>();

	private String sessionKey = Constant.SESSION_LOGIN;

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		HttpSession session = request.getSession();

		Map<String, String[]> em = request.getParameterMap();
		StringBuffer sb = new StringBuffer(request.getServletPath());
		sb.append("?");
		for(Map.Entry<String, String[]> entry:em.entrySet()){
			int i = 0;
			for(String s : entry.getValue()){
				if(i > 0)
					sb.append("&");
				sb.append(entry.getKey()+"="+s);
				i++;
			}
			sb.append("&");
		}
		String s = sb.toString();
		String uri = s.substring(0,s.length()-1);
		
		if(session.getAttribute(sessionKey) == null){
			if (!checkRequestURIIntNotFilterList(uri)){
				System.out.println(uri);
				if (request.getParameter("home_page") != null && request.getParameter("home_page").length() > 0) {
					session.setAttribute(Constant.REIDRECT_URL, request.getParameter("home_page"));
				} else {
					session.setAttribute(Constant.REIDRECT_URL, uri);
				}
				response.sendRedirect(request.getContextPath() + redirectURL);
				return;
			}
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	public void destroy() {
		notCheckURLList.clear();
	}

	private boolean checkRequestURIIntNotFilterList(String uri) {
		if(notCheckURLList == null || notCheckURLList.isEmpty())
			return false;
		for(String u:notCheckURLList){
			System.out.println(u+"-----"+uri);
			if(uri.startsWith(u)){
				return true;
			}
		}
		return false;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		redirectURL = filterConfig.getInitParameter("redirectURL");
		
		String notCheckURLListStr = filterConfig
				.getInitParameter("notCheckURLList");

		if (notCheckURLListStr != null) {
			String[] st = notCheckURLListStr.split(";");
			notCheckURLList.clear();
			for(String s : st){
				notCheckURLList.add(s);
			}
		}
	}
}
