package io.github.coenraadhuman.business.rule.engine.implementation.data.retriever;

import io.github.coenraadhuman.business.rule.engine.data.DataRetriever;
import io.github.coenraadhuman.business.rule.engine.implementation.common.TestArgument;
import io.github.coenraadhuman.business.rule.engine.implementation.common.TestResponse;
import io.github.coenraadhuman.business.rule.engine.result.EngineResult;
import io.github.coenraadhuman.business.rule.engine.result.EngineResultBuilder;
import io.github.coenraadhuman.business.rule.engine.result.EngineResultStatus;

public class ImmediateInvalidDataRetriever extends DataRetriever<TestResponse, TestArgument> {

    @Override
    protected EngineResult<TestResponse> executeDataRetrieval(final TestArgument info) {
        return new EngineResultBuilder<TestResponse>()
            .status(EngineResultStatus.IMMEDIATE_INVALID)
            .message("IMMEDIATE_INVALID_DATA_RETRIEVER")
            .ruleResult(TestResponse.IMMEDIATE_INVALID_DATA_RETRIEVER)
            .build();
    }
}
