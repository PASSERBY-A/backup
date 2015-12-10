package com.hp.idc.resm.ui;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.idc.resm.cache.GlobalCache;
import com.hp.idc.resm.util.CharsetUtil;

/**
 * ͼ���Servlet ����ʱ������/resm/IconLibray/ͼ��ID������ͼ��
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class IconServlet extends HttpServlet {
	/**
	 * ���л�UID
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
	 * GET �� POST �����ľ��崦��
	 * 
	 * @param request
	 *            ����
	 * @param response
	 *            ���
	 * @throws IOException
	 *             IO�쳣
	 */
	private void doRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// ȡurl�����һ���֣���ͼ��ID
		String n = request.getRequestURI();
		int pos = n.lastIndexOf('/');
		n = n.substring(pos + 1);
		Icon i = GlobalCache.iconCache.get(n);
		response.setCharacterEncoding(CharsetUtil.UTF_8);

		// �Ҳ���ʱ��Ĭ�ϵ�ͼ��
		if (i == null)
			i = GlobalCache.iconCache.get("0");

		// ת��
		// System.out.println(i.getPath());
		String url = CharsetUtil.changeCharset(i.getPath(), CharsetUtil.UTF_8,
				CharsetUtil.ISO_8859_1);
		url = java.net.URLEncoder.encode(i.getPath(), CharsetUtil.UTF_8)
				.replace("%2F", "/").replace("+", "%20");
		response.sendRedirect(url);
	}

}
