package gov.virginia.dmas.serializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DeadlineSerializer extends JsonSerializer<Date> {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

	@Override
	public void serialize(Date date, JsonGenerator generator, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		try {
			String formattedDate = dateFormat.format(date);
			generator.writeString(formattedDate);		
		} catch(Exception e) {
			generator.writeString(date.toString());
		}
	}
}
