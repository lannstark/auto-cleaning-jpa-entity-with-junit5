package com.lannstark

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(AutoCleaningJpaEntityExtension::class)
class AutoCleaningJpaEntityExtensionNoCleanTest {

    @AfterEach
    fun afterEach(@NeedClean needClean: Boolean) {
        assertThat(needClean).isFalse
    }

    @Test
    @NoClean
    fun testWithNoClean() {

    }

}