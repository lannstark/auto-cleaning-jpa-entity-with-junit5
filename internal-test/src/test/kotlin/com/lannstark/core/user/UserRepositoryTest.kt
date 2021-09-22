package com.lannstark.core.user

import com.lannstark.core.AbstractSpringBootTest
import com.lansntark.core.user.User
import com.lansntark.core.user.UserRepository
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class UserRepositoryTest(
    @Autowired private val userRepository: UserRepository,
) : AbstractSpringBootTest() {

    @Test
    fun `Save User Test - 1`() {
        // given
        userRepository.save(User("lansntark", 99))

        // when
        val results = userRepository.findAll()

        // then
        assertThat(results).hasSize(1)
    }

    @Test
    fun `Save User Test - 2`() {
        // given
        userRepository.save(User("lansntark", 99))

        // when
        val results = userRepository.findAll()

        // then
        assertThat(results).hasSize(1)
    }

}