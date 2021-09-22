package com.lansntark.core.wallet

import com.lansntark.core.BaseEntity
import com.lansntark.core.user.User
import javax.persistence.Entity
import javax.persistence.ManyToOne

/**
 * column_list
 * - id
 * - user_id (FK)
 * - brand_name
 * - amount
 */
@Entity
internal class Wallet(
    @ManyToOne
    var user: User,

    var brandName: String,

    var amount: Int,
) : BaseEntity()