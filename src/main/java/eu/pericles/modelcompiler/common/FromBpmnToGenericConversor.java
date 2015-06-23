package eu.pericles.modelcompiler.common;

import java.util.HashMap;
import java.util.Map;

import eu.pericles.modelcompiler.bpmn.BpmnProcess;
import eu.pericles.modelcompiler.bpmn.Activities.ScriptTask;
import eu.pericles.modelcompiler.bpmn.Activities.Subprocess;
import eu.pericles.modelcompiler.bpmn.Events.EndEvent;
import eu.pericles.modelcompiler.bpmn.Events.StartEvent;
import eu.pericles.modelcompiler.bpmn.ExternalItems.ItemDefinition;
import eu.pericles.modelcompiler.bpmn.ExternalItems.Message;
import eu.pericles.modelcompiler.bpmn.ExternalItems.Signal;
import eu.pericles.modelcompiler.bpmn.Flows.SequenceFlow;
import eu.pericles.modelcompiler.bpmn.Gateways.ParallelGateway;
import eu.pericles.modelcompiler.generic.Activity;
import eu.pericles.modelcompiler.generic.Event;
import eu.pericles.modelcompiler.generic.ExternalItem;
import eu.pericles.modelcompiler.generic.Flow;
import eu.pericles.modelcompiler.generic.Gateway;
import eu.pericles.modelcompiler.generic.Process;

public class FromBpmnToGenericConversor {

	private BpmnProcess bpmnProcess;
	private Process genericProcess;
	private Map<String,String> mapBpmnIDtoGenericUID;

	public FromBpmnToGenericConversor() {
		bpmnProcess = new BpmnProcess();
		genericProcess = new Process();
		mapBpmnIDtoGenericUID = new HashMap<String,String>();
	}

	public void convertFromBpmnToGeneric(BpmnProcess bpmnProcess) {
		
		setBpmnProcess(bpmnProcess);
		convertExternalItems(getBpmnProcess(), getGenericProcess());
		convertProcessFromBpmnToGeneric(getBpmnProcess(), getGenericProcess());
	}

	private void convertExternalItems(BpmnProcess bpmnProcess, Process genericProcess) {
		convertItemsFromBpmnToGeneric(bpmnProcess, genericProcess);
		convertSignalsFromBpmnToGeneric(bpmnProcess, genericProcess);
		convertMessagesFromBpmnToGeneric(bpmnProcess, genericProcess);
		
	}

	private void convertItemsFromBpmnToGeneric(BpmnProcess bpmnProcess, Process genericProcess) {
		for (ItemDefinition itemDefinition : bpmnProcess.getItemDefinitions()) {
			
			ExternalItem item = new ExternalItem();
			item.setType(ExternalItem.Type.ITEM);
			genericProcess.addExternalItem(item);
			
			mapBpmnIDtoGenericUID.put(itemDefinition.getId(), item.getUid());
		}
		
	}

	private void convertSignalsFromBpmnToGeneric(BpmnProcess bpmnProcess2, Process genericProcess2) {
		for (Signal signal : bpmnProcess.getSignals()) {

			ExternalItem item = new ExternalItem();
			item.setType(ExternalItem.Type.SIGNAL);
			genericProcess.addExternalItem(item);

			mapBpmnIDtoGenericUID.put(signal.getId(), item.getUid());
		}

	}

	private void convertMessagesFromBpmnToGeneric(BpmnProcess bpmnProcess, Process genericProcess) {
		for (Message message : bpmnProcess.getMessages()) {
			
			ExternalItem item = new ExternalItem();
			item.setType(ExternalItem.Type.MESSAGE);
			item.setReference(mapBpmnIDtoGenericUID.get(message.getItemRef()));
			genericProcess.addExternalItem(item);
			
			mapBpmnIDtoGenericUID.put(message.getId(), item.getUid());
		}
		
	}

	private void convertProcessFromBpmnToGeneric(BpmnProcess bpmnProcess, Process genericProcess) {
		
		genericProcess.setName(bpmnProcess.getName());
		genericProcess.setSource(bpmnProcess.getId());	
		
		convertActivitiesFromBpmnToGeneric(bpmnProcess, genericProcess);
		convertEventsFromBpmnToGeneric(bpmnProcess, genericProcess);
		convertGatewaysFromBpmnToGeneric(bpmnProcess, genericProcess);
		convertSubprocessesFromBpmnToGeneric(bpmnProcess, genericProcess);
		convertFlowsFromBpmnToGeneric(bpmnProcess, genericProcess);
	}

	private void convertActivitiesFromBpmnToGeneric(BpmnProcess bpmnProcess, Process genericProcess) {		
		for (ScriptTask scriptTask : bpmnProcess.getScriptTasks()) {
			
			Activity activity = new Activity();
			activity.setName(scriptTask.getName());
			activity.setType(Activity.Type.SCRIPT);
			genericProcess.addActivity(activity);	
			
			mapBpmnIDtoGenericUID.put(scriptTask.getId(), activity.getUid());
		}
	}

	private void convertEventsFromBpmnToGeneric(BpmnProcess bpmnProcess, Process genericProcess) {
		for (StartEvent startEvent : bpmnProcess.getStartEvents()) {	
			
			Event event = new Event();
			if (startEvent.getType() == StartEvent.Type.NONE) {
				event.setType(Event.Type.NONE_START);
			}
			if (startEvent.getType() == StartEvent.Type.SIGNAL) {
				event.setType(Event.Type.SIGNAL_START);
				event.setRef(mapBpmnIDtoGenericUID.get(startEvent.getSignalEventDefinition().getSignalRef()));
			}
			if (startEvent.getType() == StartEvent.Type.MESSAGE) {
				event.setType(Event.Type.MESSAGE_START);
				event.setRef(mapBpmnIDtoGenericUID.get(startEvent.getMessageEventDefinition().getMessageRef()));
			}
			if (startEvent.getType() == StartEvent.Type.TIMER) {
				event.setType(Event.Type.TIMER_START);
			}
			genericProcess.addEvent(event);
			
			mapBpmnIDtoGenericUID.put(startEvent.getId(), event.getUid());
		}
		for (EndEvent endEvent : bpmnProcess.getEndEvents()) {
			
			Event event = new Event();
			event.setType(Event.Type.NONE_END);
			genericProcess.addEvent(event);
			
			mapBpmnIDtoGenericUID.put(endEvent.getId(), event.getUid());
		}
	}

	private void convertGatewaysFromBpmnToGeneric(BpmnProcess bpmnProcess, Process genericProcess) {
		for (ParallelGateway parallelGateway : bpmnProcess.getParallelGateways()) {
			
			Gateway gateway = new Gateway();
			if (parallelGateway.getDirection().equals("Converging"))
				gateway.setType(Gateway.Type.CONVERGING_PARALLEL);
			else
				gateway.setType(Gateway.Type.DIVERGING_PARALLEL);
			genericProcess.addGateway(gateway);
			
			mapBpmnIDtoGenericUID.put(parallelGateway.getId(), gateway.getUid());
			
		}
	}

	private void convertFlowsFromBpmnToGeneric(BpmnProcess bpmnProcess, Process genericProcess) {
		for (SequenceFlow sequenceFlow : bpmnProcess.getSequenceFlows()) {
			
			Flow flow = new Flow();
			flow.setFrom(mapBpmnIDtoGenericUID.get(sequenceFlow.getSource()));
			flow.setTo(mapBpmnIDtoGenericUID.get(sequenceFlow.getTarget()));
			genericProcess.addFlow(flow);
			
			mapBpmnIDtoGenericUID.put(sequenceFlow.getId(), flow.getUid());
		}
	}

	private void convertSubprocessesFromBpmnToGeneric(BpmnProcess bpmnProcess, Process genericProcess) {
		for (Subprocess bpmnSubprocess : bpmnProcess.getSubprocesses()) {
			
			Process genericSubprocess = new Process();			
			convertProcessFromBpmnToGeneric(bpmnSubprocess, genericSubprocess);			
			genericProcess.addSubprocess(genericSubprocess);
			
			mapBpmnIDtoGenericUID.put(bpmnSubprocess.getId(), genericSubprocess.getUid());
		}
	}
	
	//---- Getters and setters ----// 
	
	public BpmnProcess getBpmnProcess() {
		return bpmnProcess;
	}

	public void setBpmnProcess(BpmnProcess bpmnProcess) {
		this.bpmnProcess = bpmnProcess;
	}

	public Process getGenericProcess() {
		return genericProcess;
	}

	public void setGenericProcess(Process genericProcess) {
		this.genericProcess = genericProcess;
	}

	public Map<String, String> getMapBpmnIDtoGenericUID() {
		return mapBpmnIDtoGenericUID;
	}

}
