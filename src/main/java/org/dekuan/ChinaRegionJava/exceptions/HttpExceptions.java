package org.dekuan.ChinaRegionJava.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


/**
 * About @ResponseStatus
 * <a href="https://www.baeldung.com/exception-handling-for-rest-with-spring">...</a>
 */
public final class HttpExceptions
{
	public static String buildBindingResultError( BindingResult bindingResult )
	{
		String error = "";
		if ( null != bindingResult && bindingResult.hasErrors() )
		{
			//	...
			error = String.format( "field: unknown, message: invalid payload with %d errors", bindingResult.getAllErrors().size() );

			List<ObjectError> allErrors = bindingResult.getAllErrors();
			if ( allErrors.size() > 0 )
			{
				FieldError fieldError = ( FieldError ) allErrors.get( 0 );
				if ( null != fieldError )
				{
					error = String.format( "field: %s, message: %s", fieldError.getField(), fieldError.getDefaultMessage() );
				}
			}
		}

		return error;
	}

	//
	@ResponseStatus( value = HttpStatus.ALREADY_REPORTED )
	public static class AlreadyReported extends ResponseStatusException
	{
		public AlreadyReported( String reason )
		{
			super( HttpStatus.ALREADY_REPORTED, reason );
		}
		public AlreadyReported( BindingResult bindingResult )
		{
			super( HttpStatus.ALREADY_REPORTED, buildBindingResultError( bindingResult ) );
		}
	}

	@ResponseStatus( value = HttpStatus.BAD_REQUEST )
	public static class BadRequest extends ResponseStatusException
	{
		public BadRequest( String reason )
		{
			super( HttpStatus.BAD_REQUEST, reason );
		}
		public BadRequest( BindingResult bindingResult )
		{
			super( HttpStatus.BAD_REQUEST, buildBindingResultError( bindingResult ) );
		}
	}

	@ResponseStatus( value = HttpStatus.FORBIDDEN )
	public static class Forbidden extends ResponseStatusException
	{
		public Forbidden( String reason )
		{
			super( HttpStatus.FORBIDDEN, reason );
		}
	}

	@ResponseStatus( value = HttpStatus.UNAUTHORIZED )
	public static class Unauthorized extends ResponseStatusException
	{
		public Unauthorized( String reason )
		{
			super( HttpStatus.UNAUTHORIZED, reason );
		}
	}

	@ResponseStatus( value = HttpStatus.NOT_FOUND )
	public static class NotFound extends ResponseStatusException
	{
		public NotFound( String reason )
		{
			super( HttpStatus.NOT_FOUND, reason );
		}
	}


	@ResponseStatus( value = HttpStatus.INTERNAL_SERVER_ERROR )
	public static class InternalServerError extends ResponseStatusException
	{
		public InternalServerError( String reason )
		{
			super( HttpStatus.INTERNAL_SERVER_ERROR, reason );
		}
	}

	@ResponseStatus( value = HttpStatus.SERVICE_UNAVAILABLE )
	public static class ServiceUnavailable extends ResponseStatusException
	{
		public ServiceUnavailable( String reason )
		{
			super( HttpStatus.SERVICE_UNAVAILABLE, reason );
		}
	}
}