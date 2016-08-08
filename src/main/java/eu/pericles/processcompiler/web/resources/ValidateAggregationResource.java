package eu.pericles.processcompiler.web.resources;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.core.ProcessCompiler.ValidationResult;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.exceptions.PCException;
import eu.pericles.processcompiler.web.ApiException;

@Path("/validate_aggregation")
public class ValidateAggregationResource extends BaseResource {

	public static class ValidateAggregationRequest {
		public String id;
		public String ermr;
		public String store;
		public AggregatedProcess process;
	}

	public static class ValidateAggregationResult {
		public boolean valid;
		public String message;
	}

	@PUT
	public ValidateAggregationResult validateAggregation(ValidateAggregationRequest request) throws ApiException {
		assertEntity(request);
		assertTrue(request.ermr != null, "The 'ermr' field is required.");
		assertTrue(request.store != null, "The 'store' field is required.");
		assertTrue(request.id != null, "The 'id' field is required.");

		try {
			ProcessCompiler compiler = new ProcessCompiler(request.ermr);
			if (new File(request.id).exists()) {
				ConfigBean config = parseConfig(new File(request.id));
				request.process = config.aggregatedProcess;
			} else
				request.process = compiler.getAggregatedProcess(request.store, request.id);
			ValidateAggregationResult result = new ValidateAggregationResult();
			ValidationResult vResult = compiler.validateAggregation(request.store, request.process);
			result.valid = vResult.isValid();
			if (result.valid)
				result.message = "OK\nValid aggregation";
			else 
				result.message = "OK\nInvalid aggregation\n" + vResult.getMessage();
			return result;
		} catch (ERMRClientException e) {
			throw new ApiException(500, e);
		} catch (IOException e) {
			throw new ApiException(400, e);
		} catch (PCException e) {
			throw new ApiException(400, e);
		}
	}
}
