package io.github.coenraadhuman.business.rule.engine.enrichment;

import io.github.coenraadhuman.business.rule.engine.AbstractRule;
import io.github.coenraadhuman.business.rule.engine.RuleResult;

public abstract class AbstractEnrichmentRule<Argument> extends AbstractRule<Argument> {

    public abstract EnrichmentRuleResult enrich(Argument argument);

    @Override
    protected RuleResult executeRule(Argument argument) {
        return this.enrich(argument);
    }
}
