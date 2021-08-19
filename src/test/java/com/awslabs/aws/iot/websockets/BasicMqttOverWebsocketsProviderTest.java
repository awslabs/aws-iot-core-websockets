package com.awslabs.aws.iot.websockets;

import com.awslabs.aws.iot.websockets.data.*;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jetbrains.annotations.NotNull;
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
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

public class BasicMqttOverWebsocketsProviderTest {
    private BasicMqttOverWebsocketsProvider basicMqttOverWebsocketsProvider;
    private ImmutableClientId clientId;
    private AwsCredentialsProvider awsCredentialsProvider;
    private Region region;
    private ScopeDownPolicy goodConnectScopeDownPolicy;
    private ScopeDownPolicy badConnectScopeDownPolicy;
    private ScopeDownPolicy goodConnectAndPublishScopeDownPolicy;
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
        String badClientIdArn = String.join("", goodClientIdArn, "xxx");
        String publishAnywhereArn = String.join("", "arn:aws:iot:", region.id(), ":", accountId, ":topic/", "*");

        PolicyAction connectAction = new PolicyAction("iot:Connect");
        PolicyAction publishAction = new PolicyAction("iot:Publish");

        PolicyResource goodClientIdResource = new PolicyResource(goodClientIdArn);
        PolicyResource badClientIdResource = new PolicyResource(badClientIdArn);
        PolicyResource publishAnywhereResource = new PolicyResource(publishAnywhereArn);

        ScopeDownPolicyStatement goodConnectScopeDownPolicyStatement = new ScopeDownPolicyStatement(PolicyEffect.Allow, Arrays.asList(connectAction), Arrays.asList(goodClientIdResource));
        ScopeDownPolicyStatement badConnectScopeDownPolicyStatement = new ScopeDownPolicyStatement(PolicyEffect.Allow, Arrays.asList(connectAction), Arrays.asList(badClientIdResource));
        ScopeDownPolicyStatement publishAnywhereScopeDownPolicyStatement = new ScopeDownPolicyStatement(PolicyEffect.Allow, Arrays.asList(publishAction), Arrays.asList(publishAnywhereResource));

        goodConnectScopeDownPolicy = new ScopeDownPolicy(Arrays.asList(goodConnectScopeDownPolicyStatement));
        badConnectScopeDownPolicy = new ScopeDownPolicy(Arrays.asList(badConnectScopeDownPolicyStatement));
        goodConnectAndPublishScopeDownPolicy = new ScopeDownPolicy(Arrays.asList(goodConnectScopeDownPolicyStatement, publishAnywhereScopeDownPolicyStatement));

        emptyEndpoint = ImmutableEndpointAddress.builder().build();
        emptyRoleToAssume = ImmutableRoleToAssume.builder().build();
    }

    @Test
    public void shouldGetAClientAndConnect() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, MqttException {
        MqttClientConfig mqttClientConfig = ImmutableMqttClientConfig.builder().clientId(clientId).build();
        MqttClient mqttClient = basicMqttOverWebsocketsProvider.getMqttClient(mqttClientConfig);
        basicMqttOverWebsocketsProvider.connect(mqttClient);
    }

    @Test
    public void shouldGetAClientAndConnectWithScopeDownPolicy() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, MqttException {
        MqttClientConfig mqttClientConfig = getMqttClientConfig(goodConnectScopeDownPolicy);
        MqttClient mqttClient = basicMqttOverWebsocketsProvider.getMqttClient(mqttClientConfig);
        basicMqttOverWebsocketsProvider.connect(mqttClient);
    }

    @Test
    public void shouldGetAClientAndFailToConnectWithScopeDownPolicy() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, MqttException {
        MqttClientConfig mqttClientConfig = getMqttClientConfig(badConnectScopeDownPolicy);
        MqttClient mqttClient = basicMqttOverWebsocketsProvider.getMqttClient(mqttClientConfig);
        MqttException mqttException = assertThrows(MqttException.class, () -> basicMqttOverWebsocketsProvider.connect(mqttClient));
        assertThat(mqttException.getCause().getMessage(), containsString("Already connected"));
    }

    @Test
    public void shouldPublishAMessageWithAPermissivePolicy() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, MqttException {
        MqttClientConfig mqttClientConfig = getMqttClientConfig(goodConnectAndPublishScopeDownPolicy);
        MqttClient mqttClient = basicMqttOverWebsocketsProvider.getMqttClient(mqttClientConfig);
        basicMqttOverWebsocketsProvider.connect(mqttClient);

        String topic = "test";
        MqttMessage mqttMessage = new MqttMessage("testing".getBytes());
        mqttClient.publish(topic, mqttMessage);
    }

    @Test
    public void shouldNotPublishAMessageWithAConnectOnlyPolicy() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, MqttException {
        MqttClientConfig mqttClientConfig = getMqttClientConfig(goodConnectScopeDownPolicy);
        MqttClient mqttClient = basicMqttOverWebsocketsProvider.getMqttClient(mqttClientConfig);
        basicMqttOverWebsocketsProvider.connect(mqttClient);

        String topic = "test";
        MqttMessage mqttMessage = new MqttMessage("testing".getBytes());
        MqttException mqttException = assertThrows(MqttException.class, () -> mqttClient.publish(topic, mqttMessage));
        assertThat(mqttException.getCause(), is(instanceOf(EOFException.class)));
    }

    @NotNull
    private MqttClientConfig getMqttClientConfig(ScopeDownPolicy scopeDownPolicy) {
        MqttOverWebsocketsUriConfig mqttOverWebsocketsUriConfig = ImmutableMqttOverWebsocketsUriConfig.builder()
                .optionalScopeDownPolicy(scopeDownPolicy)
                .build();

        return ImmutableMqttClientConfig.builder()
                .clientId(clientId)
                .optionalMqttOverWebsocketsUriConfig(mqttOverWebsocketsUriConfig)
                .build();
    }
}
