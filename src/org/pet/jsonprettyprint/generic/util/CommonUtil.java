package org.pet.jsonprettyprint.generic.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class CommonUtil {
	
	public static final String NEW_LINE = System.getProperty("line.separator");
	
	public static final int toInt(String s) {
		int i = 0;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException e) {
		}
		return i;
	}

	public static final boolean toBoolean(String s) {
		s = s.toLowerCase();
		return "true".equals(s) || "1".equals(s);
	}

	public static final boolean isNullString(String s) {
		return s == null || s.equals("");
	}

	public static String combineString(Object... objects) {
		String returnStr = null;
		if (objects != null) {
			StringBuilder stringBuilder = new StringBuilder();
			for (Object obj : objects) {
				stringBuilder.append(obj);
			}
			returnStr = stringBuilder.toString();
		}
		return returnStr;
	}
	
	@SuppressWarnings("unchecked")
	public static final Map<String, Object> getJsonObjectFromString(String jsonInput) {
		Gson gson = new Gson();
		Map<String, Object> jsonObj = null;
		try {
			jsonObj = gson.fromJson(jsonInput, Map.class);
		} catch (JsonSyntaxException e) {
		}
		return jsonObj;
	}
	
	public static final boolean isValidJsonFormat(String jsonInput) {
		return getJsonObjectFromString(jsonInput) != null;
	}
	
	public static final boolean isStringEqual(String str1, String str2) {
		if(str1 == null && str2 == null) {
			return true;
		} else {
			if(str1 != null && str1.equals(str2)) {
				return true;
			} else {
				
			}
			return false;
		}
	}
	
	public static final void writeTextFile(File file, String content) throws IOException {
		FileWriter fileWriter = null;
		BufferedWriter writer = null;
		try {
			fileWriter = new FileWriter(file);
			writer = new BufferedWriter(fileWriter);
			writer.write(content);
			writer.flush();
		} finally {
			try {
				if(writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(fileWriter != null) {
					fileWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static final String readTextFileAsString(File file) throws IOException {
		FileReader fileReader = null;
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			fileReader = new FileReader(file);
			reader = new BufferedReader(fileReader);
			String line;
			while((line = reader.readLine()) != null) {
				sb.append(line).append(NEW_LINE);
			}
		} finally {
			try {
				if(reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(fileReader != null) {
					fileReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
