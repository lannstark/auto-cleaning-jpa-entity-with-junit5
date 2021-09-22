package com.lannstark

import org.hibernate.event.spi.PostInsertEvent
import org.hibernate.event.spi.PostInsertEventListener
import org.hibernate.persister.entity.EntityPersister

class CustomPostInsertEventListener(
    private val callback: (String) -> Unit
) : PostInsertEventListener {

    override fun requiresPostCommitHanding(persister: EntityPersister?): Boolean {
        return false
    }

    override fun onPostInsert(event: PostInsertEvent) {
        val entityName = event.persister.entityName.split(".").last()
        callback(entityName)
    }

}