package io.github.coenraadhuman.business.rule.engine;

import java.util.ArrayList;
import java.util.List;

import io.github.coenraadhuman.business.rule.engine.result.EngineResult;
import io.github.coenraadhuman.business.rule.engine.result.EngineResultStatus;
import io.github.coenraadhuman.business.rule.engine.rule.ValidationRule;
import lombok.AccessLevel;
import lombok.Setter;

public abstract class ValidationEngineCommon<RuleResultType, ArgumentType>
    implements ValidationEngine<RuleResultType, ArgumentType> {

    private static final String DEFERRED_INVALIDATIONS_OCCURRED_SEE_HISTORY =
        "Deferred invalidations occurred, see history.";

    private final List<EngineResult<RuleResultType>> engineHistoryResults = new ArrayList<>();

    private final List<EngineResult<RuleResultType>> engineDeferredInvalidResults = new ArrayList<>();

    @Setter(AccessLevel.PROTECTED)
    private List<ValidationRule<RuleResultType, ArgumentType>> rules;

    protected EngineResult<RuleResultType> executeEngine(final ArgumentType info) {
        var result = new EngineResult<RuleResultType>();
        for (var rule : rules) {
            rule.setEngine(this);
            result = rule.validate(info);
            if (result.isRuleImmediateInvalid()) {
                break;
            }
        }
        return addHistory(result);
    }

    public EngineResult<RuleResultType> validate(final ArgumentType info) {
        resetResults();
        setRules();
        return executeEngine(info);
    }

    public void addHistoricResult(EngineResult<RuleResultType> engineResult) {
        engineHistoryResults.add(engineResult);
    }

    public void addDeferredInvalidResult(EngineResult<RuleResultType> engineResult) {
        engineDeferredInvalidResults.add(engineResult);
    }

    private EngineResult<RuleResultType> addHistory(EngineResult<RuleResultType> lastResult) {
        var finalEngineResult = new EngineResult<RuleResultType>();

        if (engineDeferredInvalidResults.size() > 0) {
            finalEngineResult.setStatus(EngineResultStatus.DEFERRED_INVALID);
            finalEngineResult.setMessage(DEFERRED_INVALIDATIONS_OCCURRED_SEE_HISTORY);
        } else {
            finalEngineResult.setStatus(lastResult.getStatus());
            finalEngineResult.setRuleResult(lastResult.getRuleResult());
            finalEngineResult.setMessage(lastResult.getMessage());
        }

        finalEngineResult.setEngineResults(new ArrayList<>(engineHistoryResults));
        finalEngineResult.setEngineSoftInvalidResults(new ArrayList<>(engineDeferredInvalidResults));
        finalEngineResult.setSourceName(this.getClass().getSimpleName());

        return finalEngineResult;
    }

    public abstract void setRules();

    private void resetResults() {
        engineHistoryResults.clear();
        engineDeferredInvalidResults.clear();
    }
}
