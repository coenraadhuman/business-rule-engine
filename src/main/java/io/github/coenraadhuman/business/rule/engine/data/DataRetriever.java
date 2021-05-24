package io.github.coenraadhuman.business.rule.engine.data;

import java.util.ArrayList;
import java.util.List;

import io.github.coenraadhuman.business.rule.engine.ValidationEngineCommon;
import io.github.coenraadhuman.business.rule.engine.result.EngineResult;
import io.github.coenraadhuman.business.rule.engine.result.EngineResultStatus;
import lombok.AccessLevel;
import lombok.Setter;

public abstract class DataRetriever<RuleResultType, ArgumentType> {

    protected List<DataRetriever<RuleResultType, ArgumentType>> childDataRetrievers =
        new ArrayList<>();

    @Setter(AccessLevel.PUBLIC)
    private ValidationEngineCommon<RuleResultType, ArgumentType> engine;

    private EngineResult<RuleResultType> dataRetrieverResult;

    public EngineResult<RuleResultType> retrieveData(final ArgumentType info) {
        dataRetrieverResult = executeDataRetrieval(info);
        processDataRetrieverResult();
        if (dataRetrieverResult.isRuleValid()) {
            executeChildDataRetrievers(info);
        }
        return dataRetrieverResult;
    }

    private void processDataRetrieverResult() {
        if (dataRetrieverResult.isRuleValid() || dataRetrieverResult.isRuleNotApplicable()) {
            engine.addHistoricResult(dataRetrieverResult);
        } else {
            dataRetrieverResult.setStatus(EngineResultStatus.IMMEDIATE_INVALID);
        }
    }

    private void executeChildDataRetrievers(final ArgumentType info) {
        for (var childDataRetriever : childDataRetrievers) {
            childDataRetriever.setEngine(engine);
            dataRetrieverResult = childDataRetriever.retrieveData(info);
            if (dataRetrieverResult.isRuleImmediateInvalid()
                || dataRetrieverResult.isRuleDeferredInvalid()) {
                break;
            }
        }
    }

    public DataRetriever<RuleResultType, ArgumentType> setChildDataRetrievers(
        List<DataRetriever<RuleResultType, ArgumentType>> childDataRetrievers) {
        this.childDataRetrievers = childDataRetrievers;
        return this;
    }

    public DataRetriever<RuleResultType, ArgumentType> addChildDataRetriever(
        final DataRetriever<RuleResultType, ArgumentType> childDataRetriever) {
        if (!this.childDataRetrievers.contains(childDataRetriever)) {
            this.childDataRetrievers.add(childDataRetriever);
        }
        return this;
    }

    protected abstract EngineResult<RuleResultType> executeDataRetrieval(final ArgumentType info);
}
