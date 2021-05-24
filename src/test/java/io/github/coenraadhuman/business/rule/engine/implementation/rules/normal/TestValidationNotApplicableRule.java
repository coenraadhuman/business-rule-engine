package io.github.coenraadhuman.business.rule.engine.implementation.rules.normal;

import io.github.coenraadhuman.business.rule.engine.implementation.common.TestArgument;
import io.github.coenraadhuman.business.rule.engine.implementation.common.TestResponse;
import io.github.coenraadhuman.business.rule.engine.result.EngineResult;
import io.github.coenraadhuman.business.rule.engine.result.EngineResultBuilder;
import io.github.coenraadhuman.business.rule.engine.result.EngineResultStatus;
import io.github.coenraadhuman.business.rule.engine.rule.ValidationRule;

public class TestValidationNotApplicableRule extends ValidationRule<TestResponse, TestArgument> {

    @Override
    protected EngineResult<TestResponse> executeRule(final TestArgument argument) {
        return new EngineResultBuilder<TestResponse>()
            .status(EngineResultStatus.NOT_APPLICABLE)
            .ruleResult(TestResponse.TEST_VALIDATION_NOT_APPLICABLE_RULE)
            .build();
    }
}
