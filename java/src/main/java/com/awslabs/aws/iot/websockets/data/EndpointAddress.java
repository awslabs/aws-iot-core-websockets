package com.awslabs.aws.iot.websockets.data;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class EndpointAddress extends NoToString {
    public abstract String getEndpointAddress();
}
