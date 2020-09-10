package gov.virginia.dmas.serializer;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DeadlineDeserializer extends JsonDeserializer<Date> {
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

	@Override
	public Timestamp deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		Date cDate = null;
		Timestamp timestamp = null;
		try {
			cDate = dateFormat.parse(parser.getText());
			long epoch = cDate.getTime();
			timestamp = new Timestamp(epoch);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestamp;
	}

}
