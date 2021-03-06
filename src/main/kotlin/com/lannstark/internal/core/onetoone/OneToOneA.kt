package com.lannstark.internal.core.onetoone

import com.lannstark.internal.core.BaseEntity
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToOne

/**
 * column_list
 * - id
 * - b_id
 */
@Entity
internal class OneToOneA(
    @OneToOne(cascade = [CascadeType.ALL])
    val b: OneToOneB
) : BaseEntity()