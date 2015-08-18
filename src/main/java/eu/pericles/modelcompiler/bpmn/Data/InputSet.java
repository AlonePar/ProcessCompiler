package eu.pericles.modelcompiler.bpmn.Data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import eu.pericles.modelcompiler.bpmn.BpmnElement;

@XStreamAlias("bpmn2:inputSet")
public class InputSet implements BpmnElement {
	
	@XStreamAsAttribute
	private String id;
	@XStreamAlias("bpmn2:dataInputRefs")
	String reference;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}

}
