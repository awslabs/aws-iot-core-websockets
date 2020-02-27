## AWS IoT Core Websockets

[![Build Status](https://travis-ci.org/awslabs/aws-iot-core-websockets.svg?branch=master)](https://travis-ci.org/awslabs/aws-iot-core-websockets)
[![Open Issues](https://img.shields.io/github/issues-raw/awslabs/aws-iot-core-websockets.svg)](https://github.com/awslabs/aws-iot-core-websockets/issues)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/awslabs/aws-iot-core-websockets/blob/master/LICENSE)

A library that handles connecting third-party websockets based MQTT clients to AWS IoT Core.

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

## License

This library is licensed under the Apache 2.0 License. 
