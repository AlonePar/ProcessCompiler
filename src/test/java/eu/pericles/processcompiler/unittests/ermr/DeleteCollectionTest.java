package eu.pericles.processcompiler.unittests.ermr;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class DeleteCollectionTest {
	static String collection;

	@Test
	public void deleteCollection() throws ERMRClientException  {
		Response response = new ERMRClientAPI().deleteCollection(collection);
		System.out.println("Delete Collection: " + response.getStatus() + " " + response.getStatusInfo());
		assertEquals(204, response.getStatus());
	}
	
	public static void setVariables(String collection2) {
		collection = collection2;
	}

}
