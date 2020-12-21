package com.awslabs.aws.iot.websockets.data;

public class NoToString {
    @Override
    public final String toString() {
        // This is to make sure string concatenation with this type throws an exception immediately
        throw new RuntimeException("This object does not support toString()");
    }
}