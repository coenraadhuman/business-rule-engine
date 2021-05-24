package io.github.coenraadhuman.implementation.data;

import io.github.coenraadhuman.business.rule.engine.DataRetriever;
import io.github.coenraadhuman.business.rule.engine.data.DataRetrieverResult;
import io.github.coenraadhuman.implementation.common.TestArgument;
import lombok.SneakyThrows;

public class SuccessfulDataRetriever extends DataRetriever<TestArgument> {

    @SneakyThrows
    @Override
    protected DataRetrieverResult executeDataRetrieval(final TestArgument Argument) {
        Thread.sleep(3000);
        return new DataRetrieverResult()
            .setRetrieved()
            .setMessage("Executed valid data retriever");
    }

}
