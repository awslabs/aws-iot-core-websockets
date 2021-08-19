package com.awslabs.aws.iot.websockets.data;

import org.immutables.gson.Gson;
import org.immutables.value.Value;
import software.amazon.awssdk.regions.Region;

import java.util.Optional;

@Gson.TypeAdapters
@Value.Immutable
public abstract class MqttClientConfig extends NoToString {
    public abstract ClientId getClientId();

    public abstract Optional<MqttOverWebsocketsUriConfig> getOptionalMqttOverWebsocketsUriConfig();
}
