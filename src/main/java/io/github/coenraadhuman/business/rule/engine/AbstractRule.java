package io.github.coenraadhuman.business.rule.engine;

import java.util.List;
import java.util.Objects;

public abstract class AbstractRule<Argument> extends AbstractSupport<Argument> implements Runnable {

    protected abstract boolean shouldExecute(Argument argument);

    protected void addEngineResult() {
        if (Objects.nonNull(this.getEngine())) {
            engine.reportHistory(this.getRuleResult());
        }
    }

    public AbstractRule<Argument> setChildRules(List<AbstractRule<Argument>> rules) {
        this.setRules(rules);
        return this;
    }

    public AbstractRule<Argument> addChildRule(final AbstractRule<Argument> childRule) {
        this.getRules().add(childRule);
        return this;
    }

    protected abstract RuleResult executeRule(final Argument argument);

    @Override
    public void run() {
        this.setRuleResult(executeRule(this.getArgument()));
        this.addEngineResult();
        if (this.getRuleResult().isGreen() || this.getRuleResult().isYellow()) {
            for (var rule : this.getRules()) {
                rule.setParentRule(this);
                rule.setEngine(this.getEngine());
                rule.setArgument(this.getArgument());
            }
            this.executeRules(this.getArgument());
        }
        this.sendCompletedMessage(this.getRuleResult());
    }
}
