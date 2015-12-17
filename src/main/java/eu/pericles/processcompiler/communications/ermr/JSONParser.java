package eu.pericles.processcompiler.communications.ermr;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;

import eu.pericles.processcompiler.ecosystem.Fixity;
import eu.pericles.processcompiler.ecosystem.Implementation;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ecosystem.Process;
import eu.pericles.processcompiler.ecosystem.Sequence;
import eu.pericles.processcompiler.ecosystem.DataFlowNode;
import eu.pericles.processcompiler.ecosystem.DataConnection;

public class JSONParser {
	
	//------------------------------- PARSE ENTITIES -------------------------------------//
	
	public static Process parseGetProcessAttributesResponse(Response response, String uri) {
		JsonArray values = getValues(response).getJsonArray(0);
		
		Process process = new Process();
		process.setId(uri);
		process.setName(values.getString(0).replace("\"", ""));
		process.setDescription(values.getString(1).replace("\"", ""));
		process.setVersion(values.getString(2).replace("\"", ""));
		
		return process;
	}

	public static Implementation parseGetImplementationEntityResponse(Response response, String uri) throws Exception {
		JsonArray values = getValues(response).getJsonArray(0);
		
		Implementation implementation = new Implementation();
		implementation.setId(uri);
		implementation.setVersion(values.getString(0).replace("\"", ""));
		implementation.setType(values.getString(1).replace("\"", ""));
		implementation.setLocation(values.getString(2).replace("\"", ""));
		implementation.setFixity(new Fixity());
		implementation.getFixity().setChecksum(values.getString(3).replace("\"", ""));
		implementation.getFixity().setAlgorithm(values.getString(4).replace("\"", ""));
		
		return implementation;
	}
	
	public static InputSlot parseGetInputSlotEntityResponse(Response response, String uri) {
		JsonArray values = getValues(response).getJsonArray(0);
		
		InputSlot inputSlot = new InputSlot();
		inputSlot.setId(uri);
		inputSlot.setName(values.getString(0).replace("\"", ""));
		inputSlot.setDescription(values.getString(1).replace("\"", ""));
		inputSlot.setType(values.getString(2).replace("\"", ""));
		if (values.getString(3).replace("\"", "").equals("true"))
			inputSlot.setOptional(true);
		else
			inputSlot.setOptional(false);
		
		return inputSlot;
	}
	
	public static OutputSlot parseGetOutputSlotEntityResponse(Response response, String uri) {
		JsonArray values = getValues(response).getJsonArray(0);
		
		OutputSlot outputSlot = new OutputSlot();
		outputSlot.setId(uri);
		outputSlot.setName(values.getString(0).replace("\"", ""));
		outputSlot.setDescription(values.getString(1).replace("\"", ""));
		outputSlot.setType(values.getString(2).replace("\"", ""));
		
		return outputSlot;
	}
	
	public static Sequence parseGetSequenceEntityResponse(Response response, String uri) {
		JsonArray values = getValues(response).getJsonArray(0);
		
		Sequence sequence = new Sequence();
		sequence.setProcessFlow(parseProcessFlow(values.getString(0).replace("\"", "")));
		sequence.setDataFlow(parseDataFlow(values.getString(1).replace("\"", "")));
		
		return sequence;
	}
	
	public static ArrayList<String> parseProcessFlow(String processFlowString) {
		return new ArrayList<String>(Arrays.asList(processFlowString.split("\\s\\s*")));
	}
	
	public static ArrayList<DataConnection> parseDataFlow(String dataFlowString) {
		ArrayList<DataConnection> dataFlow = new ArrayList<DataConnection>();		
		Pattern pattern = Pattern.compile("\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(dataFlowString);
		while (matcher.find()) {
			dataFlow.add(parseDataConnection(matcher.group()));
		}
		return dataFlow;
	}
	
	public static DataConnection parseDataConnection(String slotConnectionString) {
		DataFlowNode inputSlot, resourceSlot;
		Pattern pattern = Pattern.compile("\\[(.*?)\\]");
		Matcher matcher = pattern.matcher(slotConnectionString);
		matcher.find();
		inputSlot = parseDataFlowNode(matcher.group());
		matcher.find();
		resourceSlot = parseDataFlowNode(matcher.group());
		return new DataConnection(inputSlot, resourceSlot);
	}
	
	public static DataFlowNode parseDataFlowNode(String sequenceSlotString) {
		String[] values = sequenceSlotString.substring(1, sequenceSlotString.length()-1).split("\\s\\s*");
		return new DataFlowNode(Integer.parseInt(values[0]),values[1]);
	}

	public static String parseGetProcessTypeResponse(Response response) {
		String type = getValues(response).getJsonArray(0).getString(0);
		
		Pattern pattern = Pattern.compile("#(.*?)>");
		Matcher matcher = pattern.matcher(type);
		matcher.find();
		type = matcher.group().substring(1, matcher.group().length()-1);
		
		return type;
	}
	
	//------------------------------- PARSE URIs -------------------------------------//

	public static List<String> parseGetURIListResponse(Response response, String uri) {
		JsonArray values = getValues(response);
		
		List<String> uriList = new ArrayList<String>();
		for (int array = 0; array < values.size(); array++)
			uriList.add(values.getJsonArray(array).getString(0).replace("\"", ""));
		
		return uriList;
	}

	public static String parseGetURIResponse(Response response) {
		return getValues(response).getJsonArray(0).getString(0).replace("\"", "");
	}
	
	//------------------------------- PRIVATE FUNCTIONS -------------------------------------//

	private static JsonArray getValues(Response response) {
		return getJSONObject(response).getJsonArray("values");
	}

	private static JsonObject getJSONObject(Response response) {
		return Json.createReader(response.readEntity(InputStream.class)).readObject();
	}

}
