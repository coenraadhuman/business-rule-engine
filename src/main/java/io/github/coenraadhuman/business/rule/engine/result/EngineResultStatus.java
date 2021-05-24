package io.github.coenraadhuman.business.rule.engine.result;

import io.github.coenraadhuman.business.rule.engine.common.ResultSeverity;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum EngineResultStatus {
    SUCCESSFUL(ResultSeverity.GREEN),
    FAILED(ResultSeverity.RED),
    NO_EXECUTIONS(ResultSeverity.YELLOW);

    private final ResultSeverity resultSeverity;

    public static EngineResultStatus getEngineResultFromSeverity(ResultSeverity resultSeverity) {
        return Arrays.stream(EngineResultStatus.values())
                .filter(item -> item.resultSeverity.equals(resultSeverity))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find applicable severity."));
    }
}
