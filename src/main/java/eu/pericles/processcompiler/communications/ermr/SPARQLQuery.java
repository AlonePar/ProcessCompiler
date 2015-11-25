package eu.pericles.processcompiler.communications.ermr;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SPARQLQuery {

	public static String encode(String query) throws UnsupportedEncodingException {
		return URLEncoder.encode(query, "UTF-8");
	}
	
	public static String createQueryGetProcessAttributes(String process) throws UnsupportedEncodingException {
		String query = "PREFIX ecosystem:<http://www.pericles-project.eu/ns/ecosystem#> " 
				+ " SELECT DISTINCT ?name ?description ?version WHERE {"
				+ process + " ecosystem:name ?name . "
				+ process + " ecosystem:description ?description . "
				+ process + " ecosystem:version ?version . "
				+ " }";
		return encode(query);
	}

	public static String createQueryGetImplementationEntity(String implementation) throws UnsupportedEncodingException {
		String query = "PREFIX ecosystem:<http://www.pericles-project.eu/ns/ecosystem#> " 
			   + " SELECT DISTINCT ?version ?type ?location ?checksum ?algorithm WHERE { "
			   + implementation + " ecosystem:version ?version . "
			   + implementation + " ecosystem:type ?type . "
			   + implementation + " ecosystem:location ?location . "
			   + implementation + " ecosystem:hasFixity ?fixity . "
			   + "?fixity ecosystem:checksum ?checksum . "
			   + "?fixity ecosystem:algorithm ?algorithm . "
			   + " }";
		return encode(query);
	}

	public static String createQueryGetInputSlotEntity(String inputSlot) throws UnsupportedEncodingException {
		String query = "PREFIX ecosystem:<http://www.pericles-project.eu/ns/ecosystem#> " 
				+ "SELECT DISTINCT ?name ?description ?type ?optional WHERE {"
				+ inputSlot + " ecosystem:name ?name . "
				+ inputSlot + " ecosystem:description ?description . "
				+ inputSlot + " ecosystem:type ?type . "
				+ inputSlot + " ecosystem:isOptional ?optional . "
				+ " }";
		return encode(query);
	}

	public static String createQueryGetInputSlotURIList(String process) throws UnsupportedEncodingException {
		String query = "PREFIX ecosystem:<http://www.pericles-project.eu/ns/ecosystem#> "
				+ " SELECT DISTINCT ?inputs WHERE { "
				+ process + " ecosystem:hasInputSlot ?inputs  . "
				+ " }";
		return encode(query);
	}

	public static String createQueryGetOutputSlotURIList(String process) throws UnsupportedEncodingException {
		String query = "PREFIX ecosystem:<http://www.pericles-project.eu/ns/ecosystem#> "
				+ " SELECT DISTINCT ?outputs WHERE { "
				+ process + " ecosystem:hasOutputSlot ?outputs  . "
				+ " }";
		return encode(query);
	}
	public static String createQueryGetOutputSlotEntity(String outputSlot) throws UnsupportedEncodingException {
		String query = "PREFIX ecosystem:<http://www.pericles-project.eu/ns/ecosystem#> " 
				+ "SELECT DISTINCT ?name ?description ?type WHERE {"
				+ outputSlot + " ecosystem:name ?name . "
				+ outputSlot + " ecosystem:description ?description . "
				+ outputSlot + " ecosystem:type ?type . "
				+ " }";
		return encode(query);
	}

	public static String createQueryGetImplementationURI(String process) throws UnsupportedEncodingException {
		String query = createQueryGetURI(process, "ecosystem:hasImplementation");
		return encode(query);
	}
	
	public static String createQueryGetURI(String subject, String predicate) throws UnsupportedEncodingException {
		String query = "PREFIX ecosystem:<http://www.pericles-project.eu/ns/ecosystem#> " 
				+ "SELECT DISTINCT ?uri WHERE {"
				+ subject + " " + predicate + " ?uri. "
				+ " }";
		return encode(query);
	}

}

/**
 * QUERY TEMPLATE
 *  String query = "PREFIX ecosystem:<http://www.pericles-project.eu/ns/ecosystem#> " 
				+ "SELECT DISTINCT WHERE {"
				+ " }";
	return encode(query);
*/
