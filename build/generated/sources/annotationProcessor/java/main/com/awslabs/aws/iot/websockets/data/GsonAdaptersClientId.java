package com.awslabs.aws.iot.websockets.data;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.google.gson.stream.*;
import java.io.IOException;
import javax.annotation.processing.Generated;

/**
 * A {@code TypeAdapterFactory} that handles all of the immutable types generated under {@code ClientId}.
 * @see ImmutableClientId
 */
@SuppressWarnings({"all", "MethodCanBeStatic"})
@Generated("org.immutables.processor.ProxyProcessor")
@org.immutables.value.Generated(from = "com.awslabs.aws.iot.websockets.data", generator = "Gsons")
public final class GsonAdaptersClientId implements TypeAdapterFactory {
  @SuppressWarnings({"unchecked", "raw"}) // safe unchecked, types are verified in runtime
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (ClientIdTypeAdapter.adapts(type)) {
      return (TypeAdapter<T>) new ClientIdTypeAdapter(gson);
    }
    return null;
  }

  @Override
  public String toString() {
    return "GsonAdaptersClientId(ClientId)";
  }

  @org.immutables.value.Generated(from = "ClientId", generator = "Gsons")
  @SuppressWarnings({"unchecked", "raw"}) // safe unchecked, types are verified in runtime
  private static class ClientIdTypeAdapter extends TypeAdapter<ClientId> {

    ClientIdTypeAdapter(Gson gson) {
    } 

    static boolean adapts(TypeToken<?> type) {
      return ClientId.class == type.getRawType()
          || ImmutableClientId.class == type.getRawType();
    }

    @Override
    public void write(JsonWriter out, ClientId value) throws IOException {
      if (value == null) {
        out.nullValue();
      } else {
        writeClientId(out, value);
      }
    }

    @Override
    public ClientId read(JsonReader in) throws IOException {
      return readClientId(in);
    }

    private void writeClientId(JsonWriter out, ClientId instance)
        throws IOException {
      out.beginObject();
      out.name("clientId");
      out.value(instance.getClientId());
      out.endObject();
    }

    private  ClientId readClientId(JsonReader in)
        throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      ImmutableClientId.Builder builder = ImmutableClientId.builder();
      in.beginObject();
      while (in.hasNext()) {
        eachAttribute(in, builder);
      }
      in.endObject();
      return builder.build();
    }

    private void eachAttribute(JsonReader in, ImmutableClientId.Builder builder)
        throws IOException {
      String attributeName = in.nextName();
      switch (attributeName.charAt(0)) {
      case 'c':
        if ("clientId".equals(attributeName)) {
          readInClientId(in, builder);
          return;
        }
        break;
      default:
      }
      in.skipValue();
    }

    private void readInClientId(JsonReader in, ImmutableClientId.Builder builder)
        throws IOException {
      builder.clientId(in.nextString());
    }
  }
}
