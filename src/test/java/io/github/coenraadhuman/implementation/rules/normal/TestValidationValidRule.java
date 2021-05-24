package io.github.coenraadhuman.implementation.rules.normal;

import io.github.coenraadhuman.business.rule.engine.validation.AbstractValidationRule;
import io.github.coenraadhuman.business.rule.engine.validation.ValidationRuleResult;
import io.github.coenraadhuman.implementation.common.TestArgument;
import lombok.SneakyThrows;

public final class TestValidationValidRule extends AbstractValidationRule<TestArgument> {

    @Override
    protected boolean shouldExecute(TestArgument testArgument) {
        return true;
    }

    @Override
    @SneakyThrows
    public ValidationRuleResult validate(TestArgument testArgument) {
        Thread.sleep(1000);
        return new ValidationRuleResult()
                .setValid()
                .setMessage("Executed validation valid rule.");
    }
}
