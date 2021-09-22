package com.lannstark.internal.core.user

import org.springframework.data.jpa.repository.JpaRepository

internal interface UserRepository : JpaRepository<User, Long>