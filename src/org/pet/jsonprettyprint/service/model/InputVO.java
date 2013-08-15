package org.pet.jsonprettyprint.service.model;

import java.util.List;

import org.pet.jsonprettyprint.generic.model.BaseVO;

public class InputVO extends BaseVO {

	private static final long serialVersionUID = 248259324397171392L;
	
	private String input;
	
	private String output;
	
	private String name;
	
	private List<JSONFile> data;

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the data
	 */
	public List<JSONFile> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<JSONFile> data) {
		this.data = data;
	}

}
