package eu.pericles.processcompiler.web.resources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.core.ProcessCompiler.ValidationResult;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.exceptions.BaseException;
import eu.pericles.processcompiler.web.ApiException;
import eu.pericles.processcompiler.web.resources.BaseResource.BaseRequestBean;

@Path("/validate_aggregation")
@Consumes("application/json")
@Produces("application/json")
public class ValidateAggregationResource extends BaseResource {

	public static class ValidateAggregationRequest extends BaseRequestBean {
		public String id;
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
			if (Files.exists(getRelativeFile(request.id))) {
				ConfigBean config = parseConfig(getRelativeFile(request.id).toFile());
				request.process = config.aggregatedProcess;
			} else
				request.process = compiler.getAggregatedProcess(request.store, request.id);
			ValidateAggregationResult result = new ValidateAggregationResult();
			ValidationResult vResult = compiler.validateAggregation(request.store, request.process);
			result.valid = vResult.isValid();
			result.message = "OK\n" + vResult.getMessage();
			return result;
		} catch (ERMRClientException e) {
			throw new ApiException(500, e);
		} catch (IOException e) {
			throw new ApiException(400, e);
		} catch (BaseException e) {
			throw new ApiException(400, e);
		}
	}
}
