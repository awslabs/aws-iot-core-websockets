package com.awslabs.aws.iot.websockets.data;

import org.immutables.gson.Gson;
import org.immutables.value.Value;
import software.amazon.awssdk.regions.Region;

import java.util.Optional;

@Gson.TypeAdapters
@Value.Immutable
public abstract class MqttOverWebsocketsUriConfig extends NoToString {
    public abstract Optional<Region> optionalRegion();

    public abstract Optional<EndpointAddress> optionalEndpointAddress();

    public abstract Optional<RoleToAssume> optionalRoleToAssume();

    public abstract Optional<ScopeDownPolicy> optionalScopeDownPolicy();

    public abstract Optional<String> optionalScopeDownPolicyJson();
}
