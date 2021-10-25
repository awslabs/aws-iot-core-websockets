package com.awslabs.aws.iot.websockets;

import com.awslabs.aws.iot.websockets.data.*;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.Credentials;
import software.amazon.awssdk.services.sts.model.GetFederationTokenRequest;
import software.amazon.awssdk.services.sts.model.GetFederationTokenResponse;
import software.amazon.awssdk.services.sts.model.PolicyDescriptorType;

import java.io.EOFException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

public class BasicMqttOverWebsocketsProviderTest {
    private BasicMqttOverWebsocketsProvider basicMqttOverWebsocketsProvider;
    private ImmutableClientId clientId;
    private Region region;
    private ScopeDownPolicy goodConnectScopeDownPolicy;
    private ScopeDownPolicy badConnectScopeDownPolicy;
    private ScopeDownPolicy goodConnectAndPublishScopeDownPolicy;
    private ImmutableEndpointAddress emptyEndpoint;
    private ImmutableRoleToAssume emptyRoleToAssume;
    private StaticCredentialsProvider staticCredentialsProvider;

    @Before
    public void setup() {
        StsClient stsClient = StsClient.create();
        PolicyDescriptorType policyDescriptorType = PolicyDescriptorType.builder()
                .arn("arn:aws:iam::aws:policy/AdministratorAccess")
                .build();
        GetFederationTokenRequest getFederationTokenRequest = GetFederationTokenRequest.builder()
                .name("temp")
                .policyArns(policyDescriptorType)
                .build();
        GetFederationTokenResponse stsCredentials = stsClient.getFederationToken(getFederationTokenRequest);
        Credentials credentials = stsCredentials.credentials();
        AwsCredentials awsCredentials = AwsSessionCredentials.create(credentials.accessKeyId(), credentials.secretAccessKey(), credentials.sessionToken());
        staticCredentialsProvider = StaticCredentialsProvider.create(awsCredentials);
        region = new DefaultAwsRegionProviderChain().getRegion();
        basicMqttOverWebsocketsProvider = new BasicMqttOverWebsocketsProvider();
        String accountId = basicMqttOverWebsocketsProvider.getAccountId.apply(stsClient);
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
        MqttClientConfig mqttClientConfig = getMqttClientConfigWithScopeDown(goodConnectScopeDownPolicy);
        MqttClient mqttClient = basicMqttOverWebsocketsProvider.getMqttClient(mqttClientConfig);
        basicMqttOverWebsocketsProvider.connect(mqttClient);
    }

    @Test
    public void shouldGetAClientAndFailToConnectWithScopeDownPolicy() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, MqttException {
        MqttClientConfig mqttClientConfig = getMqttClientConfigWithScopeDown(badConnectScopeDownPolicy);
        MqttClient mqttClient = basicMqttOverWebsocketsProvider.getMqttClient(mqttClientConfig);
        MqttException mqttException = assertThrows(MqttException.class, () -> basicMqttOverWebsocketsProvider.connect(mqttClient));
        assertThat(mqttException.getCause().getMessage(), containsString("Already connected"));
    }

    @Test
    public void shouldPublishAMessageWithAPermissivePolicy() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, MqttException {
        MqttClientConfig mqttClientConfig = getMqttClientConfigWithScopeDown(goodConnectAndPublishScopeDownPolicy);
        MqttClient mqttClient = basicMqttOverWebsocketsProvider.getMqttClient(mqttClientConfig);
        basicMqttOverWebsocketsProvider.connect(mqttClient);

        String topic = "test";
        MqttMessage mqttMessage = new MqttMessage("testing".getBytes());
        mqttClient.publish(topic, mqttMessage);
    }

    @Test
    public void shouldPublishWithPermissiveTemporaryCredentials() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, MqttException {
        MqttClientConfig mqttClientConfig = getMqttClientConfigWithoutScopeDown();
        MqttClient mqttClient = basicMqttOverWebsocketsProvider.getMqttClient(mqttClientConfig);
        basicMqttOverWebsocketsProvider.connect(mqttClient);

        String topic = "test";
        MqttMessage mqttMessage = new MqttMessage("testing".getBytes());
        mqttClient.publish(topic, mqttMessage);
    }

    @Test
    public void shouldNotPublishAMessageWithAConnectOnlyPolicy() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, MqttException {
        MqttClientConfig mqttClientConfig = getMqttClientConfigWithScopeDown(goodConnectScopeDownPolicy);
        MqttClient mqttClient = basicMqttOverWebsocketsProvider.getMqttClient(mqttClientConfig);
        basicMqttOverWebsocketsProvider.connect(mqttClient);

        String topic = "test";
        MqttMessage mqttMessage = new MqttMessage("testing".getBytes());
        MqttException mqttException = assertThrows(MqttException.class, () -> mqttClient.publish(topic, mqttMessage));
        assertThat(mqttException.getCause(), is(instanceOf(EOFException.class)));
    }

    @Test
    public void shouldThrowAnExceptionWithTwoScopeDownPolicies() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, MqttException {
        MqttOverWebsocketsUriConfig mqttOverWebsocketsUriConfig = ImmutableMqttOverWebsocketsUriConfig.builder()
                .optionalScopeDownPolicy(new ScopeDownPolicy("", null))
                .optionalScopeDownPolicyJson("")
                .build();
        MqttClientConfig mqttClientConfig = ImmutableMqttClientConfig.builder()
                .clientId(clientId)
                .optionalMqttOverWebsocketsUriConfig(mqttOverWebsocketsUriConfig)
                .build();
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> basicMqttOverWebsocketsProvider.getMqttClient(mqttClientConfig));
        assertThat(runtimeException.getMessage(), containsString("but not both"));
    }

    @NotNull
    private MqttClientConfig getMqttClientConfigWithScopeDown(ScopeDownPolicy scopeDownPolicy) {
        MqttOverWebsocketsUriConfig mqttOverWebsocketsUriConfig = ImmutableMqttOverWebsocketsUriConfig.builder()
                .optionalScopeDownPolicy(scopeDownPolicy)
                .build();

        return ImmutableMqttClientConfig.builder()
                .clientId(clientId)
                .optionalMqttOverWebsocketsUriConfig(mqttOverWebsocketsUriConfig)
                .build();
    }

    @NotNull
    private MqttClientConfig getMqttClientConfigWithoutScopeDown() {
        AwsCredentialsProviderChain awsCredentialsProviderChain = AwsCredentialsProviderChain.builder()
                .addCredentialsProvider(staticCredentialsProvider)
                .build();

        MqttOverWebsocketsUriConfig mqttOverWebsocketsUriConfig = ImmutableMqttOverWebsocketsUriConfig.builder()
                .optionalAwsCredentialsProviderChain(Optional.of(awsCredentialsProviderChain))
                .build();

        return ImmutableMqttClientConfig.builder()
                .clientId(clientId)
                .optionalMqttOverWebsocketsUriConfig(mqttOverWebsocketsUriConfig)
                .build();
    }
}
