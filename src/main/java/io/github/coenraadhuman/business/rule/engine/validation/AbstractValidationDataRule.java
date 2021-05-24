package io.github.coenraadhuman.business.rule.engine.validation;

import io.github.coenraadhuman.business.rule.engine.AbstractRuleWithData;
import io.github.coenraadhuman.business.rule.engine.RuleResult;

public abstract class AbstractValidationDataRule<Argument> extends AbstractRuleWithData<Argument> {

    public abstract ValidationRuleResult validate(Argument argument);

    @Override
    protected RuleResult executeRule(Argument argument) {
        return this.validate(argument);
    }

}
