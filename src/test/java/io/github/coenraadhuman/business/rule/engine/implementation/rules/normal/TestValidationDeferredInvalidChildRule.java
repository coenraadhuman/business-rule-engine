package io.github.coenraadhuman.business.rule.engine.implementation.rules.normal;

import io.github.coenraadhuman.business.rule.engine.implementation.common.TestArgument;
import io.github.coenraadhuman.business.rule.engine.implementation.common.TestResponse;
import io.github.coenraadhuman.business.rule.engine.result.EngineResult;
import io.github.coenraadhuman.business.rule.engine.result.EngineResultBuilder;
import io.github.coenraadhuman.business.rule.engine.result.EngineResultStatus;
import io.github.coenraadhuman.business.rule.engine.rule.ValidationRule;

public class TestValidationDeferredInvalidChildRule
    extends ValidationRule<TestResponse, TestArgument> {

    @Override
    protected EngineResult<TestResponse> executeRule(final TestArgument argument) {
        invalidChildRuleExecuted();

        return new EngineResultBuilder<TestResponse>()
            .status(EngineResultStatus.DEFERRED_INVALID)
            .ruleResult(TestResponse.TEST_VALIDATION_DEFERRED_INVALID_CHILD_RULE)
            .build();
    }

    public void invalidChildRuleExecuted() {
    }
}
