package com.sparta.spartime.aop.tracelog;

public record TraceStatus(TraceNode traceNode, Long startTimeMs, String message) {
}
