package io.github.coenraadhuman.business.rule.engine.result;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class EngineResult<RuleResultType> {

    private RuleResultType ruleResult;
    private String sourceName;
    private String message;
    private EngineResultStatus status;
    private List<EngineResult<RuleResultType>> engineResults = new ArrayList<>();
    private List<EngineResult<RuleResultType>> engineSoftInvalidResults = new ArrayList<>();

    public boolean isRuleImmediateInvalid() {
        return EngineResultStatus.IMMEDIATE_INVALID.equals(status);
    }

    public boolean isRuleNotApplicable() {
        return EngineResultStatus.NOT_APPLICABLE.equals(status);
    }

    public boolean isRuleDeferredInvalid() {
        return EngineResultStatus.DEFERRED_INVALID.equals(status);
    }

    public boolean isRuleValid() {
        return EngineResultStatus.VALID.equals(status);
    }

    public boolean hasAnyInvalidResults() {
        return engineResults.stream()
            .anyMatch(ruleResult -> ruleResult.isRuleDeferredInvalid() || isRuleImmediateInvalid());
    }

    public List<EngineResult<RuleResultType>> getInvalidEngineResults() {
        return engineResults.stream()
            .filter(
                ruleResult -> ruleResult.isRuleDeferredInvalid() || ruleResult.isRuleImmediateInvalid())
            .collect(Collectors.toList());
    }
}
