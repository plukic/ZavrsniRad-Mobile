package ba.ito.assistance.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;
import java.util.Locale;

public class DateTimeTypeAdapter implements JsonDeserializer<DateTime>, JsonSerializer<DateTime> {

    private DateTimeFormatter serverDateTimeFormatter;

    public DateTimeTypeAdapter(DateTimeFormatter serverDateTimeFormatter) {
        this.serverDateTimeFormatter= serverDateTimeFormatter;
    }

    @Override
    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        String asString = json.getAsString();
        DateTime utcDate = serverDateTimeFormatter.withZoneUTC().parseDateTime(asString);
        return new DateTime(utcDate,DateTimeZone.getDefault());
    }

    @Override
    public JsonElement serialize(DateTime json, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(serverDateTimeFormatter.print(json));
    }
}