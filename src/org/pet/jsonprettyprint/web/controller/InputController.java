package org.pet.jsonprettyprint.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pet.jsonprettyprint.generic.controller.BaseController;
import org.pet.jsonprettyprint.generic.util.ErrorCode;
import org.pet.jsonprettyprint.service.manager.InputManager;
import org.pet.jsonprettyprint.service.manager.InputManagerImpl;
import org.pet.jsonprettyprint.service.model.InputVO;
import org.pet.jsonprettyprint.web.util.WebConstants;

public class InputController extends BaseController {

	private static final long serialVersionUID = 8699706225925931127L;
	
	private InputManager inputManager;

	@Override
	public void init() throws ServletException {
		inputManager = new InputManagerImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = getAction(request);
		if(WebConstants.GET_SAVED_FILE.equals(action)) {
			getSavedFile(request, response);
		} else {
			forward(request, response);
		}
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = getAction(request);
		if(WebConstants.DO_PRETTY_PRINT.equals(action)) {
			doPrettyPrint(request, response);
		} else if (WebConstants.DO_SAVE_JSON.equals(action)) {
			doSaveJson(request, response);
		} else if (WebConstants.DO_LOAD_FILE.equals(action)) {
			doLoadFile(request, response);
		} else {
			forward(request, response);
		}
	}
	
	private void doLoadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String nameStr = getStringParameter(request, "name");
		InputVO inputVO = new InputVO();
		inputVO.setName(nameStr);
		inputVO = inputManager.loadSavedFile(inputVO);
		if(!inputVO.isStatus()) {
			inputVO.setStatusMsg(getActionMessage(request, "label.error.generic.load"));
		}
		printResponse(response, inputVO);
	}
	
	private void getSavedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		InputVO inputVO = new InputVO();
		inputVO = inputManager.getFileList(inputVO);
		printResponse(response, inputVO);
	}
	
	private void doSaveJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String nameStr = getStringParameter(request, "name");
		String jsonStr = getStringParameter(request, "json");
		InputVO inputVO = new InputVO();
		inputVO.setInput(jsonStr);
		inputVO.setName(nameStr);
		inputVO = inputManager.saveJson(inputVO);
		if(!inputVO.isStatus()) {
			int errorCode = inputVO.getErrorCode();
			String errorMsg = null;
			if(errorCode == ErrorCode.FILE_NAME_EXIST) {
				errorMsg = getActionMessage(request, "label.error.filename.exist");
			} else {
				errorMsg = getActionMessage(request, "label.error.generic.save");
			}
			inputVO.setStatusMsg(errorMsg);
		}
		printResponse(response, inputVO);
	}

	private void doPrettyPrint(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String jsonStr = getStringParameter(request, "json");
		InputVO inputVO = new InputVO();
		inputVO.setInput(jsonStr);
		inputVO = inputManager.processJsonInput(inputVO);
		if(!inputVO.isStatus()) {
			inputVO.setStatusMsg(getActionMessage(request, "label.error.invalid.format"));
		}
		inputVO.setInput(null);
		printResponse(response, inputVO);
	}
	
}
