package com.lansntark.core.onetoone

import org.springframework.data.jpa.repository.JpaRepository

internal interface OneToOneARepository : JpaRepository<OneToOneA, Long>