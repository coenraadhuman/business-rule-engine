package io.github.coenraadhuman.business.rule.engine;

import io.github.coenraadhuman.business.rule.engine.result.EngineResult;

public interface ValidationEngine<RuleResultType, ArgumentType> {

    EngineResult<RuleResultType> validate(final ArgumentType argument);
}
