package com.awslabs.aws.iot.websockets;

import com.awslabs.aws.iot.websockets.data.ImmutableUsernamePassword;
import com.awslabs.aws.iot.websockets.data.MqttClientConfig;
import com.awslabs.aws.iot.websockets.data.MqttOverWebsocketsUriConfig;
import org.eclipse.paho.client.mqttv3.*;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface MqttOverWebsocketsProvider {
    MqttClient getMqttClient(MqttClientConfig mqttClientConfig) throws MqttException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException;

    MqttAsyncClient getMqttAsyncClient(MqttClientConfig mqttClientConfig) throws MqttException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException;

    /**
     * Connects a synchronous MQTT client. Does nothing if it is already connected.
     *
     * @param mqttClient
     * @throws MqttException
     */
    void connect(MqttClient mqttClient) throws MqttException;

    /**
     * Connects a synchronous MQTT client. Does nothing if it is already connected.
     *
     * @param mqttClient
     * @param usernamePassword
     * @throws MqttException
     */
    void connect(MqttClient mqttClient, ImmutableUsernamePassword usernamePassword) throws MqttException;

    /**
     * Connects an asynchronous MQTT client
     *
     * @param mqttAsyncClient
     * @return Optional.empty() if already connected, otherwise an Optional of the MqttToken
     * @throws MqttException
     */
    Optional<IMqttToken> connect(MqttAsyncClient mqttAsyncClient) throws MqttException;

    /**
     * Connects an asynchronous MQTT client
     *
     * @param mqttAsyncClient
     * @param usernamePassword
     * @param userContext
     * @param callback
     * @return Optional.empty() if already connected, otherwise an Optional of the MqttToken
     * @throws MqttException
     */
    Optional<IMqttToken> connect(MqttAsyncClient mqttAsyncClient, ImmutableUsernamePassword usernamePassword, Object userContext, IMqttActionListener callback) throws MqttException;

    // Derived from: http://docs.aws.amazon.com/iot/latest/developerguide/iot-dg.pdf
    String getMqttOverWebsocketsUri(Optional<MqttOverWebsocketsUriConfig> optionalMqttOverWebsocketsUriConfig) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException;
}
