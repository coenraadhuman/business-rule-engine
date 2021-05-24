package io.github.coenraadhuman.business.rule.engine.implementation.rules.normal;

import io.github.coenraadhuman.business.rule.engine.implementation.common.TestArgument;
import io.github.coenraadhuman.business.rule.engine.implementation.common.TestResponse;
import io.github.coenraadhuman.business.rule.engine.result.EngineResult;
import io.github.coenraadhuman.business.rule.engine.rule.ValidationRule;

public class TestValidationExceptionRule extends ValidationRule<TestResponse, TestArgument> {

    @Override
    protected EngineResult<TestResponse> executeRule(final TestArgument argument) {
        throw new RuntimeException("Some interesting exception.");
    }
}
