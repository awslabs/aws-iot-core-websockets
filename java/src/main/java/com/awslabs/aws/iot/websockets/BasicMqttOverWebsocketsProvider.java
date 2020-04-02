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
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.AwsRegionProviderChain;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.iot.IotClient;
import software.amazon.awssdk.services.iot.model.DescribeEndpointRequest;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
import software.amazon.awssdk.services.sts.model.Credentials;
import software.amazon.awssdk.services.sts.model.GetCallerIdentityRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class BasicMqttOverWebsocketsProvider implements MqttOverWebsocketsProvider {
    private static final String ARN_AWS_IAM = "arn:aws:iam::";
    private static final Logger log = LoggerFactory.getLogger(BasicMqttOverWebsocketsProvider.class);

    @Override
    public MqttClient getMqttClient(ImmutableClientId clientId) throws MqttException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        // Use default values for region and endpoint address
        return getMqttClient(clientId, Optional.empty(), Optional.empty());
    }

    @Override
    public MqttClient getMqttClient(ImmutableClientId clientId, Optional<Region> optionalRegion, Optional<ImmutableEndpointAddress> optionalEndpointAddress) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, MqttException {
        String mqttOverWebsocketsUri = getMqttOverWebsocketsUri(optionalRegion, optionalEndpointAddress, Optional.empty(), Optional.empty());

        MemoryPersistence persistence = new MemoryPersistence();

        return new MqttClient(mqttOverWebsocketsUri, clientId.getClientId(), persistence);
    }

    @Override
    public MqttClient getMqttClient(ImmutableClientId clientId, Optional<Region> optionalRegion, Optional<ImmutableEndpointAddress> optionalEndpointAddress, Optional<ImmutableRoleToAssume> optionalRoleToAssume) throws MqttException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String mqttOverWebsocketsUri = getMqttOverWebsocketsUri(optionalRegion, optionalEndpointAddress, optionalRoleToAssume, Optional.empty());

        MemoryPersistence persistence = new MemoryPersistence();

        return new MqttClient(mqttOverWebsocketsUri, clientId.getClientId(), persistence);
    }

    @Override
    public MqttClient getMqttClient(ImmutableClientId clientId, Optional<Region> optionalRegion, Optional<ImmutableEndpointAddress> optionalEndpointAddress, Optional<ImmutableRoleToAssume> optionalRoleToAssume, Optional<ImmutableScopeDownPolicy> optionalScopeDownPolicy) throws MqttException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String mqttOverWebsocketsUri = getMqttOverWebsocketsUri(optionalRegion, optionalEndpointAddress, optionalRoleToAssume, optionalScopeDownPolicy);

        MemoryPersistence persistence = new MemoryPersistence();

        return new MqttClient(mqttOverWebsocketsUri, clientId.getClientId(), persistence);
    }

    @Override
    public MqttAsyncClient getMqttAsyncClient(ImmutableClientId clientId) throws MqttException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        // Use default values for region and endpoint address
        return getMqttAsyncClient(clientId, Optional.empty(), Optional.empty());
    }

    @Override
    public MqttAsyncClient getMqttAsyncClient(ImmutableClientId clientId, Optional<Region> optionalRegion, Optional<ImmutableEndpointAddress> optionalEndpointAddress) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, MqttException {
        String mqttOverWebsocketsUri = getMqttOverWebsocketsUri(optionalRegion, optionalEndpointAddress, Optional.empty(), Optional.empty());

        MemoryPersistence persistence = new MemoryPersistence();

        return new MqttAsyncClient(mqttOverWebsocketsUri, clientId.getClientId(), persistence);
    }

    @Override
    public MqttAsyncClient getMqttAsyncClient(ImmutableClientId clientId, Optional<Region> optionalRegion, Optional<ImmutableEndpointAddress> optionalEndpointAddress, Optional<ImmutableRoleToAssume> optionalRoleToAssume) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, MqttException {
        String mqttOverWebsocketsUri = getMqttOverWebsocketsUri(optionalRegion, optionalEndpointAddress, optionalRoleToAssume, Optional.empty());

        MemoryPersistence persistence = new MemoryPersistence();

        return new MqttAsyncClient(mqttOverWebsocketsUri, clientId.getClientId(), persistence);
    }

    @Override
    public MqttAsyncClient getMqttAsyncClient(ImmutableClientId clientId, Optional<Region> optionalRegion, Optional<ImmutableEndpointAddress> optionalEndpointAddress, Optional<ImmutableRoleToAssume> optionalRoleToAssume, Optional<ImmutableScopeDownPolicy> optionalScopeDownPolicy) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, MqttException {
        String mqttOverWebsocketsUri = getMqttOverWebsocketsUri(optionalRegion, optionalEndpointAddress, optionalRoleToAssume, optionalScopeDownPolicy);

        MemoryPersistence persistence = new MemoryPersistence();

        return new MqttAsyncClient(mqttOverWebsocketsUri, clientId.getClientId(), persistence);
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
    public IMqttToken connect(MqttAsyncClient mqttAsyncClient) throws MqttException {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        return mqttAsyncClient.connect(connOpts);
    }

    @Override
    public IMqttToken connect(MqttAsyncClient mqttAsyncClient, ImmutableUsernamePassword usernamePassword, Object userContext, IMqttActionListener callback) throws MqttException {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        setUsernamePassword(usernamePassword, connOpts);
        return mqttAsyncClient.connect(connOpts, userContext, callback);
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
    public String getMqttOverWebsocketsUri(Optional<Region> optionalRegion, Optional<ImmutableEndpointAddress> optionalEndpointAddress, Optional<ImmutableRoleToAssume> optionalRoleToAssume, Optional<ImmutableScopeDownPolicy> optionalScopeDownPolicy) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        long time = System.currentTimeMillis();
        DateTime dateTime = new DateTime(time);
        String dateStamp = getDateStamp(dateTime);
        String amzdate = getAmzDate(dateTime);
        String service = "iotdata";
        Region region = optionalRegion.orElseGet(this::getDefaultRegionString);
        String regionString = region.toString();
        String clientEndpoint = optionalEndpointAddress.map(EndpointAddress::getEndpointAddress).orElseGet(this::getDefaultEndpointAddress);

        AwsCredentials awsCredentials;
        String awsAccessKeyId;
        String awsSecretAccessKey;
        Optional<String> optionalSessionToken = Optional.empty();

        if (!optionalRoleToAssume.isPresent()) {
            // Use the current role
            awsCredentials = DefaultCredentialsProvider.create().resolveCredentials();
            awsAccessKeyId = awsCredentials.accessKeyId();
            awsSecretAccessKey = awsCredentials.secretAccessKey();

            if (awsCredentials instanceof AwsSessionCredentials) {
                optionalSessionToken = Optional.of(((AwsSessionCredentials) awsCredentials).sessionToken());
            }
        } else {
            // Assume a new role
            StsClient stsClient = StsClient.create();
            String roleToAssume = optionalRoleToAssume
                    .map(RoleToAssume::getRoleToAssume)
                    .orElseThrow(() -> new RuntimeException("Role to assume is missing, this is a bug"));

            String roleArn = roleToAssume;

            if (!roleArn.startsWith(ARN_AWS_IAM)) {
                // The role coming from the environment will be the full ARN, if this is just the role name add the proper prefix
                String accountId = getAccountId(stsClient);

                roleArn = ARN_AWS_IAM + accountId + ":role/" + roleToAssume;
            }

            log.debug("Attempting to assume role: " + roleArn);

            AssumeRoleRequest.Builder assumeRoleRequestBuilder = AssumeRoleRequest.builder()
                    .roleArn(roleArn)
                    .roleSessionName("aws-iot-core-websockets-java");

            // Add the scope down policy if there is one
            optionalScopeDownPolicy
                    .map(ScopeDownPolicy::getScopeDownPolicy)
                    .ifPresent(assumeRoleRequestBuilder::policy);

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

    public String getAccountId(StsClient stsClient) {
        return stsClient.getCallerIdentity(GetCallerIdentityRequest.builder().build()).account();
    }

    private String getDefaultEndpointAddress() {
        IotClient iotClient = IotClient.create();
        DescribeEndpointRequest describeEndpointRequest = DescribeEndpointRequest.builder()
                .endpointType("iot:Data-ATS")
                .build();
        return iotClient.describeEndpoint(describeEndpointRequest).endpointAddress();
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
