package com.hp.idc.resm.ui;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.idc.resm.cache.GlobalCache;
import com.hp.idc.resm.util.CharsetUtil;

/**
 * 图标的Servlet 访问时可以用/resm/IconLibray/图标ID来访问图标
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class IconServlet extends HttpServlet {
	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = 808519754269022056L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doRequest(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doRequest(request, response);
	}

	/**
	 * GET 或 POST 方法的具体处理
	 * 
	 * @param request
	 *            请求
	 * @param response
	 *            输出
	 * @throws IOException
	 *             IO异常
	 */
	private void doRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 取url的最后一部分，即图标ID
		String n = request.getRequestURI();
		int pos = n.lastIndexOf('/');
		n = n.substring(pos + 1);
		Icon i = GlobalCache.iconCache.get(n);
		response.setCharacterEncoding(CharsetUtil.UTF_8);

		// 找不到时用默认的图标
		if (i == null)
			i = GlobalCache.iconCache.get("0");

		// 转发
		// System.out.println(i.getPath());
		String url = CharsetUtil.changeCharset(i.getPath(), CharsetUtil.UTF_8,
				CharsetUtil.ISO_8859_1);
		url = java.net.URLEncoder.encode(i.getPath(), CharsetUtil.UTF_8)
				.replace("%2F", "/").replace("+", "%20");
		response.sendRedirect(url);
	}

}
