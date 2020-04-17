package com.awslabs.aws.iot.websockets.data;

public class PolicyResource {
    private final String resource;

    public PolicyResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return resource;
    }
}

