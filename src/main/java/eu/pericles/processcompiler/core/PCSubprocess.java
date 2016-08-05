package eu.pericles.processcompiler.core;

import java.util.HashMap;

public class PCSubprocess {
	
	private String id;
	private HashMap<String, String> dataInputMap = new HashMap<>();
	private HashMap<String, String> dataOutputMap = new HashMap<>();
	
	public PCSubprocess(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String processID) {
		this.id = processID;
	}

	public HashMap<String, String> getDataInputMap() {
		return dataInputMap;
	}

	public void setDataInputMap(HashMap<String, String> dataInputMap) {
		this.dataInputMap = dataInputMap;
	}

	public HashMap<String, String> getDataOutputMap() {
		return dataOutputMap;
	}

	public void setDataOutputMap(HashMap<String, String> dataOutputMap) {
		this.dataOutputMap = dataOutputMap;
	}

}
