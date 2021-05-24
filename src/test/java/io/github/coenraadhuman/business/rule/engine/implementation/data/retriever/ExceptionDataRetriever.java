package io.github.coenraadhuman.business.rule.engine.implementation.data.retriever;

import io.github.coenraadhuman.business.rule.engine.data.DataRetriever;
import io.github.coenraadhuman.business.rule.engine.implementation.common.TestArgument;
import io.github.coenraadhuman.business.rule.engine.implementation.common.TestResponse;
import io.github.coenraadhuman.business.rule.engine.result.EngineResult;

public class ExceptionDataRetriever extends DataRetriever<TestResponse, TestArgument> {

    @Override
    protected EngineResult<TestResponse> executeDataRetrieval(final TestArgument info) {
        throw new RuntimeException("Some interesting exception.");
    }
}
