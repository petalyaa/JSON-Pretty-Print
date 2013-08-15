package org.pet.jsonprettyprint.service.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.pet.jsonprettyprint.generic.util.CommonUtil;
import org.pet.jsonprettyprint.generic.util.PropertiesUtil;

import com.google.gson.Gson;

/**
 * Class Description	: 
 * Created By			: Khairul Ikhwan
 * Created Date			: Jul 31, 2013
 * Current Version		: 1.0
 * Latest Changes By	: 
 * Latest Changes Date	: 
 */
public class JSONFileMappingUtil {
	
	private static final Properties props = PropertiesUtil.loadFromClassPath("/json.properties", JSONFileMappingUtil.class);
	
	private static final String APPLICATION_DATA_PATH = props.getProperty("application.data");
	
	private static final File DATA_DIRECTORY;
	
	private static final File SAVE_DIRECTORY;
	
	private static final File MAPPING_FILE;
	static {
		File applicationDataFile = new File(APPLICATION_DATA_PATH);
		if(!applicationDataFile.exists()) {
			applicationDataFile.mkdirs();
		}
		DATA_DIRECTORY = new File(applicationDataFile, "data");
		if(!DATA_DIRECTORY.exists()) {
			DATA_DIRECTORY.mkdirs();
		}
		SAVE_DIRECTORY = new File(applicationDataFile, "save");
		if(!SAVE_DIRECTORY.exists()) {
			SAVE_DIRECTORY.mkdirs();
		}
		MAPPING_FILE = new File(DATA_DIRECTORY, "mapping.p");
		if(!MAPPING_FILE.exists()) {
			try {
				MAPPING_FILE.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static final String readJsonFile(String filename) {
		File file = new File(SAVE_DIRECTORY, filename);
		String content = null;
		try {
			content = CommonUtil.readTextFileAsString(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	public static final boolean saveFile(String filename, String content) {
		boolean isSuccess = false;
		File outputFile = new File(SAVE_DIRECTORY, filename);
		if(outputFile.exists()) {
			outputFile.delete();
		}
		try {
			outputFile.createNewFile();
			CommonUtil.writeTextFile(outputFile, content);
			isSuccess = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}
	
	public static final boolean addMapping(JsonMapping jsonMapping) {
		boolean isSuccess = false;
		Mapping mapping = getMapping();
		if(mapping == null) {
			mapping = new Mapping();
		}
		List<JsonMapping> jsonMappingList = mapping.getMapping();
		if(jsonMappingList == null) {
			jsonMappingList = new ArrayList<JsonMapping>();
		}
		jsonMappingList.add(jsonMapping);
		mapping.setMapping(jsonMappingList);
		Gson gson = new Gson();
		String jsonStr = gson.toJson(mapping);
		try {
			CommonUtil.writeTextFile(MAPPING_FILE, jsonStr);
			isSuccess = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}
	
	public static final String generateFileName() {
		StringBuilder sb = new StringBuilder();
		sb.append(System.currentTimeMillis()).append(".mp");
		return sb.toString();
	}
	
	public static final Mapping getMapping() {
		Mapping mapping = null;
		try {
			String textString = CommonUtil.readTextFileAsString(MAPPING_FILE);
			if(!CommonUtil.isNullString(textString) && CommonUtil.isValidJsonFormat(textString)) {
				Gson gson = new Gson();
				mapping = gson.fromJson(textString, Mapping.class);
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mapping;
	}
	
	public static final boolean isFileNameExist(String filename) {
		boolean isNameExist = false;
		try {
			String textString = CommonUtil.readTextFileAsString(MAPPING_FILE);
			if(!CommonUtil.isNullString(textString) && CommonUtil.isValidJsonFormat(textString)) {
				Gson gson = new Gson();
				Mapping mapping = gson.fromJson(textString, Mapping.class);
				if(mapping != null && mapping.getMapping() != null) {
					for(JsonMapping jsonMapping : mapping.getMapping()) {
						String name = jsonMapping.getName();
						if(CommonUtil.isStringEqual(name, filename)) {
							isNameExist = true;
							break;
						}
					}
				}
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isNameExist;
	}

}
