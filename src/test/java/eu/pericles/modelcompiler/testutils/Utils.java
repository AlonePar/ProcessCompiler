package eu.pericles.modelcompiler.testutils;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.xml.sax.SAXException;

import eu.pericles.modelcompiler.common.BpmnGenericConversor;
import eu.pericles.modelcompiler.common.BpmnJbpmConversor;
import eu.pericles.modelcompiler.common.GenericBpmnConversor;
import eu.pericles.modelcompiler.common.JbpmBpmnConversor;
import eu.pericles.modelcompiler.common.RandomUidGenerator;
import eu.pericles.modelcompiler.common.UidGeneration;
import eu.pericles.modelcompiler.jbpm.JbpmFile;
import eu.pericles.modelcompiler.jbpm.JbpmFileParser;
import eu.pericles.modelcompiler.jbpm.JbpmFileWriter;

public class Utils {

	public static boolean checkParseAndWrite(String inputFileName, String testFileName, String outputFileName) {
		write(parse(inputFileName), outputFileName);

		return fileContentEquals(outputFileName, testFileName);
	}

	public static boolean checkParseBpmnConvertAndWrite(String inputFileName, String testFileName, String outputFileName) {
		write(convertJbpmBpmnJbpm(parse(inputFileName)), outputFileName);

		return fileContentEquals(outputFileName, testFileName);
	}

	public static boolean checkParseGenericConvertAndWrite(String inputFileName, String testFileName, String outputFileName) {
		write(convertJbpmBpmnGenericBpmnJbpm(parse(inputFileName)), outputFileName);

		return fileContentEquals(outputFileName, testFileName);
	}

	public static void write(JbpmFile jbpmFile, String outputFileName) {
		JbpmFileWriter jbpmFileWriter = new JbpmFileWriter();
		jbpmFileWriter.write(jbpmFile, outputFileName);
	}

	public static JbpmFile parse(String inputFileName) {
		JbpmFileParser jbpmFileParser = new JbpmFileParser();
		jbpmFileParser.parse(inputFileName);

		return jbpmFileParser.getJbpmFile();
	}

	public static JbpmFile convertJbpmBpmnJbpm(JbpmFile jbpmFile) {
		JbpmBpmnConversor jbpmBpmnConversor = new JbpmBpmnConversor();
		jbpmBpmnConversor.convert(jbpmFile);
		BpmnJbpmConversor bpmnJbpmConversor = new BpmnJbpmConversor(new RandomUidGenerator());
		bpmnJbpmConversor.convert(jbpmBpmnConversor.getBpmnProcess());

		return bpmnJbpmConversor.getJbpmFile();
	}

	public static JbpmFile convertJbpmBpmnGenericBpmnJbpm(JbpmFile jbpmFile) {
		JbpmBpmnConversor jbpmBpmnConversor = new JbpmBpmnConversor();
		BpmnGenericConversor bpmnGenericConversor = new BpmnGenericConversor(new RandomUidGenerator());
		GenericBpmnConversor genericBpmnConversor = new GenericBpmnConversor(new RandomUidGenerator());
		BpmnJbpmConversor bpmnJbpmConversor = new BpmnJbpmConversor(new RandomUidGenerator());
		jbpmBpmnConversor.convert(jbpmFile); // from jBPM to BPMN
		bpmnGenericConversor.convert(jbpmBpmnConversor.getBpmnProcess()); // from
																			// BPMN
																			// to
																			// generic
		// System.out.println("Generic Process: " +
		// bpmnGenericConversor.getGenericProcess().getUid() + " " +
		// bpmnGenericConversor.getGenericProcess().getActivities().get(0).getScript());
		genericBpmnConversor.convert(bpmnGenericConversor.getGenericProcess()); // from
																				// generic
																				// to
																				// BPMN
		bpmnJbpmConversor.convert(genericBpmnConversor.getBpmnProcess()); // from
																			// BPMN
																			// to
																			// jBPM

		return bpmnJbpmConversor.getJbpmFile(); // return jBPM
	}

	public static boolean fileContentEquals(String outputFileName, String testFileName) {
		try {
			String a = FileUtils.readFileToString(new File(outputFileName));
			String b = FileUtils.readFileToString(new File(testFileName));
			XMLCompare.assertEquals(a, b);
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}

		return true;
	}

}
