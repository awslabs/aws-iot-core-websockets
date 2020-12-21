package com.awslabs.aws.iot.websockets.data;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.google.gson.stream.*;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.processing.Generated;

/**
 * A {@code TypeAdapterFactory} that handles all of the immutable types generated under {@code UsernamePassword}.
 * @see ImmutableUsernamePassword
 */
@SuppressWarnings({"all", "MethodCanBeStatic"})
@Generated("org.immutables.processor.ProxyProcessor")
@org.immutables.value.Generated(from = "com.awslabs.aws.iot.websockets.data", generator = "Gsons")
public final class GsonAdaptersUsernamePassword implements TypeAdapterFactory {
  @SuppressWarnings({"unchecked", "raw"}) // safe unchecked, types are verified in runtime
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (UsernamePasswordTypeAdapter.adapts(type)) {
      return (TypeAdapter<T>) new UsernamePasswordTypeAdapter(gson);
    }
    return null;
  }

  @Override
  public String toString() {
    return "GsonAdaptersUsernamePassword(UsernamePassword)";
  }

  @org.immutables.value.Generated(from = "UsernamePassword", generator = "Gsons")
  @SuppressWarnings({"unchecked", "raw"}) // safe unchecked, types are verified in runtime
  private static class UsernamePasswordTypeAdapter extends TypeAdapter<UsernamePassword> {
    public final char[] passwordTypeSample = null;
    private final TypeAdapter<char[]> passwordTypeAdapter;

    UsernamePasswordTypeAdapter(Gson gson) {
      this.passwordTypeAdapter = gson.getAdapter( TypeToken.get(char[].class));
    } 

    static boolean adapts(TypeToken<?> type) {
      return UsernamePassword.class == type.getRawType()
          || ImmutableUsernamePassword.class == type.getRawType();
    }

    @Override
    public void write(JsonWriter out, UsernamePassword value) throws IOException {
      if (value == null) {
        out.nullValue();
      } else {
        writeUsernamePassword(out, value);
      }
    }

    @Override
    public UsernamePassword read(JsonReader in) throws IOException {
      return readUsernamePassword(in);
    }

    private void writeUsernamePassword(JsonWriter out, UsernamePassword instance)
        throws IOException {
      out.beginObject();
      Optional<String> usernameOptional = instance.getUsername();
      if (usernameOptional.isPresent()) {
        out.name("username");
        String usernameValue = usernameOptional.get();
        out.value(usernameValue);
      } else if (out.getSerializeNulls()) {
        out.name("username");
        out.nullValue();
      }
      Optional<char[]> passwordOptional = instance.getPassword();
      if (passwordOptional.isPresent()) {
        out.name("password");
        char[] passwordValue = passwordOptional.get();
        passwordTypeAdapter.write(out, passwordValue);
      } else if (out.getSerializeNulls()) {
        out.name("password");
        out.nullValue();
      }
      out.endObject();
    }

    private  UsernamePassword readUsernamePassword(JsonReader in)
        throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      ImmutableUsernamePassword.Builder builder = ImmutableUsernamePassword.builder();
      in.beginObject();
      while (in.hasNext()) {
        eachAttribute(in, builder);
      }
      in.endObject();
      return builder.build();
    }

    private void eachAttribute(JsonReader in, ImmutableUsernamePassword.Builder builder)
        throws IOException {
      String attributeName = in.nextName();
      switch (attributeName.charAt(0)) {
      case 'u':
        if ("username".equals(attributeName)) {
          readInUsername(in, builder);
          return;
        }
        break;
      case 'p':
        if ("password".equals(attributeName)) {
          readInPassword(in, builder);
          return;
        }
        break;
      default:
      }
      in.skipValue();
    }

    private void readInUsername(JsonReader in, ImmutableUsernamePassword.Builder builder)
        throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
      } else {
        builder.username(in.nextString());
      }
    }

    private void readInPassword(JsonReader in, ImmutableUsernamePassword.Builder builder)
        throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
      } else {
        char[] value = passwordTypeAdapter.read(in);
        builder.password(value);
      }
    }
  }
}
