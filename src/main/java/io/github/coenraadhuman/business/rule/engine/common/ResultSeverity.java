package io.github.coenraadhuman.business.rule.engine.common;

import lombok.Getter;
import lombok.Setter;

public enum ResultSeverity {

    RED,
    GREEN,
    YELLOW;

    @Setter
    @Getter
    private String message = "Message wasn't assigned.";

}
