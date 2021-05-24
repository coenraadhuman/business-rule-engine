package io.github.coenraadhuman.business.rule.engine.implementation.rules.normal;

import io.github.coenraadhuman.business.rule.engine.implementation.common.TestArgument;
import io.github.coenraadhuman.business.rule.engine.implementation.common.TestResponse;
import io.github.coenraadhuman.business.rule.engine.result.EngineResult;
import io.github.coenraadhuman.business.rule.engine.rule.ValidationRuleWithData;

public class TestValidationExceptionDataRule
    extends ValidationRuleWithData<TestResponse, TestArgument> {

    @Override
    protected EngineResult<TestResponse> executeRuleWithData(final TestArgument info) {
        throw new RuntimeException("Some interesting exception.");
    }
}
