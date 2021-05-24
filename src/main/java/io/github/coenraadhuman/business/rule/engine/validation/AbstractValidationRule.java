package io.github.coenraadhuman.business.rule.engine.validation;

import io.github.coenraadhuman.business.rule.engine.AbstractRule;
import io.github.coenraadhuman.business.rule.engine.RuleResult;

public abstract class AbstractValidationRule<Argument> extends AbstractRule<Argument> {

    public abstract ValidationRuleResult validate(Argument argument);

    @Override
    protected RuleResult executeRule(Argument argument) {
        return this.validate(argument);
    }
}
