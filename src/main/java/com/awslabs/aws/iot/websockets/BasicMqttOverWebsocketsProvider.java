package com.awslabs.aws.iot.websockets;

import com.awslabs.aws.iot.websockets.data.*;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.AwsRegionProviderChain;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.iot.IotClient;
import software.amazon.awssdk.services.iot.IotClientBuilder;
import software.amazon.awssdk.services.iot.model.DescribeEndpointRequest;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.StsClientBuilder;
import software.amazon.awssdk.services.sts.model.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BasicMqttOverWebsocketsProvider implements MqttOverWebsocketsProvider {
    private static final String ARN_AWS_IAM = "arn:aws:iam::";
    private static final Logger log = LoggerFactory.getLogger(BasicMqttOverWebsocketsProvider.class);

    private static final ApacheHttpClient.Builder apacheClientBuilder = ApacheHttpClient.builder();
    private static final Map<Optional<Region>, String> endpointMap = new HashMap<>();

    // This is not private so that a test can override it if necessary
    AwsCredentialsProvider credentialsProvider = DefaultCredentialsProvider.create();

    @Inject
    public BasicMqttOverWebsocketsProvider() {
    }

    @Override
    public MqttClient getMqttClient(MqttClientConfig mqttClientConfig) throws MqttException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String mqttOverWebsocketsUri = getMqttOverWebsocketsUri(mqttClientConfig.getOptionalMqttOverWebsocketsUriConfig());

        MemoryPersistence persistence = new MemoryPersistence();

        return new MqttClient(mqttOverWebsocketsUri, mqttClientConfig.getClientId().getClientId(), persistence);
    }

    @Override
    public MqttAsyncClient getMqttAsyncClient(MqttClientConfig mqttClientConfig) throws MqttException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String mqttOverWebsocketsUri = getMqttOverWebsocketsUri(mqttClientConfig.getOptionalMqttOverWebsocketsUriConfig());

        MemoryPersistence persistence = new MemoryPersistence();

        return new MqttAsyncClient(mqttOverWebsocketsUri, mqttClientConfig.getClientId().getClientId(), persistence);
    }

    @Override
    public void connect(MqttClient mqttClient) throws MqttException {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        mqttClient.connect(connOpts);
    }

    @Override
    public void connect(MqttClient mqttClient, ImmutableUsernamePassword usernamePassword) throws MqttException {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        setUsernamePassword(usernamePassword, connOpts);
        mqttClient.connect(connOpts);
    }

    private void setUsernamePassword(ImmutableUsernamePassword usernamePassword, MqttConnectOptions connOpts) {
        usernamePassword.getUsername().ifPresent(connOpts::setUserName);
        usernamePassword.getPassword().ifPresent(connOpts::setPassword);
    }

    @Override
    public Optional<IMqttToken> connect(MqttAsyncClient mqttAsyncClient) throws MqttException {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);

        return Optional.of(mqttAsyncClient.connect(connOpts));
    }

    @Override
    public Optional<IMqttToken> connect(MqttAsyncClient mqttAsyncClient, ImmutableUsernamePassword usernamePassword, Object userContext, IMqttActionListener callback) throws MqttException {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        setUsernamePassword(usernamePassword, connOpts);

        return Optional.of(mqttAsyncClient.connect(connOpts, userContext, callback));
    }

    private String getDateStamp(DateTime dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd");
        return dateTimeFormatter.print(dateTime.withZone(DateTimeZone.UTC));
    }

    private String getAmzDate(DateTime dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd'T'HHmmss'Z'");
        return dateTimeFormatter.print(dateTime.withZone(DateTimeZone.UTC));
    }

    // Derived from: http://docs.aws.amazon.com/iot/latest/developerguide/iot-dg.pdf
    @Override
    public String getMqttOverWebsocketsUri(Optional<MqttOverWebsocketsUriConfig> optionalMqttOverWebsocketsUriConfig) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        Optional<String> optionalScopeDownJson = Optional.empty();

        if (optionalMqttOverWebsocketsUriConfig.isPresent()) {
            // We have a websockets config, have they've specified both the policy JSON and the policy object?
            MqttOverWebsocketsUriConfig mqttOverWebsocketsUriConfig = optionalMqttOverWebsocketsUriConfig.get();
            if (mqttOverWebsocketsUriConfig.optionalScopeDownPolicy().isPresent() &&
                    mqttOverWebsocketsUriConfig.optionalScopeDownPolicyJson().isPresent()) {
                throw new RuntimeException("Scope down policy object and scope down policy JSON can not be used simultaneously. Use either a scope down policy object or scope down policy JSON but not both.");
            }

            if (mqttOverWebsocketsUriConfig.optionalScopeDownPolicy().isPresent()) {
                optionalScopeDownJson = mqttOverWebsocketsUriConfig.optionalScopeDownPolicy().map(ScopeDownPolicy::toString);
            } else if (mqttOverWebsocketsUriConfig.optionalScopeDownPolicyJson().isPresent()) {
                optionalScopeDownJson = mqttOverWebsocketsUriConfig.optionalScopeDownPolicyJson();
            }
        }

        long time = System.currentTimeMillis();
        DateTime dateTime = new DateTime(time);
        String dateStamp = getDateStamp(dateTime);
        String amzdate = getAmzDate(dateTime);
        String service = "iotdata";
        Optional<Region> optionalRegion = optionalMqttOverWebsocketsUriConfig.flatMap(MqttOverWebsocketsUriConfig::optionalRegion);
        Region region = optionalRegion.orElseGet(this::getDefaultRegionString);
        String regionString = region.toString();
        String clientEndpoint = optionalMqttOverWebsocketsUriConfig
                .flatMap(MqttOverWebsocketsUriConfig::optionalEndpointAddress)
                .flatMap(EndpointAddress::getEndpointAddress)
                .orElseGet(() -> getEndpointAddressForRegion(optionalRegion));

        AwsCredentials awsCredentials;
        String awsAccessKeyId;
        String awsSecretAccessKey;
        Optional<String> optionalSessionToken = Optional.empty();

        Optional<String> optionalRoleToAssume = optionalMqttOverWebsocketsUriConfig
                .flatMap(MqttOverWebsocketsUriConfig::optionalRoleToAssume)
                .flatMap(RoleToAssume::getRoleToAssume);

        StsClient stsClient = getStsClient(optionalRegion);

        if (!optionalRoleToAssume.isPresent()) {
            if (optionalScopeDownJson.isPresent()) {
                // There is a scope down policy, get a federation token with it

                // Trim the UUID down to a size that STS will accept
                String name = UUID.randomUUID().toString().substring(0, 31);

                GetFederationTokenRequest getFederationTokenRequest = GetFederationTokenRequest.builder()
                        .name(name)
                        .policy(optionalScopeDownJson.get())
                        .build();

                GetFederationTokenResponse getFederationTokenResponse = stsClient.getFederationToken(getFederationTokenRequest);

                Credentials credentials = getFederationTokenResponse.credentials();

                awsAccessKeyId = credentials.accessKeyId();
                awsSecretAccessKey = credentials.secretAccessKey();
                optionalSessionToken = Optional.of(credentials.sessionToken());
            } else {
                // No scope down policy, just use the user's existing permissions
                awsCredentials = credentialsProvider.resolveCredentials();
                awsAccessKeyId = awsCredentials.accessKeyId();
                awsSecretAccessKey = awsCredentials.secretAccessKey();

                if (awsCredentials instanceof AwsSessionCredentials) {
                    optionalSessionToken = Optional.of(((AwsSessionCredentials) awsCredentials).sessionToken());
                }
            }
        } else {
            // Assume a new role
            String roleArn = optionalRoleToAssume.get();

            if (!roleArn.startsWith(ARN_AWS_IAM)) {
                // The role coming from the environment will be the full ARN, if this is just the role name add the proper prefix
                String accountId = getAccountId(stsClient);

                roleArn = ARN_AWS_IAM + accountId + ":role/" + roleArn;
            }

            log.debug("Attempting to assume role: " + roleArn);

            AssumeRoleRequest.Builder assumeRoleRequestBuilder = AssumeRoleRequest.builder()
                    .roleArn(roleArn)
                    .roleSessionName("aws-iot-core-websockets-java");

            // Add the scope down policy if there is one
            optionalScopeDownJson.ifPresent(assumeRoleRequestBuilder::policy);

            AssumeRoleRequest assumeRoleRequest = assumeRoleRequestBuilder.build();
            AssumeRoleResponse assumeRoleResult = stsClient.assumeRole(assumeRoleRequest);

            Credentials credentials = assumeRoleResult.credentials();
            awsSecretAccessKey = credentials.secretAccessKey();
            awsAccessKeyId = credentials.accessKeyId();
            optionalSessionToken = Optional.of(credentials.sessionToken());
        }

        String algorithm = "AWS4-HMAC-SHA256";
        String method = "GET";
        String canonicalUri = "/mqtt";

        String credentialScope = dateStamp + "/" + regionString + "/" + service + "/" + "aws4_request";
        String canonicalQuerystring = "X-Amz-Algorithm=AWS4-HMAC-SHA256";
        canonicalQuerystring += "&X-Amz-Credential=" + URLEncoder.encode(awsAccessKeyId + "/" + credentialScope, "UTF-8");
        canonicalQuerystring += "&X-Amz-Date=" + amzdate;
        canonicalQuerystring += "&X-Amz-SignedHeaders=host";

        String canonicalHeaders = "host:" + clientEndpoint + ":443\n";
        String payloadHash = sha256("");
        String canonicalRequest = method + "\n" + canonicalUri + "\n" + canonicalQuerystring + "\n" + canonicalHeaders + "\nhost\n" + payloadHash;

        String stringToSign = algorithm + "\n" + amzdate + "\n" + credentialScope + "\n" + sha256(canonicalRequest);
        byte[] signingKey = getSignatureKey(awsSecretAccessKey, dateStamp, regionString, service);
        String signature = sign(signingKey, stringToSign);

        canonicalQuerystring += "&X-Amz-Signature=" + signature;
        String requestUrl = "wss://" + clientEndpoint + canonicalUri + "?" + canonicalQuerystring;

        if (optionalSessionToken.isPresent()) {
            requestUrl += "&X-Amz-Security-Token=" + URLEncoder.encode(optionalSessionToken.get(), "UTF-8");
        }

        return requestUrl;
    }

    private StsClient getStsClient(Optional<Region> optionalRegion) {
        StsClientBuilder builder = StsClient.builder()
                .httpClientBuilder(apacheClientBuilder);
        optionalRegion.ifPresent(builder::region);

        return builder.build();
    }

    public String getAccountId(StsClient stsClient) {
        return stsClient.getCallerIdentity(GetCallerIdentityRequest.builder().build()).account();
    }

    private String getEndpointAddressForRegion(Optional<Region> optionalRegion) {
        if (!endpointMap.containsKey(optionalRegion)) {
            DescribeEndpointRequest describeEndpointRequest = DescribeEndpointRequest.builder()
                    .endpointType("iot:Data-ATS")
                    .build();
            String endpointAddress = getIotClient(optionalRegion).describeEndpoint(describeEndpointRequest).endpointAddress();

            endpointMap.put(optionalRegion, endpointAddress);
        }

        return endpointMap.get(optionalRegion);
    }

    private IotClient getIotClient(Optional<Region> optionalRegion) {
        IotClientBuilder builder = IotClient.builder()
                .httpClientBuilder(apacheClientBuilder);
        optionalRegion.ifPresent(builder::region);

        return builder.build();

    }

    private Region getDefaultRegionString() {
        AwsRegionProviderChain awsRegionProviderChain = new DefaultAwsRegionProviderChain();
        return awsRegionProviderChain.getRegion();
    }

    private byte[] HmacSHA256(String data, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String algorithm = "HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data.getBytes("UTF8"));
    }

    private byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
        byte[] kDate = HmacSHA256(dateStamp, kSecret);
        byte[] kRegion = HmacSHA256(regionName, kDate);
        byte[] kService = HmacSHA256(serviceName, kRegion);
        byte[] kSigning = HmacSHA256("aws4_request", kService);

        return kSigning;
    }

    private String sign(byte[] key, String message) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        byte[] hash = HmacSHA256(message, key);
        return bytesToHex(hash);
    }

    // From: https://gist.github.com/avilches/750151
    private String sha256(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data.getBytes());

        return bytesToHex(md.digest());
    }

    // From: https://gist.github.com/avilches/750151
    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}
