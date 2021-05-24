package io.github.coenraadhuman.business.rule.engine.implementation.rules.data;

import java.util.Objects;

import io.github.coenraadhuman.business.rule.engine.implementation.common.TestArgument;
import io.github.coenraadhuman.business.rule.engine.implementation.common.TestResponse;
import io.github.coenraadhuman.business.rule.engine.result.EngineResult;
import io.github.coenraadhuman.business.rule.engine.result.EngineResultBuilder;
import io.github.coenraadhuman.business.rule.engine.result.EngineResultStatus;
import io.github.coenraadhuman.business.rule.engine.rule.ValidationRuleWithData;

public class TestValidationInfoValidChildDataRule
    extends ValidationRuleWithData<TestResponse, TestArgument> {

    @Override
    protected EngineResult<TestResponse> executeRuleWithData(final TestArgument info) {
        EngineResultStatus resultType;
        String message = null;

        if (Objects.nonNull(info.getDataRetrieverMessage())) {
            resultType =
                "VALID_DATA_RETRIEVER".equals(info.getDataRetrieverMessage())
                    ? EngineResultStatus.VALID
                    : EngineResultStatus.IMMEDIATE_INVALID;
            message = info.getDataRetrieverMessage();
        } else {
            resultType = EngineResultStatus.IMMEDIATE_INVALID;
        }

        return new EngineResultBuilder<TestResponse>()
            .status(resultType)
            .message(message)
            .ruleResult(TestResponse.TEST_VALIDATION_PROVIDER_VALID_DATA_CHILD_RULE)
            .build();
    }
}
