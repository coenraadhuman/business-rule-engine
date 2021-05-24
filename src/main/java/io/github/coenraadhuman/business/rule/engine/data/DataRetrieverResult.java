package io.github.coenraadhuman.business.rule.engine.data;

import io.github.coenraadhuman.business.rule.engine.RuleResult;
import io.github.coenraadhuman.business.rule.engine.common.ResultSeverity;

public final class DataRetrieverResult extends RuleResult {

    public DataRetrieverResult setRetrieved() {
        this.setSeverity(ResultSeverity.GREEN);
        return this;
    }

    public DataRetrieverResult setFailed() {
        this.setSeverity(ResultSeverity.RED);
        return this;
    }

    public DataRetrieverResult setNotApplicable() {
        this.setSeverity(ResultSeverity.YELLOW);
        return this;
    }

    public DataRetrieverResult setMessage(String message) {
        this.setSeverityMessage(message);
        return this;
    }

}
