package io.github.coenraadhuman.business.rule.engine;

import io.github.coenraadhuman.business.rule.engine.common.LogUtility;
import io.github.coenraadhuman.business.rule.engine.data.DataRetrieverResult;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractRuleWithData<Argument> extends AbstractRule<Argument> {

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    protected List<DataRetriever<Argument>> dataRetrievers = new ArrayList<>();

    public AbstractRuleWithData<Argument> setRuleDataRetrievers(final List<DataRetriever<Argument>> dataRetrievers) {
        this.setDataRetrievers(dataRetrievers);
        return this;
    }

    public AbstractRuleWithData<Argument> addDataRetriever(final DataRetriever<Argument> dataRetriever) {
        this.getDataRetrievers().add(dataRetriever);
        return this;
    }

    @Getter
    Queue<RuleResult> dataRetrieverResults = new ConcurrentLinkedQueue<>();

    @Override
    public void run() {
        for (var dataRetriever : this.getDataRetrievers()) {
            dataRetriever.setParentRule(this);
            dataRetriever.setEngine(this.getEngine());
            dataRetriever.setArgument(this.getArgument());
        }
        this.executeDataRetrievers();
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

    protected void reportDataRetrieverResult(final DataRetrieverResult dataRetrieverResult) {
        LogUtility.printMessage("Data retriever result was reported.");
        this.dataRetrieverResults.add(dataRetrieverResult);
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
}
