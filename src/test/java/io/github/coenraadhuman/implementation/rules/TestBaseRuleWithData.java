package io.github.coenraadhuman.implementation.rules;

import io.github.coenraadhuman.business.rule.engine.validation.AbstractValidationDataRule;
import io.github.coenraadhuman.business.rule.engine.validation.ValidationRuleResult;
import io.github.coenraadhuman.implementation.common.TestArgument;
import lombok.SneakyThrows;

public final class TestBaseRuleWithData extends AbstractValidationDataRule<TestArgument> {

    @Override
    protected boolean shouldExecute(final TestArgument testArgument) {
        return true;
    }

    @SneakyThrows
    @Override
    public ValidationRuleResult validate(final TestArgument testArgument) {
        Thread.sleep(1000);
        return new ValidationRuleResult()
            .setValid()
            .setMessage("Executed validation valid data rule.");
    }
}
