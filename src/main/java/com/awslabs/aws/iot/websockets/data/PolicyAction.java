package com.awslabs.aws.iot.websockets.data;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

public class PolicyAction {
    private final String action;

    public PolicyAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return action;
    }
}
