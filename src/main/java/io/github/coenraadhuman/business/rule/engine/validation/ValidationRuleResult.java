package io.github.coenraadhuman.business.rule.engine.validation;

import io.github.coenraadhuman.business.rule.engine.common.ResultSeverity;
import io.github.coenraadhuman.business.rule.engine.RuleResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class ValidationRuleResult extends RuleResult {

    public ValidationRuleResult setValid() {
        this.setSeverity(ResultSeverity.GREEN);
        return this;
    }

    public ValidationRuleResult setInvalid() {
        this.setSeverity(ResultSeverity.RED);
        return this;
    }

    public ValidationRuleResult setNotApplicable() {
        this.setSeverity(ResultSeverity.YELLOW);
        return this;
    }

    public ValidationRuleResult setMessage(String message) {
        this.setSeverityMessage(message);
        return this;
    }

}
