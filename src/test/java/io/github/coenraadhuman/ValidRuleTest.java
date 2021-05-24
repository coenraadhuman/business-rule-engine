package io.github.coenraadhuman;

import io.github.coenraadhuman.business.rule.engine.AbstractBusinessRuleEngine;
import io.github.coenraadhuman.business.rule.engine.AbstractRule;
import io.github.coenraadhuman.business.rule.engine.result.EngineResultStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import io.github.coenraadhuman.implementation.common.TestArgument;
import io.github.coenraadhuman.implementation.rules.normal.TestValidationValidRule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ValidRuleTest extends AbstractBusinessRuleEngine<TestArgument> {

    @Override
    protected List<AbstractRule<TestArgument>> setEngineRules() {
        return List.of(
                new TestValidationValidRule(),
                new TestValidationValidRule()
                        .setChildRules(
                                List.of(
                                        new TestValidationValidRule(),
                                        new TestValidationValidRule())
        ));
    }

    @Test
    public void invalidChildRuleUsingDefaultRulesShouldReturnInvalidChildRuleResponse() {

        var result = this.run(new TestArgument());

        assertEquals(4, result.getExecutionResults().size());
        assertEquals(EngineResultStatus.SUCCESSFUL, result.getStatus());
        assertFalse(result.hasAnyInvalidResults());
        assertEquals(0, result.getInvalidEngineResults().size());

    }

}
