package com.lansntark.core.wallet

import org.springframework.data.jpa.repository.JpaRepository

internal interface WalletRepository : JpaRepository<Wallet, Long>