package com.awslabs.aws.iot.websockets.data;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.Optional;

@Gson.TypeAdapters
@Value.Immutable
public abstract class UsernamePassword extends NoToString {
    public abstract Optional<String> getUsername();

    public abstract Optional<char[]> getPassword();
}
