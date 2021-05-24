package io.github.coenraadhuman.business.rule.engine.enrichment;

import io.github.coenraadhuman.business.rule.engine.AbstractRuleWithData;
import io.github.coenraadhuman.business.rule.engine.RuleResult;

public abstract class AbstractEnrichmentDataRule<Argument> extends AbstractRuleWithData<Argument> {

    public abstract EnrichmentRuleResult enrich(Argument argument);

    @Override
    protected RuleResult executeRule(Argument argument) {
        return this.enrich(argument);
    }
}
