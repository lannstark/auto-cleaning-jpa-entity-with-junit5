package com.lannstark.internal.core.onetoone

import org.springframework.data.jpa.repository.JpaRepository

internal interface OneToOneBRepository : JpaRepository<OneToOneB, Long>