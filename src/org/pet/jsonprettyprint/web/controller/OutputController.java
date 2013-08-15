package org.pet.jsonprettyprint.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pet.jsonprettyprint.generic.controller.BaseController;

public class OutputController extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1185693257424703474L;

	@Override
	public void init() throws ServletException {
		
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		forward(request, response);
	}

}
