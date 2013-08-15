package org.pet.jsonprettyprint.generic.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pet.jsonprettyprint.generic.model.BaseVO;
import org.pet.jsonprettyprint.generic.util.ActionMessage;
import org.pet.jsonprettyprint.generic.util.CommonUtil;
import org.pet.jsonprettyprint.web.util.WebConstants;

import com.google.gson.Gson;

public abstract class BaseController extends HttpServlet {

	private static final long serialVersionUID = 6864627056580842141L;
	
	private ActionMessage actionMessage;
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public abstract void init() throws ServletException;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected abstract void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected abstract void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
	protected void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String page = getInitParameter("page");
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
		dispatcher.forward(request, response);
	}
	
	protected String getAction(HttpServletRequest request) {
		return request.getParameter(WebConstants.ACTION_NAME);
	}

	protected String getStringParameter(HttpServletRequest request, String key) {
		return request.getParameter(key);
	}
	
	protected boolean getBooleanParameter(HttpServletRequest request, String key) {
		return CommonUtil.toBoolean(getStringParameter(request, key));
	}
	
	protected int getIntegerParameter(HttpServletRequest request, String key) {
		return CommonUtil.toInt(getStringParameter(request, key));
	}
	
	protected void printResponse(HttpServletResponse response, BaseVO baseVO) throws IOException {
		Gson gson = new Gson();
		String string = gson.toJson(baseVO);
		PrintWriter out = response.getWriter();
		response.setHeader("Content-type", "application/json");
		response.setContentLength(string!=null?string.length():0);
	    out.println(string);
	    out.close();
	}
	
	protected String getActionMessage(HttpServletRequest request, String key) {
		return getActionMessage(request, key, null);
	}
	
	protected String getActionMessage(HttpServletRequest request, String key, String defaultMsg) {
		if(actionMessage == null) {
			String name = request.getServletContext().getInitParameter("javax.servlet.jsp.jstl.fmt.localizationContext");
			String filename = "/" + name + ".properties";
			actionMessage = ActionMessage.getInstance(filename);
		}
		return actionMessage.getMessage(key, defaultMsg);
	}
	
}
