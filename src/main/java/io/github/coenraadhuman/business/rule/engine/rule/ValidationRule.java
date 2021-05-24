package io.github.coenraadhuman.business.rule.engine.rule;

import java.util.ArrayList;
import java.util.List;

import io.github.coenraadhuman.business.rule.engine.ValidationEngineCommon;
import io.github.coenraadhuman.business.rule.engine.result.EngineResult;
import lombok.AccessLevel;
import lombok.Setter;

public abstract class ValidationRule<RuleResultType, ArgumentType> {

    @Setter(AccessLevel.PUBLIC)
    protected ValidationEngineCommon<RuleResultType, ArgumentType> engine;

    private List<ValidationRule<RuleResultType, ArgumentType>> childRules = new ArrayList<>();

    private EngineResult<RuleResultType> ruleResult;

    public EngineResult<RuleResultType> validate(final ArgumentType info) {
        ruleResult = executeRule(info);
        addEngineResult();
        if (ruleResult.isRuleValid() || ruleResult.isRuleDeferredInvalid()) {
            ruleResult = executeChildRules(info);
        }
        return ruleResult;
    }

    private void addEngineResult() {
        if (ruleResult.isRuleDeferredInvalid()) {
            ruleResult.setSourceName(this.getClass().getSimpleName());
            engine.addDeferredInvalidResult(ruleResult);
        }
        engine.addHistoricResult(ruleResult);
    }

    private EngineResult<RuleResultType> executeChildRules(final ArgumentType info) {
        EngineResult<RuleResultType> childRuleResult = ruleResult;
        for (var rule : childRules) {
            rule.setEngine(engine);
            childRuleResult = rule.validate(info);
            if (childRuleResult.isRuleImmediateInvalid()) {
                childRuleResult.setSourceName(rule.getClass().getSimpleName());
                break;
            }
        }
        return childRuleResult;
    }

    public ValidationRule<RuleResultType, ArgumentType> setChildRules(
        final List<ValidationRule<RuleResultType, ArgumentType>> childRules) {
        this.childRules = childRules;
        return this;
    }

    public ValidationRule<RuleResultType, ArgumentType> addChildRule(
        final ValidationRule<RuleResultType, ArgumentType> childRule) {
        if (!this.childRules.contains(childRule)) {
            this.childRules.add(childRule);
        }
        return this;
    }

    protected abstract EngineResult<RuleResultType> executeRule(final ArgumentType info);
}
