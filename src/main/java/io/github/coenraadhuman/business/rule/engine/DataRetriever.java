package io.github.coenraadhuman.business.rule.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.github.coenraadhuman.business.rule.engine.common.LogUtility;
import io.github.coenraadhuman.business.rule.engine.data.DataRetrieverResult;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class DataRetriever<Argument> implements Runnable {

    @Setter(AccessLevel.PUBLIC)
    protected List<DataRetriever<Argument>> dataRetrievers = new ArrayList<>();

    @Setter(AccessLevel.PROTECTED)
    private AbstractBusinessRuleEngine<Argument> engine;

    @Setter(AccessLevel.PROTECTED)
    private AbstractRuleWithData<Argument> parentRule;

    @Setter(AccessLevel.PROTECTED)
    private DataRetriever<Argument> parentDataRetriever;

    @Getter
    Queue<RuleResult> dataRetrieverResults = new ConcurrentLinkedQueue<>();

    private DataRetrieverResult dataRetrieverResult;

    @Setter(AccessLevel.PROTECTED)
    private Argument argument;

    protected void reportDataRetrieverResult(final RuleResult ruleResult) {
        this.dataRetrieverResults.add(ruleResult);
    }

    protected void executeDataRetrievers() {
        if (this.dataRetrievers.size() > 0) {
            var executorService = this.engine.getExecutorService();

            for (var dataRetriever : this.dataRetrievers) {
                executorService.execute(dataRetriever);
            }

            while (dataRetrievers.size() != dataRetrieverResults.size()) {
                if (this.shouldStop()) {
                    this.engine.panic();
                }
            }
        } else {
            LogUtility.printMessage("No data retrievers assigned to be executed.");
        }
    }

    private boolean shouldStop() {
        return this.dataRetrieverResults.stream().anyMatch(RuleResult::isRed);
    }

    protected void sendCompletedMessage(final DataRetrieverResult ruleResult) {
        if (Objects.nonNull(parentDataRetriever)) {
            parentDataRetriever.reportDataRetrieverResult(ruleResult);
        } else {
            if (Objects.nonNull(parentRule)) {
                parentRule.reportDataRetrieverResult(ruleResult);
            } else {
                throw new RuntimeException("Result could not be reported.");
            }
        }
    }

    public DataRetriever<Argument> addChildRule(final DataRetriever<Argument> childDataRetriever) {
        this.dataRetrievers.add(childDataRetriever);
        return this;
    }

    protected void addEngineResult() {
        if (Objects.nonNull(this.engine)) {
            engine.reportHistory(this.dataRetrieverResult);
        }
    }

    protected abstract DataRetrieverResult executeDataRetrieval(final Argument Argument);

    @Override
    public void run() {
        this.dataRetrieverResult = executeDataRetrieval(argument);
        this.addEngineResult();
        if (this.dataRetrieverResult.isGreen() || this.dataRetrieverResult.isYellow()) {
            for (var dataRetriever : this.dataRetrievers) {
                dataRetriever.setParentDataRetriever(this);
                dataRetriever.setEngine(this.engine);
                dataRetriever.setArgument(argument);
            }
            this.executeDataRetrievers();
        }
        this.sendCompletedMessage(this.dataRetrieverResult);
    }
}
