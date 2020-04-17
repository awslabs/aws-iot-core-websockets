package com.awslabs.aws.iot.websockets.data;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScopeDownPolicy {
    private final String version;
    private final List<ScopeDownPolicyStatement> scopeDownPolicyStatements;

    public ScopeDownPolicy(String version, List<ScopeDownPolicyStatement> scopeDownPolicyStatements) {
        this.version = version;
        this.scopeDownPolicyStatements = scopeDownPolicyStatements;
    }

    public ScopeDownPolicy(List<ScopeDownPolicyStatement> scopeDownPolicyStatements) {
        this.version = getDefaultVersion();
        this.scopeDownPolicyStatements = scopeDownPolicyStatements;
    }

    public String getDefaultVersion() {
        return "2012-10-17";
    }

    public String getVersion() {
        return version;
    }

    public List<ScopeDownPolicyStatement> getScopeDownPolicyStatements() {
        return scopeDownPolicyStatements;
    }

    @Override
    public String toString() {
        Map<String, Object> map = new HashMap<>();
        List<Map> statement = scopeDownPolicyStatements.stream().map(ScopeDownPolicyStatement::toMap).collect(Collectors.toList());

        map.put("Version", version);
        map.put("Statement", statement);

        return new Gson().toJson(map);
    }
}
