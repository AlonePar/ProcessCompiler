package eu.pericles.modelcompiler.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import eu.pericles.modelcompiler.bpmn.BpmnProcess;
import eu.pericles.modelcompiler.bpmn.EndEvent;
import eu.pericles.modelcompiler.bpmn.ScriptTask;
import eu.pericles.modelcompiler.bpmn.SequenceFlow;
import eu.pericles.modelcompiler.bpmn.StartEvent;
import eu.pericles.modelcompiler.bpmn.Subprocess;
import eu.pericles.modelcompiler.generic.Activity;
import eu.pericles.modelcompiler.generic.Event;
import eu.pericles.modelcompiler.generic.Flow;
import eu.pericles.modelcompiler.generic.Gateway;
import eu.pericles.modelcompiler.generic.Process;

public class BpmnGenericConversor {

	private BpmnProcess bpmnProcess;
	private Process genericProcess;
	private Map<String,String> mapBpmnIDtoGenericUID;

	public BpmnGenericConversor() {
		bpmnProcess = new BpmnProcess();
		genericProcess = new Process();
		mapBpmnIDtoGenericUID = new HashMap<String,String>();
	}

	public void convertFromBpmnToGeneric(BpmnProcess bpmnProcess) {
		System.out.println("convertFromBpmnToGeneric");
		setBpmnProcess(bpmnProcess);

		convertProcessAttributesFromBpmnToGeneric(getBpmnProcess(), getGenericProcess());
		convertProcessNodesFromBpmnToGeneric(getBpmnProcess(), getGenericProcess());
	}

	private void convertProcessAttributesFromBpmnToGeneric(BpmnProcess bpmnProcess, Process genericProcess) {
		System.out.println("convertProcessAttributesFromBpmnToGeneric");
		genericProcess.setName(bpmnProcess.getName());
		genericProcess.setSource(bpmnProcess.getId());	
	}


	private void convertProcessNodesFromBpmnToGeneric(BpmnProcess bpmnProcess, Process genericProcess) {
		System.out.println("convertProcessNodesFromBpmnToGeneric");
		convertActivitiesFromBpmnToGeneric(bpmnProcess, genericProcess);
		convertEventsFromBpmnToGeneric(bpmnProcess, genericProcess);
		convertGatewaysFromBpmnToGeneric(bpmnProcess, genericProcess);
		convertSubprocessesFromBpmnToGeneric(bpmnProcess, genericProcess);
		convertFlowsFromBpmnToGeneric(bpmnProcess, genericProcess);
		System.out.println("End convertProcessNodesFromBpmnToGeneric");
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

			event.setType(Event.Type.START);

			genericProcess.addEvent(event);
			mapBpmnIDtoGenericUID.put(startEvent.getId(), event.getUid());
		}
		for (EndEvent endEvent : bpmnProcess.getEndEvents()) {			
			Event event = new Event();

			event.setType(Event.Type.END);

			genericProcess.addEvent(event);
			mapBpmnIDtoGenericUID.put(endEvent.getId(), event.getUid());
		}

	}

	private void convertGatewaysFromBpmnToGeneric(BpmnProcess bpmnProcess, Process genericProcess) {
		// TODO Auto-generated method stub


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
		System.out.println("convertSubprocessFromBpmnToGeneric");
		if (bpmnProcess.getSubprocesses() == null)
		{
			System.out.println("bpmnProcess.getSubprocesses() == null");
		} else {
		for (Subprocess bpmnSubprocess : bpmnProcess.getSubprocesses()) {
			System.out.println("Bpmn Subprocess: " + bpmnSubprocess.getId());
			Process genericSubprocess = new Process();
			
			convertProcessAttributesFromBpmnToGeneric(bpmnSubprocess, genericSubprocess);
			convertProcessNodesFromBpmnToGeneric(bpmnSubprocess, genericSubprocess);
			
			genericProcess.addSubprocess(genericSubprocess);
			mapBpmnIDtoGenericUID.put(bpmnSubprocess.getId(), genericSubprocess.getUid());

		}
		}
		System.out.println("End convertSubprocessFromBpmnToGeneric");
	}

	public BpmnProcess getBpmnProcess() {
		return bpmnProcess;
	}

	public void setBpmnProcess(BpmnProcess bpmnProcess) {
		this.bpmnProcess = bpmnProcess;
		//getBpmnProcess().copyBpmnProcess(this.bpmnProcess, bpmnProcess);
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
