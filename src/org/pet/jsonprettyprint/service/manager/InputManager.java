package org.pet.jsonprettyprint.service.manager;

import org.pet.jsonprettyprint.generic.manager.Manager;
import org.pet.jsonprettyprint.service.model.InputVO;

public interface InputManager extends Manager {
	
	public InputVO processJsonInput(InputVO inputVO);
	
	public InputVO saveJson(InputVO inputVO);
	
	public InputVO getFileList(InputVO inputVO);
	
	public InputVO loadSavedFile(InputVO inputVO);

}
