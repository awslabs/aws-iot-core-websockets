package com.awslabs.aws.iot.websockets.data;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.google.gson.stream.*;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.processing.Generated;

/**
 * A {@code TypeAdapterFactory} that handles all of the immutable types generated under {@code EndpointAddress}.
 * @see ImmutableEndpointAddress
 */
@SuppressWarnings({"all", "MethodCanBeStatic"})
@Generated("org.immutables.processor.ProxyProcessor")
@org.immutables.value.Generated(from = "com.awslabs.aws.iot.websockets.data", generator = "Gsons")
public final class GsonAdaptersEndpointAddress implements TypeAdapterFactory {
  @SuppressWarnings({"unchecked", "raw"}) // safe unchecked, types are verified in runtime
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (EndpointAddressTypeAdapter.adapts(type)) {
      return (TypeAdapter<T>) new EndpointAddressTypeAdapter(gson);
    }
    return null;
  }

  @Override
  public String toString() {
    return "GsonAdaptersEndpointAddress(EndpointAddress)";
  }

  @org.immutables.value.Generated(from = "EndpointAddress", generator = "Gsons")
  @SuppressWarnings({"unchecked", "raw"}) // safe unchecked, types are verified in runtime
  private static class EndpointAddressTypeAdapter extends TypeAdapter<EndpointAddress> {

    EndpointAddressTypeAdapter(Gson gson) {
    } 

    static boolean adapts(TypeToken<?> type) {
      return EndpointAddress.class == type.getRawType()
          || ImmutableEndpointAddress.class == type.getRawType();
    }

    @Override
    public void write(JsonWriter out, EndpointAddress value) throws IOException {
      if (value == null) {
        out.nullValue();
      } else {
        writeEndpointAddress(out, value);
      }
    }

    @Override
    public EndpointAddress read(JsonReader in) throws IOException {
      return readEndpointAddress(in);
    }

    private void writeEndpointAddress(JsonWriter out, EndpointAddress instance)
        throws IOException {
      out.beginObject();
      Optional<String> endpointAddressOptional = instance.getEndpointAddress();
      if (endpointAddressOptional.isPresent()) {
        out.name("endpointAddress");
        String endpointAddressValue = endpointAddressOptional.get();
        out.value(endpointAddressValue);
      } else if (out.getSerializeNulls()) {
        out.name("endpointAddress");
        out.nullValue();
      }
      out.endObject();
    }

    private  EndpointAddress readEndpointAddress(JsonReader in)
        throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      ImmutableEndpointAddress.Builder builder = ImmutableEndpointAddress.builder();
      in.beginObject();
      while (in.hasNext()) {
        eachAttribute(in, builder);
      }
      in.endObject();
      return builder.build();
    }

    private void eachAttribute(JsonReader in, ImmutableEndpointAddress.Builder builder)
        throws IOException {
      String attributeName = in.nextName();
      switch (attributeName.charAt(0)) {
      case 'e':
        if ("endpointAddress".equals(attributeName)) {
          readInEndpointAddress(in, builder);
          return;
        }
        break;
      default:
      }
      in.skipValue();
    }

    private void readInEndpointAddress(JsonReader in, ImmutableEndpointAddress.Builder builder)
        throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
      } else {
        builder.endpointAddress(in.nextString());
      }
    }
  }
}
