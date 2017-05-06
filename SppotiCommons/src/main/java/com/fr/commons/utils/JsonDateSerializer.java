package com.fr.commons.utils;

/**
 * Created by djenanewail on 5/6/17.
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Used to serialize Java.util.Date, which is not a common JSON
 * type, so we have to create a custom serialize method;.
 *
 * @author Loiane Groner http://loianegroner.com (English) http://loiane.com (Portuguese)
 */
@Component
public class JsonDateSerializer extends JsonSerializer<Date>
{
	/** Date format. */
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final Date date, final JsonGenerator gen, final SerializerProvider provider)
			throws IOException, JsonProcessingException
	{
		final String formattedDate = dateFormat.format(date);
		gen.writeString(formattedDate);
	}
}