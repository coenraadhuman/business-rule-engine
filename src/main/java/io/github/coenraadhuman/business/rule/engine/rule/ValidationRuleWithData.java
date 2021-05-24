package io.github.coenraadhuman.business.rule.engine.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.coenraadhuman.business.rule.engine.data.DataRetriever;
import io.github.coenraadhuman.business.rule.engine.result.EngineResult;

public abstract class ValidationRuleWithData<RuleResultType, ArgumentType>
    extends ValidationRule<RuleResultType, ArgumentType> {

    private List<DataRetriever<RuleResultType, ArgumentType>> dataRetrievers = new ArrayList<>();

    private EngineResult<RuleResultType> dataRuleResult;

    @Override
    protected EngineResult<RuleResultType> executeRule(final ArgumentType info) {
        executeDataRetrievers(info);
        if (Objects.isNull(dataRuleResult) || dataRuleResult.isRuleValid()) {
            dataRuleResult = executeRuleWithData(info);
        }
        return dataRuleResult;
    }

    private void executeDataRetrievers(final ArgumentType info) {
        for (var dataRetriever : dataRetrievers) {
            dataRetriever.setEngine(engine);
            dataRuleResult = dataRetriever.retrieveData(info);
            if (dataRuleResult.isRuleImmediateInvalid() || dataRuleResult.isRuleDeferredInvalid()) {
                dataRuleResult.setSourceName(this.getClass().getSimpleName());
                break;
            }
        }
    }

    public ValidationRuleWithData<RuleResultType, ArgumentType> setDataRetrievers(
        final List<DataRetriever<RuleResultType, ArgumentType>> dataRetrievers) {
        this.dataRetrievers = dataRetrievers;
        return this;
    }

    public ValidationRuleWithData<RuleResultType, ArgumentType> addDataRetriever(
        final DataRetriever<RuleResultType, ArgumentType> dataRetriever) {
        if (!this.dataRetrievers.contains(dataRetriever)) {
            this.dataRetrievers.add(dataRetriever);
        }
        return this;
    }

    protected abstract EngineResult<RuleResultType> executeRuleWithData(final ArgumentType info);
}
