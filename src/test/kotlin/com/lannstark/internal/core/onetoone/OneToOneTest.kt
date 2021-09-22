package com.lannstark.internal.core.onetoone

import com.lannstark.internal.core.AbstractSpringBootTest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class OneToOneTest(
    @Autowired private val oneToOneARepository: OneToOneARepository,
    @Autowired private val oneToOneBRepository: OneToOneBRepository,
) : AbstractSpringBootTest() {

    @Test
    fun `OneToOne two entities are saved`() {
        // given
        oneToOneARepository.save(OneToOneA(OneToOneB()))

        // when
        val aResults = oneToOneARepository.findAll()
        val bResults = oneToOneBRepository.findAll()

        // then
        assertThat(aResults).hasSize(1)
        assertThat(bResults).hasSize(1)
    }

    @Test
    fun `Entities must be clean`() {
        // then
        assertThat(oneToOneARepository.findAll()).isEmpty()
        assertThat(oneToOneARepository.findAll()).isEmpty()
    }

}