package eu.pericles.processcompiler.communications.ermr;

import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.AtomicProcess;
import eu.pericles.processcompiler.ecosystem.Implementation;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ecosystem.Process;
import eu.pericles.processcompiler.ecosystem.Sequence;

public class ERMRCommunications {
	
	private ERMRClientAPI client;
	
	public ERMRCommunications() throws KeyManagementException, NoSuchAlgorithmException {
		client = new ERMRClientAPI();
	}

	public ERMRClientAPI getClient() {
		return client;
	}
	
	public AggregatedProcess getAggregatedProcessEntity(String repository, String uri) throws Exception {
		AggregatedProcess aggregatedProcess = new AggregatedProcess(getProcessEntity(repository, uri));
		aggregatedProcess.setSequence(getProcessSequence(repository, uri));
		
		return aggregatedProcess;
	}

	public Process getProcessEntity(String repository, String uri) throws Exception {
		Process process = getProcessAttributes(repository, uri);
		process.setInputs(getProcessInputSlots(repository, uri));
		process.setOutputs(getProcessOutputSlots(repository, uri));
		process.setImplementation(getProcessImplementation(repository, uri));
		
		return process;
	}
	
	public String getProcessType(String repository, String uri) throws UnsupportedEncodingException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetProcessType(uri));
		return JSONParser.parseGetTypeResponse(response);
	}
	
	public Process getProcessAttributes(String repository, String uri) throws UnsupportedEncodingException {
		
		Response response = client.query(repository, SPARQLQuery.createQueryGetProcessAttributes(uri));
		return JSONParser.parseGetProcessAttributesResponse(response, uri);
	}

	public List<InputSlot> getProcessInputSlots(String repository, String uri) throws UnsupportedEncodingException {
		List<InputSlot> inputSlots = new ArrayList<InputSlot>();
		for (String inputSlotURI : getInputSlotURIList(repository, uri))
			inputSlots.add(getInputSlotEntity(repository, inputSlotURI));
		return inputSlots;
	}

	public List<String> getInputSlotURIList(String repository, String uri) throws UnsupportedEncodingException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetInputSlotURIList(uri));
		return JSONParser.parseGetURIListResponse(response, uri);
	}

	public InputSlot getInputSlotEntity(String repository, String uri) throws UnsupportedEncodingException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetInputSlotEntity(uri));
		return JSONParser.parseGetInputSlotEntityResponse(response, uri);
	}
	
	public List<OutputSlot> getProcessOutputSlots(String repository, String uri) throws UnsupportedEncodingException {
		List<OutputSlot> outputSlots = new ArrayList<OutputSlot>();
		for (String outputSlotURI : getOutputSlotURIList(repository, uri))
			outputSlots.add(getOutputSlotEntity(repository, outputSlotURI));
		return outputSlots;
	}

	public List<String> getOutputSlotURIList(String repository, String uri) throws UnsupportedEncodingException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetOutputSlotURIList(uri));
		return JSONParser.parseGetURIListResponse(response, uri);
	}

	public OutputSlot getOutputSlotEntity(String repository, String uri) throws UnsupportedEncodingException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetOutputSlotEntity(uri));
		return JSONParser.parseGetOutputSlotEntityResponse(response, uri);
	}

	public Implementation getProcessImplementation(String repository, String uri) throws Exception {
		return getImplementationEntity(repository, getImplementationURI(repository, uri));
	}
	
	public Implementation getImplementationEntity(String repository, String uri) throws Exception {
		Response response = client.query(repository, SPARQLQuery.createQueryGetImplementationEntity(uri));
		return JSONParser.parseGetImplementationEntityResponse(response, uri);
	}

	public String getImplementationURI(String repository, String uri) throws UnsupportedEncodingException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetImplementationURI(uri));
		return JSONParser.parseGetURIResponse(response);
	}

	public Sequence getProcessSequence(String repository, String uri) throws Exception {
		return getSequenceEntity(repository, getSequenceURI(repository, uri));
	}
	
	public Sequence getSequenceEntity(String repository, String uri) throws UnsupportedEncodingException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetSequenceEntity(uri));
		return JSONParser.parseGetSequenceEntityResponse(response, uri);
	}
	
	public String getSequenceURI(String repository, String uri) throws UnsupportedEncodingException {
		Response response = client.query(repository, SPARQLQuery.createQueryGetSequenceURI(uri));
		return JSONParser.parseGetURIResponse(response);
	}

}
