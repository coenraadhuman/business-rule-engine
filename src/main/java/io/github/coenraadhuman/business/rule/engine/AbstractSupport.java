package io.github.coenraadhuman.business.rule.engine;

import io.github.coenraadhuman.business.rule.engine.common.LogUtility;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractSupport<Argument> {

    @Getter
    Queue<RuleResult> ruleResults = new ConcurrentLinkedQueue<>();

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private Argument argument;

    @Setter(AccessLevel.PROTECTED)
    @Getter(AccessLevel.PROTECTED)
    protected AbstractBusinessRuleEngine<Argument> engine;

    @Setter(AccessLevel.PROTECTED)
    protected AbstractRule<Argument> parentRule;

    @Setter(AccessLevel.PROTECTED)
    @Getter(AccessLevel.PROTECTED)
    private List<AbstractRule<Argument>> rules = new ArrayList<>();

    protected AtomicReference<RuleResult> ruleResult;

    public RuleResult getRuleResult() {
        if (Objects.nonNull(ruleResult)) {
            return ruleResult.get();
        }
        return null;
    }

    public void setRuleResult(final RuleResult ruleResult) {
        this.ruleResult = new AtomicReference<>(ruleResult);
    }

    protected void reportChildRuleResult(final RuleResult ruleResult) {
        LogUtility.printMessage("Child rule result was reported.");
        this.ruleResults.add(ruleResult);
    }

    protected void executeRules(final Argument argument) {
        if (this.rules.size() > 0) {
            LogUtility.printMessage(this.rules.size() + " rules assigned to be executed.");

            if (Objects.nonNull(engine)) {
                var executorService = this.engine.getExecutorService();

                for (var rule : this.rules) {
                    if (rule.shouldExecute(argument)) {
                        executorService.execute(rule);
                    }
                }

                while (rules.size() != ruleResults.size()) {
                    if (this.shouldStop()) {
                        this.engine.panic();
                    }
                }
            } else {
                throw new RuntimeException(Thread.currentThread().getName() + ": Engine reference is not set.");
            }
        } else {
            LogUtility.printMessage("No rules assigned to be executed.");
        }
    }

    private boolean shouldStop() {
        return this.rules.stream()
                .filter(rule -> Objects.nonNull(rule.getRuleResult()))
                .anyMatch(rule -> rule.getRuleResult().isRed());
    }

    protected void sendCompletedMessage(final RuleResult ruleResult) {
        if (Objects.nonNull(parentRule)) {
            parentRule.reportChildRuleResult(ruleResult);
        } else {
            if (Objects.nonNull(engine)) {
                engine.reportChildRuleResult(ruleResult);
            } else {
                throw new RuntimeException(Thread.currentThread().getName() + ": Result could not be reported.");
            }
        }
    }

}
