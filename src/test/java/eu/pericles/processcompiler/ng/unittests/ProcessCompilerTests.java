package eu.pericles.processcompiler.ng.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.pericles.processcompiler.bpmn.BPMNParser;
import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.ng.ProcessCompiler;
import eu.pericles.processcompiler.ng.ProcessCompiler.ValidationResult;
import eu.pericles.processcompiler.ng.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ng.ecosystem.ProcessBase;
import eu.pericles.processcompiler.ng.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.ng.ermr.ERMRCommunications;

public class ProcessCompilerTests {

	static String service = "https://pericles1:PASSWORD@141.5.100.67/api";
	static String collection = "NoaCollection/Test/";
	static String repository = "NoaRepositoryTest";
	static String ecosystem = "src/test/resources/ingest_sba/Ecosystem_Compilation.ttl";
	static String triplesMediaType = "text/turtle";
	static String doMediaType = MediaType.APPLICATION_XML;
	static String doPath = "src/test/resources/ingest_sba/";
	
	private String[] digObjects = {"VirusCheck.bpmn", "ExtractMD.bpmn", "EncapsulateDOMD.bpmn"};

	@Before
	public void setRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI(service);
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
			ERMRClientAPI client = new ERMRClientAPI(service);
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
	
	// ----------------------------- TESTS ----------------------------------

	@Test
	public void validImplementation() {
		try {
			String uri = "<http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD>";
			ProcessBase process = new ERMRCommunications().getProcessEntity(repository, uri);
			String file = "src/test/resources/validate_implementation/ValidImplementation.bpmn2";
			BPMNProcess bpmnProcess = new BPMNParser().parse(file);
			ValidationResult validationResult = new ProcessCompiler(service).validateImplementation(process, bpmnProcess);
			assertTrue(validationResult.isValid());
		} catch (Exception e) {
			fail("validImplementation(): " + e.getMessage());
		}
	}
	
	@Test
	public void inputSlotMissing() {
		try {
			String uri = "<http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD>";
			ProcessBase process = new ERMRCommunications().getProcessEntity(repository, uri);
			String file = "src/test/resources/validate_implementation/InputSlotMissing.bpmn2";
			BPMNProcess bpmnProcess = new BPMNParser().parse(file);
			ValidationResult validationResult = new ProcessCompiler(service).validateImplementation(process, bpmnProcess);
			assertFalse(validationResult.isValid());
			assertEquals("NOT VALID IMPLEMENTATION: Slot <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF> in process <http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD> is wrong or missing in the BPMN file", validationResult.getMessage());
		} catch (Exception e) {
			fail("inputSlotMissing(): " + e.getMessage());
		}
	}
	
	@Test
	public void inputSlotDifferentType() {
		try {
			String uri = "<http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD>";
			ProcessBase process = new ERMRCommunications().getProcessEntity(repository, uri);
			String file = "src/test/resources/validate_implementation/InputSlotDifferentType.bpmn2";
			BPMNProcess bpmnProcess = new BPMNParser().parse(file);
			ValidationResult validationResult = new ProcessCompiler(service).validateImplementation(process, bpmnProcess);
			assertFalse(validationResult.isValid());
			assertEquals("NOT VALID IMPLEMENTATION: Slot <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF> in process <http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD> is wrong or missing in the BPMN file", validationResult.getMessage());
		} catch (Exception e) {
			fail("inputSlotDifferentType(): " + e.getMessage());
		}
	}

	@Test
	public void compileTest() {
		try {
			String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
			String expected = FileUtils.readFileToString(new File("src/test/resources/ingest_sba/IngestAWSW.bpmn"));
			AggregatedProcess aggregatedProcess = new ERMRCommunications().getAggregatedProcessEntity(repository, uri);
			ProcessCompiler compiler = new ProcessCompiler(service);
			String result = compiler.compile(repository, aggregatedProcess);
			assertEquals(expected, result);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception in compileTest()");
		}
	}
}
