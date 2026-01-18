package com.bhnatiuk.uni.bookstore.backend.entity.policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultExceptionWrappingPolicyTest {

    private DefaultExceptionWrappingPolicy policy;

    @BeforeEach
    void setUp() {
        policy = new DefaultExceptionWrappingPolicy();
    }

    @Test
    void wrap_shouldReturnTargetTypeException_whenConstructorIsPresent() {
        // given

    }

}