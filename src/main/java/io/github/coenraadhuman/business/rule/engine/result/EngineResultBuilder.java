package io.github.coenraadhuman.business.rule.engine.result;

public class EngineResultBuilder<RuleResultType> {

    private final EngineResult<RuleResultType> engineResult = new EngineResult<>();

    public EngineResultBuilder<RuleResultType> sourceName(String sourceName) {
        engineResult.setSourceName(sourceName);
        return this;
    }

    public EngineResultBuilder<RuleResultType> message(String message) {
        engineResult.setMessage(message);
        return this;
    }

    public EngineResultBuilder<RuleResultType> status(EngineResultStatus status) {
        engineResult.setStatus(status);
        return this;
    }

    public EngineResultBuilder<RuleResultType> ruleResult(RuleResultType ruleResult) {
        engineResult.setRuleResult(ruleResult);
        return this;
    }

    public EngineResult<RuleResultType> build() {
        return engineResult;
    }
}
