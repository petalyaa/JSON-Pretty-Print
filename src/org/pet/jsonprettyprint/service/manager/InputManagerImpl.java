package org.pet.jsonprettyprint.service.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.pet.jsonprettyprint.generic.manager.GenericManager;
import org.pet.jsonprettyprint.generic.util.CommonUtil;
import org.pet.jsonprettyprint.generic.util.ErrorCode;
import org.pet.jsonprettyprint.service.model.InputVO;
import org.pet.jsonprettyprint.service.model.JSONFile;
import org.pet.jsonprettyprint.service.util.JSONFileMappingUtil;
import org.pet.jsonprettyprint.service.util.JsonMapping;
import org.pet.jsonprettyprint.service.util.Mapping;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class InputManagerImpl extends GenericManager implements InputManager {

	@Override
	public InputVO processJsonInput(InputVO inputVO) {
		String jsonInputStr = inputVO.getInput();
		if(CommonUtil.isNullString(jsonInputStr)) {
			inputVO.setStatus(false);
		} else {
			Map<String, Object> jsonObj = CommonUtil.getJsonObjectFromString(jsonInputStr);
			if(jsonObj == null) {
				inputVO.setStatus(false);
			} else {
				String unorderedHTML = constructUnorderedList(jsonInputStr);
				inputVO.setOutput(unorderedHTML);
				inputVO.setStatus(true);
			}
		}
		return inputVO;
	}
	
	private String constructUnorderedList(String jsonStr) {
		Gson gson = new Gson();
		JsonObject jsonObj = gson.fromJson(jsonStr, JsonObject.class);
		return constructJsonObject(jsonObj, null, true);
	}
	
	private String constructJsonObject(JsonObject jsonObj, String parentPath, boolean isFirstNode) {
		StringBuilder sb = new StringBuilder();
		sb.append("<ul");
		if(isFirstNode) {
			sb.append(" class='json-tree-output'");
		}
		sb.append(">");
		for(Entry<String, JsonElement> jsonEntry : jsonObj.entrySet()) {
			String key = jsonEntry.getKey();
			JsonElement value = jsonEntry.getValue();
			StringBuilder liSb = new StringBuilder();
			String path = parentPath != null ? parentPath + "." + key : key;
			liSb.append("<li class='closed hoverJson' data-path='").append(path).append("' data-first-node='").append(isFirstNode).append("' data-key='").append(key).append("'><span class='json-key leaf'>").append(key).append("</span> : ");
			if(value.isJsonPrimitive()) {
				liSb.append(value.getAsString());
			} else {
				if(value.isJsonArray()) {
					String arrayStr = constructJsonArray(value.getAsJsonArray(), path);
					liSb.append("[<span class='json-array-size'>").append(value.getAsJsonArray().size()).append("</span>").append(arrayStr).append("]");
				} else if (value.isJsonObject()) {
					String objStr = constructJsonObject(value.getAsJsonObject(), path, false);
					liSb.append(objStr);
				} else {
					liSb.append(value);
				}
			}
			liSb.append("</li>");
			sb.append(liSb);
		}
		sb.append("</ul>");
		return sb.toString();
	}
	
	private String constructJsonArray(JsonArray jsonArray, String parentPath) {
		StringBuilder sb = new StringBuilder();
		sb.append("<ul>");
		for(int i = 0; i < jsonArray.size(); i++) {
			JsonElement jsonArrayElem = jsonArray.get(i);
			StringBuilder jsonArraySb = new StringBuilder();
			String path = parentPath + "[" + i + "]";
			jsonArraySb.append("<li class='closed hoverJson' data-key='").append(path).append("'>&nbsp;[").append(i).append("] ");
			if(jsonArrayElem.isJsonPrimitive()) {
				jsonArraySb.append(jsonArrayElem.getAsString());
			} else if (jsonArrayElem.isJsonArray()) {
				jsonArraySb.append(constructJsonArray(jsonArrayElem.getAsJsonArray(), path));
			} else if (jsonArrayElem.isJsonObject()) {
				jsonArraySb.append(constructJsonObject(jsonArrayElem.getAsJsonObject(), path, false));
			} else {
				jsonArraySb.append(jsonArrayElem);
			}
			jsonArraySb.append("</li>");
			sb.append(jsonArraySb);
		}
		sb.append("</ul>");
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see org.pet.jsonprettyprint.service.manager.InputManager#saveJson(org.pet.jsonprettyprint.service.model.InputVO)
	 */
	@Override
	public InputVO saveJson(InputVO inputVO) {
		String name = inputVO.getName();
		String jsonInput = inputVO.getInput();
		if(JSONFileMappingUtil.isFileNameExist(name)) {
			inputVO.setErrorCode(ErrorCode.FILE_NAME_EXIST);
		} else {
			String filenameStr = JSONFileMappingUtil.generateFileName();
			JsonMapping jsonMapping = new JsonMapping();
			jsonMapping.setCreatedDate(System.currentTimeMillis());
			jsonMapping.setFilename(filenameStr);
			jsonMapping.setName(name);
			jsonMapping.setSize(jsonInput.getBytes().length);
			if(JSONFileMappingUtil.addMapping(jsonMapping)) { // Save file to mapping file
				if(JSONFileMappingUtil.saveFile(filenameStr, jsonInput)) {
					inputVO.setStatus(true);
				} else {
					inputVO.setErrorCode(ErrorCode.GENERIC_SAVE_ERROR);
				}
			} else {
				inputVO.setErrorCode(ErrorCode.GENERIC_SAVE_ERROR);
			}
		}
		return inputVO;
	}

	/* (non-Javadoc)
	 * @see org.pet.jsonprettyprint.service.manager.InputManager#getFileList(org.pet.jsonprettyprint.service.model.InputVO)
	 */
	@Override
	public InputVO getFileList(InputVO inputVO) {
		List<JSONFile> jsonFileList = new ArrayList<JSONFile>();
		Mapping mapping = JSONFileMappingUtil.getMapping();
		if(mapping != null) {
			List<JsonMapping> jsonMapping = mapping.getMapping();
			for(JsonMapping mappingFile : jsonMapping) {
				JSONFile jsonFile = new JSONFile();
				jsonFile.setCreatedDate(mappingFile.getCreatedDate());
				jsonFile.setName(mappingFile.getName());
				jsonFile.setSize(mappingFile.getSize());
				jsonFileList.add(jsonFile);
			}
		}
		inputVO.setData(jsonFileList);
		inputVO.setStatus(true);
		return inputVO;
	}

	/* (non-Javadoc)
	 * @see org.pet.jsonprettyprint.service.manager.InputManager#loadSavedFile(org.pet.jsonprettyprint.service.model.InputVO)
	 */
	@Override
	public InputVO loadSavedFile(InputVO inputVO) {
		String nameStr = inputVO.getName();
		String jsonStr = null;
		Mapping mapping = JSONFileMappingUtil.getMapping();
		if(mapping != null) {
			List<JsonMapping> jsonMapping = mapping.getMapping();
			String filenameStr = null;
			for(JsonMapping mappingFile : jsonMapping) {
				if(mappingFile.getName().equals(nameStr)) {
					filenameStr = mappingFile.getFilename();
					break;
				}
			}
			if(filenameStr != null) {
				jsonStr = JSONFileMappingUtil.readJsonFile(filenameStr);
				inputVO.setInput(jsonStr);
			}
		}
		inputVO.setStatus(jsonStr != null);
		return inputVO;
	}

}
