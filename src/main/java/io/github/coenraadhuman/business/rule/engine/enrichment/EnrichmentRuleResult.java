package io.github.coenraadhuman.business.rule.engine.enrichment;

import io.github.coenraadhuman.business.rule.engine.common.ResultSeverity;
import io.github.coenraadhuman.business.rule.engine.RuleResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class EnrichmentRuleResult extends RuleResult {

    public EnrichmentRuleResult setApplied() {
        this.setSeverity(ResultSeverity.GREEN);
        return this;
    }

    public EnrichmentRuleResult setFailed() {
        this.setSeverity(ResultSeverity.RED);
        return this;
    }

    public EnrichmentRuleResult setNotApplicable() {
        this.setSeverity(ResultSeverity.YELLOW);
        return this;
    }

    public EnrichmentRuleResult setMessage(String message) {
        this.setSeverityMessage(message);
        return this;
    }

}
