package org.pet.jsonprettyprint.generic.model;

import java.io.Serializable;

public class BaseVO implements Serializable {
	
	private static final long serialVersionUID = 8822651673074428901L;

	private boolean status;
	
	private String statusMsg;
	
	private int errorCode;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
