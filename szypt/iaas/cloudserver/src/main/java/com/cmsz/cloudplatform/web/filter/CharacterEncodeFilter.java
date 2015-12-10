package com.cmsz.cloudplatform.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.hp.util.StringUtil;

public class CharacterEncodeFilter implements Filter {

	private String charset = "UTF-8"; //default value 
	
	public void init(FilterConfig filterConfig) throws ServletException {
		if(StringUtil.isNotEmpty(filterConfig.getInitParameter("charset"))){
			charset =  filterConfig.getInitParameter("charset").trim();
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding(charset);
		response.setCharacterEncoding(charset);
		chain.doFilter(request, response);

	}

	public void destroy() {
	}

}