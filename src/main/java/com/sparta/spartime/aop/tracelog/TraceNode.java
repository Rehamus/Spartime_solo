package com.sparta.spartime.aop.tracelog;

import com.sparta.spartime.web.filter.transaction.TransactionIdHolder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TraceNode {
    private final String id;
    private final int level;

    public TraceNode() {
        this.id = TransactionIdHolder.getTrId();
        this.level = 0;
    }

    public TraceNode createNextTraceNode() {
        return new TraceNode(id, level + 1);
    }

    public TraceNode createPreviousTraceNode() {
        return new TraceNode(id, level - 1);
    }

    public boolean isHead() {
        return level == 0;
    }

}
