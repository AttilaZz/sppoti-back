package com.fr.commons.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by djenanewail on 5/6/17.
 */
public class JsonDateDeserializer extends JsonDeserializer<Date>
{
	/** Date format. */
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
			throws IOException, JsonProcessingException
	{
		final String date = jsonParser.getText();
		try {
			return dateFormat.parse(date);
		} catch (final ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
