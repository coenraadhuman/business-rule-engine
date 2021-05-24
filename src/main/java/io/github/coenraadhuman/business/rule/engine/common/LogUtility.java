package io.github.coenraadhuman.business.rule.engine.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogUtility {

    public static void printMessage(String message) {
        var prepend = String.format("%s - %s: ", Thread.currentThread().getName(), LocalDateTime.now());
        System.out.println(prepend + message);
    }

}
