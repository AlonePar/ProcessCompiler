package eu.pericles.modelcompiler.generic;

import eu.pericles.modelcompiler.common.ProcessElement;

public class Gateway extends ProcessElement {
	
	public enum Type {
		PARALLEL
	}

	private Type type;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	

}
