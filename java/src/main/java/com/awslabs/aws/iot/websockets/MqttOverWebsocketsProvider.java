package com.awslabs.aws.iot.websockets;

import com.awslabs.aws.iot.websockets.data.*;
import org.eclipse.paho.client.mqttv3.*;
import software.amazon.awssdk.regions.Region;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface MqttOverWebsocketsProvider {
    MqttClient getMqttClient(ImmutableClientId clientId) throws MqttException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException;

    MqttClient getMqttClient(ImmutableClientId clientId, Optional<Region> optionalRegion, ImmutableEndpointAddress optionalEndpointAddress) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, MqttException;

    MqttClient getMqttClient(ImmutableClientId clientId, Optional<Region> optionalRegion, ImmutableEndpointAddress optionalEndpointAddress, ImmutableRoleToAssume optionalRoleToAssume) throws MqttException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException;

    MqttClient getMqttClient(ImmutableClientId clientId, Optional<Region> optionalRegion, ImmutableEndpointAddress optionalEndpointAddress, ImmutableRoleToAssume optionalRoleToAssume, ImmutableScopeDownPolicy optionalScopeDownPolicy) throws MqttException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException;

    MqttAsyncClient getMqttAsyncClient(ImmutableClientId clientId) throws MqttException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException;

    MqttAsyncClient getMqttAsyncClient(ImmutableClientId clientId, Optional<Region> optionalRegion, ImmutableEndpointAddress optionalEndpointAddress) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, MqttException;

    MqttAsyncClient getMqttAsyncClient(ImmutableClientId clientId, Optional<Region> optionalRegion, ImmutableEndpointAddress optionalEndpointAddress, ImmutableRoleToAssume optionalRoleToAssume) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, MqttException;

    MqttAsyncClient getMqttAsyncClient(ImmutableClientId clientId, Optional<Region> optionalRegion, ImmutableEndpointAddress optionalEndpointAddress, ImmutableRoleToAssume optionalRoleToAssume, ImmutableScopeDownPolicy optionalScopeDownPolicy) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, MqttException;

    void connect(MqttClient mqttClient) throws MqttException;

    void connect(MqttClient mqttClient, ImmutableUsernamePassword usernamePassword) throws MqttException;

    IMqttToken connect(MqttAsyncClient mqttAsyncClient) throws MqttException;

    IMqttToken connect(MqttAsyncClient mqttAsyncClient, ImmutableUsernamePassword usernamePassword, Object userContext, IMqttActionListener callback) throws MqttException;

    // Derived from: http://docs.aws.amazon.com/iot/latest/developerguide/iot-dg.pdf
    String getMqttOverWebsocketsUri(Optional<Region> optionalRegion, ImmutableEndpointAddress optionalEndpointAddress, ImmutableRoleToAssume optionalRoleToAssume, ImmutableScopeDownPolicy optionalScopeDownPolicy) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException;
}
