package com.awslabs.aws.iot.websockets.data;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScopeDownPolicyStatement {
    private final PolicyEffect effect;

    private final List<PolicyAction> action;

    private final List<PolicyResource> resource;

    public ScopeDownPolicyStatement(PolicyEffect effect, List<PolicyAction> action, List<PolicyResource> resource) {
        this.effect = effect;
        this.action = action;
        this.resource = resource;
    }

    public PolicyEffect getEffect() {
        return effect;
    }

    public List<PolicyAction> getAction() {
        return action;
    }

    public List<PolicyResource> getResource() {
        return resource;
    }

    public Map toMap() {
        Map<String, Object> map = new HashMap<>();
        List<String> actions = action.stream().map(PolicyAction::toString).collect(Collectors.toList());
        List<String> resources = resource.stream().map(PolicyResource::toString).collect(Collectors.toList());

        map.put("Effect", effect.toString());
        map.put("Action", actions);
        map.put("Resource", resources);

        return map;
    }
}
