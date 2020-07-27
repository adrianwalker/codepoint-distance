package org.adrianwalker.codepointdistance.rest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.adrianwalker.codepointdistance.model.Postcode;

public final class PostcodeSerializer extends StdSerializer<Postcode> {

  public PostcodeSerializer() {

    this(null);
  }

  public PostcodeSerializer(final Class<Postcode> t) {

    super(t);
  }

  @Override
  public void serialize(
    final Postcode postcode,
    final JsonGenerator generator,
    final SerializerProvider provider)
    throws IOException, JsonProcessingException {

    generator.writeString(postcode.getValue());
  }
}
