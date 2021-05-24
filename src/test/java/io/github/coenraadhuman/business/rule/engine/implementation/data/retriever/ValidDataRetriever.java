package io.github.coenraadhuman.business.rule.engine.implementation.data.retriever;

import io.github.coenraadhuman.business.rule.engine.data.DataRetriever;
import io.github.coenraadhuman.business.rule.engine.implementation.common.TestArgument;
import io.github.coenraadhuman.business.rule.engine.implementation.common.TestResponse;
import io.github.coenraadhuman.business.rule.engine.result.EngineResult;
import io.github.coenraadhuman.business.rule.engine.result.EngineResultBuilder;
import io.github.coenraadhuman.business.rule.engine.result.EngineResultStatus;

public class ValidDataRetriever extends DataRetriever<TestResponse, TestArgument> {

    @Override
    protected EngineResult<TestResponse> executeDataRetrieval(final TestArgument info) {
        info.setDataRetrieverMessage("VALID_DATA_RETRIEVER");
        return new EngineResultBuilder<TestResponse>()
            .status(EngineResultStatus.VALID)
            .message("VALID_DATA_RETRIEVER")
            .ruleResult(TestResponse.VALID_DATA_RETRIEVER)
            .build();
    }
}
