package eu.pericles.processcompiler.ecosystem;

public abstract class Slot {
	
	private String id;
	private String name;
	private String description;
	private String type;
	
	//--------------- GETTERS AND SETTERS ----------------//
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	

}
