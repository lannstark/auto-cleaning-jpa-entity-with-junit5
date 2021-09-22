package com.lannstark.internal.core.user

import com.lannstark.internal.core.BaseEntity
import com.lannstark.internal.core.wallet.Wallet
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany

/**
 * column_list
 * - id
 * - name
 * - age
 */
@Entity
internal class User(
    var name: String,

    var age: Int,
) : BaseEntity() {

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val wallets: MutableList<Wallet> = mutableListOf()

    fun addWallet(brandName: String) {
        wallets.add(
            Wallet(
                user = this,
                brandName = brandName,
                amount = 0
            )
        )
    }
}