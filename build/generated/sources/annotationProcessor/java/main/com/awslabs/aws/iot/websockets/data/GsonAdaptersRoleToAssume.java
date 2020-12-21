package com.awslabs.aws.iot.websockets.data;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.google.gson.stream.*;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.processing.Generated;

/**
 * A {@code TypeAdapterFactory} that handles all of the immutable types generated under {@code RoleToAssume}.
 * @see ImmutableRoleToAssume
 */
@SuppressWarnings({"all", "MethodCanBeStatic"})
@Generated("org.immutables.processor.ProxyProcessor")
@org.immutables.value.Generated(from = "com.awslabs.aws.iot.websockets.data", generator = "Gsons")
public final class GsonAdaptersRoleToAssume implements TypeAdapterFactory {
  @SuppressWarnings({"unchecked", "raw"}) // safe unchecked, types are verified in runtime
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (RoleToAssumeTypeAdapter.adapts(type)) {
      return (TypeAdapter<T>) new RoleToAssumeTypeAdapter(gson);
    }
    return null;
  }

  @Override
  public String toString() {
    return "GsonAdaptersRoleToAssume(RoleToAssume)";
  }

  @org.immutables.value.Generated(from = "RoleToAssume", generator = "Gsons")
  @SuppressWarnings({"unchecked", "raw"}) // safe unchecked, types are verified in runtime
  private static class RoleToAssumeTypeAdapter extends TypeAdapter<RoleToAssume> {

    RoleToAssumeTypeAdapter(Gson gson) {
    } 

    static boolean adapts(TypeToken<?> type) {
      return RoleToAssume.class == type.getRawType()
          || ImmutableRoleToAssume.class == type.getRawType();
    }

    @Override
    public void write(JsonWriter out, RoleToAssume value) throws IOException {
      if (value == null) {
        out.nullValue();
      } else {
        writeRoleToAssume(out, value);
      }
    }

    @Override
    public RoleToAssume read(JsonReader in) throws IOException {
      return readRoleToAssume(in);
    }

    private void writeRoleToAssume(JsonWriter out, RoleToAssume instance)
        throws IOException {
      out.beginObject();
      Optional<String> roleToAssumeOptional = instance.getRoleToAssume();
      if (roleToAssumeOptional.isPresent()) {
        out.name("roleToAssume");
        String roleToAssumeValue = roleToAssumeOptional.get();
        out.value(roleToAssumeValue);
      } else if (out.getSerializeNulls()) {
        out.name("roleToAssume");
        out.nullValue();
      }
      out.endObject();
    }

    private  RoleToAssume readRoleToAssume(JsonReader in)
        throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      ImmutableRoleToAssume.Builder builder = ImmutableRoleToAssume.builder();
      in.beginObject();
      while (in.hasNext()) {
        eachAttribute(in, builder);
      }
      in.endObject();
      return builder.build();
    }

    private void eachAttribute(JsonReader in, ImmutableRoleToAssume.Builder builder)
        throws IOException {
      String attributeName = in.nextName();
      switch (attributeName.charAt(0)) {
      case 'r':
        if ("roleToAssume".equals(attributeName)) {
          readInRoleToAssume(in, builder);
          return;
        }
        break;
      default:
      }
      in.skipValue();
    }

    private void readInRoleToAssume(JsonReader in, ImmutableRoleToAssume.Builder builder)
        throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
      } else {
        builder.roleToAssume(in.nextString());
      }
    }
  }
}
