package io.github.coenraadhuman.business.rule.engine;

import org.junit.jupiter.api.Test;

import java.util.List;

import io.github.coenraadhuman.business.rule.engine.implementation.common.TestArgument;
import io.github.coenraadhuman.business.rule.engine.implementation.common.TestResponse;
import io.github.coenraadhuman.business.rule.engine.implementation.data.retriever.DeferredInvalidDataRetriever;
import io.github.coenraadhuman.business.rule.engine.implementation.data.retriever.ExceptionDataRetriever;
import io.github.coenraadhuman.business.rule.engine.implementation.data.retriever.ImmediateInvalidDataRetriever;
import io.github.coenraadhuman.business.rule.engine.implementation.data.retriever.ValidDataRetriever;
import io.github.coenraadhuman.business.rule.engine.implementation.rules.data.TestValidationInfoValidChildDataRule;
import io.github.coenraadhuman.business.rule.engine.implementation.rules.normal.TestValidationDeferredInvalidChildRule;
import io.github.coenraadhuman.business.rule.engine.implementation.rules.normal.TestValidationImmediateInvalidChildRule;
import io.github.coenraadhuman.business.rule.engine.implementation.rules.normal.TestValidationImmediateInvalidRule;
import io.github.coenraadhuman.business.rule.engine.implementation.rules.normal.TestValidationInfoProviderImmediateInvalidChildRule;
import io.github.coenraadhuman.business.rule.engine.implementation.rules.normal.TestValidationNotApplicableRule;
import io.github.coenraadhuman.business.rule.engine.implementation.rules.normal.TestValidationValidChildRule;
import io.github.coenraadhuman.business.rule.engine.implementation.rules.normal.TestValidationValidRule;
import io.github.coenraadhuman.business.rule.engine.result.EngineResultStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ValidationEngineTest extends ValidationEngineCommon<TestResponse, TestArgument> {

    @Override
    public void setRules() {
        setRules(
            List.of(
                new TestValidationValidRule()
                    .setChildRules(
                        List.of(
                            new TestValidationValidChildRule(),
                            new TestValidationImmediateInvalidChildRule())),
                new TestValidationValidRule()));
    }

    @Test
    public void invalidChildRuleUsingDefaultRulesShouldReturnInvalidChildRuleResponse() {

        var result = this.validate(new TestArgument());

        assertEquals(TestResponse.TEST_VALIDATION_IMMEDIATE_INVALID_CHILD_RULE, result.getRuleResult());

        assertEquals(3, result.getEngineResults().size());
        assertEquals(
            TestResponse.TEST_VALIDATION_VALID_RULE, result.getEngineResults().get(0).getRuleResult());
        assertEquals(
            TestResponse.TEST_VALIDATION_VALID_CHILD_RULE,
            result.getEngineResults().get(1).getRuleResult());
        assertEquals(
            TestResponse.TEST_VALIDATION_IMMEDIATE_INVALID_CHILD_RULE,
            result.getEngineResults().get(2).getRuleResult());

        assertEquals(0, result.getEngineSoftInvalidResults().size());
    }

    @Test
    public void notApplicableRuleShouldSkipAndReturnValidRuleResponse() {
        this.setRules(
            List.of(
                new TestValidationNotApplicableRule()
                    .setChildRules(List.of(new TestValidationImmediateInvalidChildRule())),
                new TestValidationValidRule()));

        var result = this.executeEngine(new TestArgument());

        assertEquals(TestResponse.TEST_VALIDATION_VALID_RULE, result.getRuleResult());

        assertEquals(2, result.getEngineResults().size());
        assertEquals(
            TestResponse.TEST_VALIDATION_NOT_APPLICABLE_RULE,
            result.getEngineResults().get(0).getRuleResult());
        assertEquals(
            TestResponse.TEST_VALIDATION_VALID_RULE, result.getEngineResults().get(1).getRuleResult());

        assertEquals(0, result.getEngineSoftInvalidResults().size());
    }

    @Test
    public void notApplicableRuleShouldNotRunChildrenAndReturnNotApplicableRuleValidRuleResponse() {
        this.setRules(
            List.of(
                new TestValidationNotApplicableRule()
                    .setChildRules(List.of(new TestValidationImmediateInvalidChildRule()))));

        var result = this.executeEngine(new TestArgument());
        assertEquals(TestResponse.TEST_VALIDATION_NOT_APPLICABLE_RULE, result.getRuleResult());

        assertEquals(1, result.getEngineResults().size());
        assertEquals(
            TestResponse.TEST_VALIDATION_NOT_APPLICABLE_RULE,
            result.getEngineResults().get(0).getRuleResult());
    }

    @Test
    public void when_ProviderIsGiven_should_BeAvailableToChildRule() {
        this.setRules(
            List.of(
                new TestValidationValidRule()
                    .setChildRules(List.of(new TestValidationInfoProviderImmediateInvalidChildRule())),
                new TestValidationValidRule()));

        var result = this.executeEngine(new TestArgument());

        assertEquals(
            TestResponse.TEST_VALIDATION_PROVIDER_IMMEDIATE_INVALID_CHILD_RULE, result.getRuleResult());

        assertEquals(2, result.getEngineResults().size());
        assertEquals(
            TestResponse.TEST_VALIDATION_VALID_RULE, result.getEngineResults().get(0).getRuleResult());
        assertEquals(
            TestResponse.TEST_VALIDATION_PROVIDER_IMMEDIATE_INVALID_CHILD_RULE,
            result.getEngineResults().get(1).getRuleResult());
    }

    @Test
    public void
    when_ValidDataChildRuleWithValidDataRetriever_should_returnValidDataChildRuleResponse() {
        this.setRules(
            List.of(
                new TestValidationValidRule()
                    .setChildRules(
                        List.of(
                            new TestValidationValidChildRule(),
                            new TestValidationInfoValidChildDataRule()
                                .setDataRetrievers(List.of(new ValidDataRetriever()))))));

        var result = this.executeEngine(new TestArgument());

        assertEquals(
            TestResponse.TEST_VALIDATION_PROVIDER_VALID_DATA_CHILD_RULE, result.getRuleResult());
        assertEquals(EngineResultStatus.VALID, result.getStatus());

        assertEquals(4, result.getEngineResults().size());
        assertEquals(
            TestResponse.TEST_VALIDATION_VALID_RULE, result.getEngineResults().get(0).getRuleResult());
        assertEquals(
            TestResponse.TEST_VALIDATION_VALID_CHILD_RULE,
            result.getEngineResults().get(1).getRuleResult());
        assertEquals(
            TestResponse.VALID_DATA_RETRIEVER, result.getEngineResults().get(2).getRuleResult());
        assertEquals(
            TestResponse.TEST_VALIDATION_PROVIDER_VALID_DATA_CHILD_RULE,
            result.getEngineResults().get(3).getRuleResult());
    }

    @Test
    public void
    when_ValidDataChildRuleWithInvalidDataRetriever_should_returnValidDataChildRuleResponse() {
        this.setRules(
            List.of(
                new TestValidationValidRule()
                    .setChildRules(
                        List.of(
                            new TestValidationValidChildRule(),
                            new TestValidationInfoValidChildDataRule()
                                .setDataRetrievers(
                                    List.of(
                                        new ValidDataRetriever()
                                            .setChildDataRetrievers(
                                                List.of(
                                                    new ValidDataRetriever(),
                                                    new ImmediateInvalidDataRetriever(),
                                                    new ValidDataRetriever())),
                                        new ValidDataRetriever())),
                            new TestValidationInfoValidChildDataRule())),
                new TestValidationValidRule()));

        var result = this.executeEngine(new TestArgument());

        assertEquals(TestResponse.IMMEDIATE_INVALID_DATA_RETRIEVER, result.getRuleResult());
        assertEquals(EngineResultStatus.IMMEDIATE_INVALID, result.getStatus());

        assertEquals(5, result.getEngineResults().size());
        assertEquals(
            TestResponse.TEST_VALIDATION_VALID_RULE, result.getEngineResults().get(0).getRuleResult());
        assertEquals(
            TestResponse.TEST_VALIDATION_VALID_CHILD_RULE,
            result.getEngineResults().get(1).getRuleResult());
        assertEquals(
            TestResponse.VALID_DATA_RETRIEVER, result.getEngineResults().get(2).getRuleResult());
        assertEquals(
            TestResponse.VALID_DATA_RETRIEVER, result.getEngineResults().get(3).getRuleResult());
        assertEquals(
            TestResponse.IMMEDIATE_INVALID_DATA_RETRIEVER,
            result.getEngineResults().get(4).getRuleResult());
    }

    @Test
    public void
    when_ValidDataChildRuleWithSoftInvalidChildRules_should_returnValidDataChildRuleResponse() {
        this.setRules(
            List.of(
                new TestValidationValidRule()
                    .setChildRules(
                        List.of(
                            new TestValidationDeferredInvalidChildRule(),
                            new TestValidationDeferredInvalidChildRule(),
                            new TestValidationDeferredInvalidChildRule())),
                new TestValidationValidRule()));

        var result = this.executeEngine(new TestArgument());

        assertNull(result.getRuleResult());
        assertEquals("Deferred invalidations occurred, see history.", result.getMessage());
        assertEquals(EngineResultStatus.DEFERRED_INVALID, result.getStatus());

        assertEquals(5, result.getEngineResults().size());
        assertEquals(
            TestResponse.TEST_VALIDATION_VALID_RULE, result.getEngineResults().get(0).getRuleResult());
        assertEquals(
            TestResponse.TEST_VALIDATION_DEFERRED_INVALID_CHILD_RULE,
            result.getEngineResults().get(1).getRuleResult());
        assertEquals(
            TestResponse.TEST_VALIDATION_DEFERRED_INVALID_CHILD_RULE,
            result.getEngineResults().get(2).getRuleResult());
        assertEquals(
            TestResponse.TEST_VALIDATION_DEFERRED_INVALID_CHILD_RULE,
            result.getEngineResults().get(3).getRuleResult());
        assertEquals(
            TestResponse.TEST_VALIDATION_VALID_RULE, result.getEngineResults().get(4).getRuleResult());
    }

    @Test
    public void when_AnyInvalidDataRetriever_should_returnImmediateInvalid() {
        this.setRules(
            List.of(
                new TestValidationValidRule()
                    .setChildRules(
                        List.of(
                            new TestValidationInfoValidChildDataRule()
                                .setDataRetrievers(
                                    List.of(
                                        new DeferredInvalidDataRetriever(),
                                        new DeferredInvalidDataRetriever(),
                                        new DeferredInvalidDataRetriever())))),
                new TestValidationValidRule()));

        var result = this.executeEngine(new TestArgument());

        assertEquals(EngineResultStatus.IMMEDIATE_INVALID, result.getStatus());

        assertEquals(2, result.getEngineResults().size());
        assertEquals(
            TestResponse.TEST_VALIDATION_VALID_RULE, result.getEngineResults().get(0).getRuleResult());
        assertEquals(
            TestResponse.DEFERRED_INVALID_DATA_RETRIEVER,
            result.getEngineResults().get(1).getRuleResult());
    }

    @Test
    public void addNewChildRuleWithValidResult() {
        this.setRules(
            List.of(
                new TestValidationValidRule()
                    .addChildRule(new TestValidationValidChildRule())
                    .addChildRule(new TestValidationValidChildRule()),
                new TestValidationValidRule()));

        var result = this.executeEngine(new TestArgument());

        assertEquals(EngineResultStatus.VALID, result.getStatus());
        assertEquals(4, result.getEngineResults().size());
    }

    @Test
    public void addNewChildRuleWithImmediateInvalidResult() {
        this.setRules(
            List.of(
                new TestValidationValidRule(),
                new TestValidationNotApplicableRule(),
                new TestValidationValidRule()
                    .addChildRule(new TestValidationValidChildRule())
                    .addChildRule(new TestValidationImmediateInvalidChildRule())
                    .addChildRule(new TestValidationValidChildRule()),
                new TestValidationValidRule()));

        var result = this.executeEngine(new TestArgument());

        assertEquals(EngineResultStatus.IMMEDIATE_INVALID, result.getStatus());
        assertEquals(5, result.getEngineResults().size());
    }

    @Test
    public void addNewChildRuleWithDeferredInvalidResult() {
        this.setRules(
            List.of(
                new TestValidationValidRule(),
                new TestValidationValidRule()
                    .addChildRule(new TestValidationDeferredInvalidChildRule())
                    .addChildRule(new TestValidationValidChildRule())
                    .addChildRule(new TestValidationValidChildRule()),
                new TestValidationNotApplicableRule(),
                new TestValidationImmediateInvalidRule()));

        var result = this.executeEngine(new TestArgument());

        assertEquals(EngineResultStatus.DEFERRED_INVALID, result.getStatus());
        assertEquals(7, result.getEngineResults().size());
    }

    @Test
    public void addDataRetrieverWithValidResult() {
        this.setRules(
            List.of(
                new TestValidationValidRule()
                    .addChildRule(
                        new TestValidationInfoValidChildDataRule()
                            .addDataRetriever(new ValidDataRetriever())
                            .addDataRetriever(new ValidDataRetriever())),
                new TestValidationValidRule().addChildRule(new TestValidationValidChildRule()),
                new TestValidationValidRule()));

        var result = this.executeEngine(new TestArgument());

        assertEquals(EngineResultStatus.VALID, result.getStatus());
        assertEquals(7, result.getEngineResults().size());
    }

    @Test
    public void addNewChildRuleAndIfDuplicatesAreCateredFor() {

        final var validChildDataRetriever = new ValidDataRetriever();

        final var testValidationValidChildRule = new TestValidationInfoValidChildDataRule();
        testValidationValidChildRule
            .addDataRetriever(
                new ValidDataRetriever()
                    .addChildDataRetriever(validChildDataRetriever)
                    .addChildDataRetriever(validChildDataRetriever))
            .addDataRetriever(new DeferredInvalidDataRetriever());

        this.setRules(
            List.of(
                new TestValidationValidRule()
                    .addChildRule(new TestValidationValidChildRule())
                    .addChildRule(testValidationValidChildRule)
                    .addChildRule(new TestValidationDeferredInvalidChildRule())
                    .addChildRule(testValidationValidChildRule)
                    .addChildRule(testValidationValidChildRule),
                new TestValidationValidRule()));

        var result = this.executeEngine(new TestArgument());

        assertEquals(EngineResultStatus.IMMEDIATE_INVALID, result.getStatus());
        assertEquals(5, result.getEngineResults().size());
    }

    @Test
    public void addDataRetrieverAndIfDuplicatesAreCateredFor() {

        final var validChildDataRetriever = new ValidDataRetriever();

        final var validDataRetriever =
            new ValidDataRetriever()
                .addChildDataRetriever(validChildDataRetriever)
                .addChildDataRetriever(new ValidDataRetriever())
                .addChildDataRetriever(
                    validChildDataRetriever.addChildDataRetriever(new ImmediateInvalidDataRetriever()))
                .addChildDataRetriever(new ExceptionDataRetriever());

        this.setRules(
            List.of(
                new TestValidationValidRule()
                    .addChildRule(
                        new TestValidationInfoValidChildDataRule()
                            .addDataRetriever(validDataRetriever)
                            .addDataRetriever(validDataRetriever)
                            .addDataRetriever(new ExceptionDataRetriever())),
                new TestValidationValidRule(),
                new TestValidationNotApplicableRule(),
                new TestValidationValidRule()
                    .addChildRule(new TestValidationInfoProviderImmediateInvalidChildRule())
                    .addChildRule(new TestValidationValidChildRule())));

        var result = this.executeEngine(new TestArgument());

        assertEquals(EngineResultStatus.IMMEDIATE_INVALID, result.getStatus());
        assertEquals(4, result.getEngineResults().size());
    }
}
