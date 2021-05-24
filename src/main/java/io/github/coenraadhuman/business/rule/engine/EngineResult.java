package io.github.coenraadhuman.business.rule.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.github.coenraadhuman.business.rule.engine.result.EngineResultStatus;
import lombok.Data;

@Data
public class EngineResult<Argument> {

    private Argument providedArgument;
    private EngineResultStatus status;
    private List<RuleResult> executionResults = new ArrayList<>();

    public boolean hasAnyInvalidResults() {
        return executionResults
                .stream()
                .anyMatch(RuleResult::isRed);
    }

    public List<RuleResult> getInvalidEngineResults() {
        return executionResults.stream()
            .filter(RuleResult::isRed)
            .collect(Collectors.toList());
    }

}
