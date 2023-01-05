package org.dekuan.ChinaRegionJava.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.dekuan.ChinaRegionJava.models.HttpExceptionHeader;
import org.dekuan.ChinaRegionJava.utils.DeWebUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 	Guide to Spring Boot REST API Error Handling
 * 	<a href="https://www.toptal.com/java/spring-boot-rest-api-error-handling">https://www.toptal.com/java/spring-boot-rest-api-error-handling</a>
 */

@Order( Ordered.HIGHEST_PRECEDENCE )
@ControllerAdvice
@Slf4j
public class GlobalException extends ResponseEntityExceptionHandler
{
	@ExceptionHandler( io.grpc.StatusRuntimeException.class )
	public void handleRRpcStatusRuntimeException
		(
			io.grpc.StatusRuntimeException ex,
			HttpServletResponse response
		) throws IOException
	{
		response.sendError(
			HttpStatus.SERVICE_UNAVAILABLE.value(),
			"rpc.service.unavailable"
		);
	}


	@ExceptionHandler( {
		HttpExceptions.AlreadyReported.class,
		HttpExceptions.BadRequest.class,
		HttpExceptions.Forbidden.class,
		HttpExceptions.InternalServerError.class,
		HttpExceptions.NotFound.class,
		HttpExceptions.ServiceUnavailable.class,
		HttpExceptions.Unauthorized.class
	} )
	private ResponseEntity<HttpExceptionHeader> customHandleHttpExceptions( ResponseStatusException ex, WebRequest request )
	{
		HttpExceptionHeader httpExceptionHeader = HttpExceptionHeader.httpExceptionHeaderBuilder()
			.status( ex.getStatus().value() )
			.error( ex.getStatus().getReasonPhrase() )
			.message( ex.getReason() )
			.traceId( "TraceContext.traceId()" )
			.path( ((ServletWebRequest)request).getRequest().getRequestURI() )
			.client( DeWebUtils.getClientIpWithProxy() )
			.build();

		return new ResponseEntity<>( httpExceptionHeader, ex.getStatus() );
	}

	public Map<String, Object> getBody(HttpStatus status, Exception ex, String message )
	{
		log.error( message, ex );

		Map<String, Object> body = new LinkedHashMap<>();
		body.put( "message", message );
		body.put( "timestamp", new Date() );
		body.put( "status", status.value() );
		body.put( "error", status.getReasonPhrase() );
		body.put( "exception", ex.toString() );

		Throwable cause = ex.getCause();
		if ( cause != null )
		{
			body.put( "exceptionCause",
				( null != ex.getCause() ) ? ex.getCause().toString() : "null.cause" );
		}

		return body;
	}
}
