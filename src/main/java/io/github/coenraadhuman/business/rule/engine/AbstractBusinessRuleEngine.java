package io.github.coenraadhuman.business.rule.engine;

import io.github.coenraadhuman.business.rule.engine.common.ResultSeverity;
import io.github.coenraadhuman.business.rule.engine.result.EngineResultStatus;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractBusinessRuleEngine<Argument> extends AbstractSupport<Argument> {

    @Getter
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Getter
    Queue<RuleResult> parentRuleResults = new ConcurrentLinkedQueue<>();

    @Getter
    private final Queue<RuleResult> engineHistory = new ConcurrentLinkedQueue<>();

    public EngineResult<Argument> run(final Argument argument) {
        this.setRules(this.setEngineRules());
        this.setEngine(this);
        for (var rule : this.getRules()) {
            rule.setEngine(this);
            rule.setArgument(argument);
        }
        this.executeRules(argument);
        this.executorService.shutdown();
        return this.createEngineResult();
    }

    private EngineResult<Argument> createEngineResult() {
        var result = new EngineResult<Argument>();

        result.setExecutionResults(new ArrayList<>(engineHistory));
        result.setProvidedArgument(this.getArgument());

        if (result.getExecutionResults().isEmpty()) {
            result.setStatus(EngineResultStatus.getEngineResultFromSeverity(ResultSeverity.YELLOW));
        } else {
            if (result.hasAnyInvalidResults()) {
                result.setStatus(EngineResultStatus.getEngineResultFromSeverity(ResultSeverity.RED));
            } else {
                result.setStatus(EngineResultStatus.getEngineResultFromSeverity(ResultSeverity.GREEN));
            }
        }

        return result;
    }

    protected void panic() {
        this.executorService.shutdown();
    }

    protected void reportHistory(final RuleResult ruleResult) {
        this.engineHistory.add(ruleResult);
    }

    protected abstract List<AbstractRule<Argument>> setEngineRules();

}
