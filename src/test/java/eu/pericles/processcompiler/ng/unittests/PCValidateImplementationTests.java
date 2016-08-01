package eu.pericles.processcompiler.ng.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import eu.pericles.processcompiler.bpmn.BPMNParser;
import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.ng.ecosystem.InputSlot;
import eu.pericles.processcompiler.ng.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ng.ecosystem.ProcessBase;
import eu.pericles.processcompiler.ng.ProcessCompiler;
import eu.pericles.processcompiler.ng.ProcessCompiler.ValidationResult;
import eu.pericles.processcompiler.ng.testutils.CreateEntities;

public class PCValidateImplementationTests {
	
	private String service = "https://pericles1:PASSWORD@141.5.100.67/api";

	// ----------------------------- TESTS ----------------------------------

	@Test
	public void validImplementation() {
		try {
			ProcessBase process = this.createProcess();
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
			ProcessBase process = this.createProcess();
			String file = "src/test/resources/validate_implementation/InputSlotMissing.bpmn2";
			BPMNProcess bpmnProcess = new BPMNParser().parse(file);
			ValidationResult validationResult = new ProcessCompiler(service).validateImplementation(process, bpmnProcess);
			assertFalse(validationResult.isValid());
			assertEquals(validationResult.getMessage(), "Slot: <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF> is missing or invalid in BPMN file");
		} catch (Exception e) {
			fail("inputSlotMissing(): " + e.getMessage());
		}
	}
	
	@Test
	public void inputSlotDifferentType() {
		try {
			ProcessBase process = this.createProcess();
			String file = "src/test/resources/validate_implementation/InputSlotDifferentType.bpmn2";
			BPMNProcess bpmnProcess = new BPMNParser().parse(file);
			ValidationResult validationResult = new ProcessCompiler(service).validateImplementation(process, bpmnProcess);
			assertFalse(validationResult.isValid());
			assertEquals(validationResult.getMessage(), "Slot: <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF> is missing or invalid in BPMN file");
		} catch (Exception e) {
			fail("inputSlotDifferentType(): " + e.getMessage());
		}
	}

	// ----------------------- HELP FUNCTIONS ----------------------------------

	private ProcessBase createProcess() {
		ProcessBase process = new ProcessBase();
		process.setId("<http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD>");
		process.setName("Encapsulate Digital Object and its Metadata");
		process.setDescription("Atomic process that encapsulate a digital object and its metadata together in a package of a specific format");
		process.setVersion("1");
		process.setImplementation(CreateEntities.createImplementation(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#impEncapsulateDOMD>", "1", "BPMN",
				"src/test/resources/core/processaggregationwithdata/EncapsulateDOMDProcess.bpmn2", "sha256",
				"ad0dec12cb47c4b3856929f803c6d40d76fffd3cf90681bf9a7bf65d63ca7f80"))));
		process.setInputSlots(new ArrayList<InputSlot>());
		process.getInputSlots().add(
				CreateEntities.createInputSlot(
						new ArrayList<String>(Arrays.asList("<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDDO>",
								"Digital Material InputSlot", "Input slot corresponding to the digital object",
								"<http://www.pericles-project.eu/ns/ecosystem#DigitalObject>")), false));
		process.getInputSlots().add(
				CreateEntities.createInputSlot(
						new ArrayList<String>(Arrays.asList("<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD>",
								"Metadata InputSlot", "Input slot corresponding to the metadata",
								"<http://www.pericles-project.eu/ns/ecosystem#Metadata>")), false));
		process.getInputSlots().add(
				CreateEntities.createInputSlot(
						new ArrayList<String>(Arrays.asList("<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF>",
								"Package Format InputSlot",
								"Input slot corresponding to the package format used to encapsulate the digital object and its metadata",
								"<http://www.pericles-project.eu/ns/ecosystem#PackageFormat>")), false));
		process.setOutputSlots(new ArrayList<OutputSlot>());
		process.getOutputSlots().add(
				CreateEntities.createOutputSlot(new ArrayList<String>(Arrays.asList(
						"<http://www.pericles-project.eu/ns/ecosystem#osEncapsulateDOMDP>", "Package OutputSlot",
						"Output slot corresponding to the package resulting of encapsulate a digital object and its metadata",
						"<http://www.pericles-project.eu/ns/ecosystem#Package>"))));
		return process;
	}
}
