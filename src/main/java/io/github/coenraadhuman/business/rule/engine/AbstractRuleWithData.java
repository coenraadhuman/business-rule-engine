package io.github.coenraadhuman.business.rule.engine;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractRuleWithData<Argument> extends AbstractRule<Argument> {

    protected List<DataRetriever<Argument>> dataRetrievers = new ArrayList<>();

    @Getter
    Queue<RuleResult> dataRetrieverResults = new ConcurrentLinkedQueue<>();

    @Override
    public void run() {
        this.executeDataRetrievers();
        this.setRuleResult(executeRule(this.getArgument()));
        this.addEngineResult();
        if (this.getRuleResult().isGreen() || this.getRuleResult().isYellow()) {
            for (var rule : this.getRules()) {
                rule.setParentRule(this);
                rule.setArgument(this.getArgument());
            }
            this.executeRules(this.getArgument());
        }
        this.sendCompletedMessage(this.getRuleResult());
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
                } else {
                    System.out.println("Waiting for assigned data retrievers to finish.");
                }
            }
        } else {
            System.out.println("No data retrievers assigned to be executed.");
        }
    }

    private boolean shouldStop() {
        return this.dataRetrieverResults.stream().anyMatch(RuleResult::isRed);
    }
}
