package io.github.coenraadhuman.implementation.common;

import lombok.Data;

import java.util.concurrent.atomic.AtomicReference;

@Data
public class TestArgument {

    private AtomicReference<String> dataRetrieverMessage;

}
