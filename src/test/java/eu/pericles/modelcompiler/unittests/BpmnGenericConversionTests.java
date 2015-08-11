package eu.pericles.modelcompiler.unittests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import eu.pericles.modelcompiler.common.BaseElement;
import eu.pericles.modelcompiler.testutils.PredefinedUUIDGenerator;
import eu.pericles.modelcompiler.testutils.Utils;

public class BpmnGenericConversionTests {

	Utils util = new Utils();
	BaseElement baseElement;
	
	@Test
	public void testGenericConversionHelloWorld() {
		baseElement = new BaseElement(new PredefinedUUIDGenerator());
		parseConvertAndWrite("src/test/resources/helloworld/");
	}

	private void parseConvertAndWrite(String path) {
		String inputFileName = path + "Input.bpmn2";
		String testFileName = path + "GenericConversionTest.bpmn2";
		String outputFileName = path + "GenericConversionOutput.bpmn2";

		assertTrue(util.checkParseGenericConvertAndWrite(inputFileName, testFileName, outputFileName));
	}

}
