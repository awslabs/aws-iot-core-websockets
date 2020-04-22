## AWS IoT Core Websockets

[![Build Status](https://travis-ci.org/awslabs/aws-iot-core-websockets.svg?branch=master)](https://travis-ci.org/awslabs/aws-iot-core-websockets)
[![Open Issues](https://img.shields.io/github/issues-raw/awslabs/aws-iot-core-websockets.svg)](https://github.com/awslabs/aws-iot-core-websockets/issues)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/awslabs/aws-iot-core-websockets/blob/master/LICENSE)

A library that handles connecting third-party websockets based MQTT clients to AWS IoT Core.

## Why MQTT over WebSockets?

MQTT over WebSockets allows you to use SigV4 credentials. This means that you can use this library to connect to AWS IoT Core,
without certificates, assuming that you have a way to get SigV4 credentials. For normal IAM users this can be from
`.aws/config`'s access key and secret key ID, or it could be from the EC2 instance meta-data service so you can use an
instance profile, or it could be from the [AWS Secure Token Service](https://docs.aws.amazon.com/STS/latest/APIReference/Welcome.html)
so you can ship temporary credentials to other systems over any secure delivery mechanism that you have set up already.

## Can I use SigV4 credentials with normal MQTT?

[Not as of 2020-03-26 according to the AWS IoT documentation](https://docs.aws.amazon.com/iot/latest/developerguide/protocols.html).

## How do I include it in my Gradle project?

1. Add the jitpack repo to the repositories section

    ```
    maven { url 'https://jitpack.io' }
    ```

2. Add the dependency version [(replace x.y.z with the appropriate version from the JitPack site)](https://jitpack.io/#awslabs/aws-iot-core-websockets)

    ```
    def awsIotCoreWebsocketsVersion = 'x.y.z'
    ```

3. Add the dependency to the dependencies section

    ```
    compile "com.github.awslabs:aws-iot-core-websockets:$awsIotCoreWebsocketsVersion"
    ```

## How do I use it?

Check out an [example in the IoT reference architectures repo](https://github.com/aws-samples/iot-reference-architectures/tree/master/mqtt-over-websockets-jitpack/java).

## Is there a really simple example snippet to get me started?

Of course, all you have to do after including the library to get an MQTT client with your IAM credentials is this:

```
        mqttOverWebsocketsProvider = new BasicMqttOverWebsocketsProvider();
        String uuid = UUID.randomUUID().toString();
        clientId = ImmutableClientId.builder().clientId(uuid).build();
        mqttClient = mqttOverWebsocketsProvider.getMqttClient(clientId);
```

## License

This library is licensed under the Apache 2.0 License. 
