package com.lansntark.core.onetoone

import com.lansntark.core.BaseEntity
import javax.persistence.Entity
import javax.persistence.OneToOne

/**
 * id
 */
@Entity
internal class OneToOneB : BaseEntity() {

    @OneToOne(mappedBy = "b")
    var a: OneToOneA? = null

}