package eu.pericles.modelcompiler.jbpm;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import eu.pericles.modelcompiler.bpmn.BpmnProcess;
import eu.pericles.modelcompiler.bpmn.ExternalItems.ItemDefinition;
import eu.pericles.modelcompiler.bpmn.ExternalItems.Message;
import eu.pericles.modelcompiler.jbpm.Diagram.Diagram;

@XStreamAlias("bpmn2:definitions")
public class JbpmFile {

	@XStreamAsAttribute
	@XStreamAlias("bpmn2:process")
	private BpmnProcess bpmnProcess;
	@XStreamAsAttribute
	@XStreamAlias("bpmndi:BPMNDiagram")
	private Diagram diagram;
	@XStreamImplicit
	private List<Message> messages;
	@XStreamImplicit
	private List<ItemDefinition> itemDefinitions;

	public JbpmFile() {
		init();
	}

	private void init() {
		itemDefinitions = new ArrayList<ItemDefinition>();
		messages = new ArrayList<Message>();
	}


	public void organiseInfo() {
		/* When reading a process from a bpmn file with DOM, those lists of elements that are not in the file remain 
		 * pointing to NULL. With checkAndComplete(), those lists are created as empty lists.
		 */
		getBpmnProcess().checkAndComplete();

		/* Add lists of external variables */
		addExternalVariablesToBpmnProcess();
	}

	private void addExternalVariablesToBpmnProcess() {
		if (getItemDefinitions() == null) 
			getBpmnProcess().setItemDefinitions(new ArrayList<ItemDefinition>());
		else 
			getBpmnProcess().setItemDefinitions(getItemDefinitions());
		if (getMessages() == null)
			getBpmnProcess().setMessages(new ArrayList<Message>());
		else
			getBpmnProcess().setMessages(getMessages());
	}



	//---- Getters and Setters ----//

	public BpmnProcess getBpmnProcess() {
		return bpmnProcess;
	}
	public void setBpmnProcess(BpmnProcess bpmnProcess) {
		this.bpmnProcess = bpmnProcess;
	}
	public Diagram getDiagram() {
		return diagram;
	}
	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;
	}
	public List<Message> getMessages() {
		return messages;
	}
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	public List<ItemDefinition> getItemDefinitions() {
		return itemDefinitions;
	}
	public void setItemDefinitions(List<ItemDefinition> itemDefinitions) {
		this.itemDefinitions = itemDefinitions;
	}

}