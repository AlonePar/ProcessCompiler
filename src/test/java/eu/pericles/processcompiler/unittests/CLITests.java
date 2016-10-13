package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.pericles.processcompiler.cli.CommandlineInterface;
import eu.pericles.processcompiler.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class CLITests {
	
	private PrintStream defaultOutputStream;
	private ByteArrayOutputStream outputStream;
	private PrintStream defaultErrorStream;
	private ByteArrayOutputStream errorStream;
	
	private String baseArgs = "-s https://pericles1:PASSWORD@141.5.100.67/api -r NoaRepositoryTest ";
	
	static String collection = "NoaCollection/Test/";
	static String repository = "NoaRepositoryTest";
	static String ecosystem = "src/test/resources/ingest_sba/Ecosystem.ttl";
	static String triplesMediaType = "text/turtle";
	static String doMediaType = MediaType.APPLICATION_XML;
	static String doPath = "src/test/resources/ingest_sba/";
	static String testPath = "src/test/resources/cli/";
	
	private String[] digObjects = {"VirusCheck.bpmn", "ExtractMD.bpmn", "EncapsulateDOMD.bpmn"};

	@Before
	public void setRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI();
			Response response = client.addTriples(repository, ecosystem, triplesMediaType);
			assertEquals(201, response.getStatus());
			for (int i=0; i<digObjects.length; i++) {
				response = new ERMRClientAPI().createDigitalObject(collection + digObjects[i], doPath + digObjects[i], doMediaType);
				assertEquals(201, response.getStatus());
			}
		} catch (ERMRClientException e) {
			fail("setRepository(): " + e.getMessage());
		}
	}

	@After
	public void deleteRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI();
			Response response = client.deleteTriples(repository);
			assertEquals(204, response.getStatus());
			for (int i=0; i<digObjects.length; i++) {
			response = new ERMRClientAPI().deleteDigitalObject(collection + digObjects[i]);
			assertEquals(204, response.getStatus());
			}
		} catch (ERMRClientException e) {
			fail("deleteRepository(): " + e.getMessage());
		}
	}
	
	@Before
	public void changeOutputStream() {
		defaultOutputStream = System.out;
		outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		defaultErrorStream = System.err;
		errorStream = new ByteArrayOutputStream();
		System.setErr(new PrintStream(errorStream));
	}
	
	@After
	public void restoreDefaultOutputStream() {
		System.setOut(defaultOutputStream);
		System.setErr(defaultErrorStream);
	}
	
	@Test
	public void invalidArgumentsTest() {
		String command = baseArgs + "validate_implementation";
		String[] args = command.split(" ");
		assertCall(args, 2);
		
		String result = errorStream.toString();
		defaultErrorStream.println(result);
		assertEquals("usage: modelcompiler -s URL -r REPO validate_implementation [-h] PROC IMPL\nmodelcompiler: error: too few arguments\n",result);
	}

	@Test
	public void validateImplementationByFileTest() {
		String command = baseArgs + "validate_implementation " + testPath + "inputValidImplementation.json " + doPath + "EncapsulateDOMD.bpmn";
		String[] args = command.split(" ");
		assertCall(args, 0);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertEquals("200 OK\nVALID IMPLEMENTATION\n",result);
	}
	
	@Test
	public void validateImplementationByIDTest() {
		String command = baseArgs + "validate_implementation <http://www.pericles-project.eu/ns/ingest-scenario#atpEncapsulateDOMD> <http://www.pericles-project.eu/ns/ingest-scenario#impEncapsulateDOMD>";
		String[] args = command.split(" ");
		assertCall(args, 0);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertEquals("200 OK\nVALID IMPLEMENTATION\n",result);
	}
	
	@Test
	public void invalidImplementationTest() {
		String command = baseArgs + "validate_implementation " + testPath + "inputInvalidImplementation.json " + doPath + "EncapsulateDOMD.bpmn";
		String[] args = command.split(" ");
		assertCall(args, 1);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertEquals("200 OK\nINVALID IMPLEMENTATION: Slot <http://www.pericles-project.eu/ns/ingest-scenario#isEncapsulateDOMDPF> is wrong or missing in the BPMN file\n",result);
	}
	
	@Test
	public void validateAggregationByFileTest() {
		String command = baseArgs + "validate_aggregation " + testPath + "inputValidAggregation.json";
		String[] args = command.split(" ");
		assertCall(args, 0);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertEquals("200 OK\nVALID AGGREGATION\n",result);
	}
	
	@Test
	public void validateAggregationByIDTest() {
		String command = baseArgs + "validate_aggregation <http://www.pericles-project.eu/ns/ingest-scenario#agpIngestAWSW>";
		String[] args = command.split(" ");
		assertCall(args, 0);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertEquals("200 OK\nVALID AGGREGATION\n",result);
	} 
	
	@Test
	public void invalidAggregationTest() {
		String command = baseArgs + "validate_aggregation " + testPath + "inputInvalidAggregation.json";
		String[] args = command.split(" ");
		assertCall(args, 1);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertEquals("200 OK\nINVALID AGGREGATION: Invalid data type in connection (<http://www.pericles-project.eu/ns/ingest-scenario#isIngestAWSWPF>,<http://www.pericles-project.eu/ns/ingest-scenario#isExtractMDDO>)\n",result);
	}

	@Test
	public void compileAggregatedProcessByFileTest() {
		String command = baseArgs + "compile -o " + testPath + "output.bpmn " + testPath + "inputValidAggregation.json";
		String[] args = command.split(" ");
		assertCall(args, 0);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("200 OK"));
	}
	
	@Test
	public void compileAggregatedProcessByIDTest() {
		String command = baseArgs + "compile -o " + testPath + "output.bpmn <http://www.pericles-project.eu/ns/ingest-scenario#agpIngestAWSW>";
		String[] args = command.split(" ");
		assertCall(args, 0);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("200 OK"));
	}

	@Test
	public void invalidCompilationTest() {
		String command = baseArgs + "compile -o " + testPath + "output.bpmn " + testPath + "inputInvalidAggregation.json";
		String[] args = command.split(" ");
		assertCall(args, 1);
		
		String result = outputStream.toString();
		defaultOutputStream.println(result);
		assertTrue(result.contains("400 Bad Request"));
	}
	
	private void assertCall(String[] args, int code) {
		int status = new CommandlineInterface().call(args);
		if(status != code)
			System.out.println(outputStream.toString());
		assertEquals(code,  status);
	}

}