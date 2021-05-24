package io.github.coenraadhuman.business.rule.engine;

import io.github.coenraadhuman.business.rule.engine.common.ResultSeverity;

import java.util.Objects;

public abstract class RuleResult {

    private ResultSeverity resultSeverity = ResultSeverity.GREEN;

    public String getSeverityMessage() {
        return this.resultSeverity.getMessage();
    }

    protected void setSeverityMessage(String message) {
        this.resultSeverity.setMessage(message);
    }

    protected ResultSeverity getSeverity() {
        return this.resultSeverity;
    }

    protected boolean isGreen() {
        return Objects.nonNull(resultSeverity) && ResultSeverity.GREEN.equals(resultSeverity);
    }

    protected boolean isRed() {
        return Objects.nonNull(resultSeverity) && ResultSeverity.RED.equals(resultSeverity);
    }

    protected boolean isYellow() {
        return Objects.nonNull(resultSeverity) && ResultSeverity.YELLOW.equals(resultSeverity);
    }

    protected void setSeverity(ResultSeverity resultSeverity) {
        this.resultSeverity = resultSeverity;
        this.resultSeverity.setMessage(getSeverityMessage());
    }

}
