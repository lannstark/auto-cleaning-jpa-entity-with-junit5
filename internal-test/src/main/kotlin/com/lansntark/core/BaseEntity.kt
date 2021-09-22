package com.lansntark.core

import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
internal abstract class BaseEntity(
    @Id
    @GeneratedValue
    val id: Long = 0L
)