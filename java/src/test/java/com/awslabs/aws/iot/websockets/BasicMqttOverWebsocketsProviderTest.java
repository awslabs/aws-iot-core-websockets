package com.awslabs.aws.iot.websockets;

import com.awslabs.aws.iot.websockets.data.*;
import com.google.common.util.concurrent.RateLimiter;
import io.vavr.control.Try;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Before;
import org.junit.Test;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.sts.StsClient;

import java.io.EOFException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

public class BasicMqttOverWebsocketsProviderTest {
    private BasicMqttOverWebsocketsProvider basicMqttOverWebsocketsProvider;
    private ImmutableClientId clientId;
    private AwsCredentialsProvider awsCredentialsProvider;
    private Region region;
    private ScopeDownPolicy goodConnectScopeDownPolicy;
    private ScopeDownPolicy badConnectScopeDownPolicy;
    private ImmutableEndpointAddress emptyEndpoint;
    private ImmutableRoleToAssume emptyRoleToAssume;

    @Before
    public void setup() {
        awsCredentialsProvider = DefaultCredentialsProvider.create();
        region = new DefaultAwsRegionProviderChain().getRegion();
        basicMqttOverWebsocketsProvider = new BasicMqttOverWebsocketsProvider();
        String accountId = basicMqttOverWebsocketsProvider.getAccountId(StsClient.create());
        clientId = ImmutableClientId.builder().clientId(UUID.randomUUID().toString()).build();

        String goodClientIdArn = String.join("", "arn:aws:iot:", region.id(), ":", accountId, ":client/", clientId.getClientId());
        String badClientIdArn = String.join("", goodClientIdArn, "1");

        PolicyAction connectAction = new PolicyAction("iot:Connect");

        PolicyResource goodClientIdResource = new PolicyResource(goodClientIdArn);

        PolicyResource badClientIdResource = new PolicyResource(badClientIdArn);

        ScopeDownPolicyStatement goodConnectScopeDownPolicyStatement = new ScopeDownPolicyStatement(PolicyEffect.Allow, Arrays.asList(connectAction), Arrays.asList(goodClientIdResource));

        ScopeDownPolicyStatement badConnectScopeDownPolicyStatement = new ScopeDownPolicyStatement(PolicyEffect.Allow, Arrays.asList(connectAction), Arrays.asList(badClientIdResource));

        goodConnectScopeDownPolicy = new ScopeDownPolicy(Arrays.asList(goodConnectScopeDownPolicyStatement));

        badConnectScopeDownPolicy = new ScopeDownPolicy(Arrays.asList(badConnectScopeDownPolicyStatement));

        emptyEndpoint = ImmutableEndpointAddress.builder().build();
        emptyRoleToAssume = ImmutableRoleToAssume.builder().build();
    }

    @Test
    public void shouldGetAClientAndConnect() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, MqttException {
        MqttClient mqttClient = basicMqttOverWebsocketsProvider.getMqttClient(clientId);
        basicMqttOverWebsocketsProvider.connect(mqttClient);
    }

    @Test
    public void shouldGetAClientAndConnectWithScopeDownPolicy() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, MqttException {
        MqttClient mqttClient = basicMqttOverWebsocketsProvider.getMqttClient(clientId, Optional.empty(), emptyEndpoint, emptyRoleToAssume, goodConnectScopeDownPolicy);
        basicMqttOverWebsocketsProvider.connect(mqttClient);
    }

    @Test
    public void shouldGetAClientAndFailToConnectWithScopeDownPolicy() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, MqttException {
        MqttClient mqttClient = basicMqttOverWebsocketsProvider.getMqttClient(clientId, Optional.empty(), emptyEndpoint, emptyRoleToAssume, badConnectScopeDownPolicy);
        MqttException mqttException = assertThrows(MqttException.class, () -> basicMqttOverWebsocketsProvider.connect(mqttClient));
        assertThat(mqttException.getCause().getMessage(), containsString("Already connected"));
    }

    @Test
    public void shouldGetDisconnectedWhenRateIsOver512k() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, MqttException {
        MqttClient mqttClient = basicMqttOverWebsocketsProvider.getMqttClient(clientId, Optional.empty(), emptyEndpoint, emptyRoleToAssume, goodConnectScopeDownPolicy);
        basicMqttOverWebsocketsProvider.connect(mqttClient);

        // Create a 128 kB message
        String message = createTestString(128 * 1024);
        String topic = "test";
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        final RateLimiter rateLimiter = RateLimiter.create(6);

        MqttException mqttException = assertThrows(MqttException.class, () -> sendXMessages(1000, rateLimiter, mqttClient, topic, mqttMessage));
        assertThat(mqttException.getCause(), is(instanceOf(EOFException.class)));
    }

    private String createTestString(int size) {
        return new String(new char[size]).replace("\0", "x");
    }

    private void sendXMessages(int count, RateLimiter rateLimiter, MqttClient mqttClient, String topic, MqttMessage mqttMessage) throws MqttException {
        IntStream.range(0, count)
                .forEach(i -> Try.run(() -> sendRateLimitedMessage(rateLimiter, mqttClient, topic, mqttMessage)).get());
    }

    private void sendRateLimitedMessage(RateLimiter rateLimiter, MqttClient mqttClient, String topic, MqttMessage mqttMessage) throws MqttException {
        rateLimiter.acquire();
        mqttClient.publish(topic, mqttMessage);
    }
}
