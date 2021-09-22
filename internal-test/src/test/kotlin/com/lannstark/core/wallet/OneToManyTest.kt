package com.lannstark.core.wallet

import com.lannstark.core.AbstractSpringBootTest
import com.lansntark.core.user.User
import com.lansntark.core.user.UserRepository
import com.lansntark.core.wallet.WalletRepository
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class OneToManyTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val walletRepository: WalletRepository,
) : AbstractSpringBootTest() {

    @Test
    fun `Saving OneToMany entities works well`() {
        // given
        val user = User("lannstark", 99)
        user.addWallet("NIKE")
        user.addWallet("ADIDAS")
        userRepository.save(user)

        // when
        val userResults = userRepository.findAll()
        val walletResults = walletRepository.findAll()

        // then
        assertThat(userResults).hasSize(1)
        assertThat(walletResults).hasSize(2)
    }

    @Test
    fun `user and wallets will be clean`() {
        // then
        assertThat(userRepository.findAll()).isEmpty()
        assertThat(walletRepository.findAll()).isEmpty()
    }

}