package io.github.coenraadhuman;

import io.github.coenraadhuman.business.rule.engine.AbstractBusinessRuleEngine;
import io.github.coenraadhuman.business.rule.engine.AbstractRule;
import io.github.coenraadhuman.business.rule.engine.result.EngineResultStatus;
import io.github.coenraadhuman.implementation.common.TestArgument;
import io.github.coenraadhuman.implementation.data.SuccessfulDataRetriever;
import io.github.coenraadhuman.implementation.rules.TestBaseRuleWithData;
import io.github.coenraadhuman.implementation.rules.normal.TestValidationValidRule;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ValidDataRuleWitDataRetrieversTest extends AbstractBusinessRuleEngine<TestArgument> {

    @Override
    protected List<AbstractRule<TestArgument>> setEngineRules() {
        return List.of(
                new TestBaseRuleWithData()
                    .addChildRule(
                        new TestBaseRuleWithData()
                            .setRuleDataRetrievers(
                                List.of(
                                    new SuccessfulDataRetriever(),
                                    new SuccessfulDataRetriever(),
                                    new SuccessfulDataRetriever(),
                                    new SuccessfulDataRetriever(),
                                    new SuccessfulDataRetriever()
                                )
                            )
                    )
                    .addChildRule(
                        new TestBaseRuleWithData()
                            .addDataRetriever(new SuccessfulDataRetriever())
                            .addDataRetriever(new SuccessfulDataRetriever())
                            .addDataRetriever(new SuccessfulDataRetriever())
                            .addDataRetriever(new SuccessfulDataRetriever())
                            .addDataRetriever(new SuccessfulDataRetriever())
                    ),
            new TestBaseRuleWithData()
                .addChildRule(
                    new TestBaseRuleWithData()
                        .setRuleDataRetrievers(
                            List.of(
                                new SuccessfulDataRetriever(),
                                new SuccessfulDataRetriever(),
                                new SuccessfulDataRetriever(),
                                new SuccessfulDataRetriever(),
                                new SuccessfulDataRetriever()
                            )
                        )
                )
                .addChildRule(
                    new TestBaseRuleWithData()
                        .addDataRetriever(new SuccessfulDataRetriever())
                        .addDataRetriever(new SuccessfulDataRetriever())
                        .addDataRetriever(new SuccessfulDataRetriever())
                        .addDataRetriever(new SuccessfulDataRetriever())
                        .addDataRetriever(new SuccessfulDataRetriever())
                )
        );
    }

    @Test
    public void invalidChildRuleUsingDefaultRulesShouldReturnInvalidChildRuleResponse() {

        var result = this.run(new TestArgument());

        assertEquals(26, result.getExecutionResults().size());
        assertEquals(EngineResultStatus.SUCCESSFUL, result.getStatus());
        assertFalse(result.hasAnyInvalidResults());
        assertEquals(0, result.getInvalidEngineResults().size());

    }

}
