package com.awslabs.aws.iot.websockets;

import com.awslabs.aws.iot.websockets.data.*;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.sts.StsClient;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
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

        String goodConnectScopeDownPolicyString = goodConnectScopeDownPolicy.toString();
        String badConnectScopeDownPolicyString = badConnectScopeDownPolicy.toString();

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
}
