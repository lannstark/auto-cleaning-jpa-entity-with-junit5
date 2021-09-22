package com.lannstark

import com.lannstark.CleaningSpringBootTest.SingletonProvider.dependencyMap
import com.lannstark.CleaningSpringBootTest.SingletonProvider.entityCountMap
import com.lannstark.CleaningSpringBootTest.SingletonProvider.entityJavaTypeMap
import com.lannstark.CleaningSpringBootTest.SingletonProvider.hasNotCount
import org.hibernate.Session
import org.hibernate.engine.spi.SessionFactoryImplementor
import org.hibernate.event.service.spi.EventListenerRegistry
import org.hibernate.event.spi.EventType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.query.QueryUtils
import java.lang.reflect.Field
import javax.persistence.EntityManagerFactory
import javax.persistence.OneToOne
import javax.persistence.metamodel.Attribute
import javax.persistence.metamodel.EntityType

/**
 * In this version, @ManyToMany not Supported
 * Only @OneToOne, @OneToMany, @ManyToOne are supported
 * Suppose that only @OneToMany will be not used alone
 *
 *
 * TODO info -> debug or info -> error (yml Configuration)
 */
@ExtendWith(AutoCleaningJpaEntityExtension::class)
abstract class CleaningSpringBootTest {

    @Autowired
    private lateinit var entityManagerFactory: EntityManagerFactory

    private val log: Logger
        get() = LoggerFactory.getLogger(CleaningSpringBootTest::class.java)

    // Hibernate 5.4.x ManagedType Impl : EntityTypeImpl, Attribute Impl : XXXAttributeImpl
    @BeforeEach
    fun beforeEach() {
        if (SingletonProvider.init) {
            return
        }

        log.info("JPA Entity Auto Cleaning Init Start...")
        val registry = entityManagerFactory.unwrap(SessionFactoryImplementor::class.java)
            .serviceRegistry.getService(EventListenerRegistry::class.java)
        registry.appendListeners(EventType.POST_INSERT, CustomPostInsertEventListener(SingletonProvider::plusCount))
        log.info("Post insert listener registered complete")

        entityManagerFactory.metamodel.managedTypes
            .filterIsInstance<EntityType<*>>()
            .forEach { entityType ->
                entityJavaTypeMap[entityType.javaType] = entityType.name
                entityCountMap[entityType.name] = 0
            }

        entityManagerFactory.metamodel.managedTypes
            .filterIsInstance<EntityType<*>>()
            .forEach { entityType ->

                // OneToOne Handling
                entityType.attributes
                    .filter { attribute -> attribute.persistentAttributeType == Attribute.PersistentAttributeType.ONE_TO_ONE }
                    .forEach { attribute ->
                        val oneToOne = (attribute.javaMember as Field).getAnnotation(OneToOne::class.java)
                        if (oneToOne.mappedBy != "") {
                            addDependencyMap(entityType.javaType, attribute.javaType)
                        }
                    }

                // ManyToOne (single-side) Handling
                entityType.attributes
                    .filter { attribute -> attribute.persistentAttributeType == Attribute.PersistentAttributeType.MANY_TO_ONE }
                    .forEach { attribute ->
                        addDependencyMap(attribute.javaType, entityType.javaType)
                    }
            }

        SingletonProvider.init = true
    }

    private fun addDependencyMap(javaTypeOfEntity: Class<*>, javaTypeOfAttribute: Class<*>) {
        val keyEntityName = entityJavaTypeMap[javaTypeOfEntity]!!
        val valueEntityName = entityJavaTypeMap[javaTypeOfAttribute]!!

        if (dependencyMap.containsKey(keyEntityName)) {
            dependencyMap[keyEntityName]!!.add(valueEntityName)
        } else {
            dependencyMap[keyEntityName] = mutableSetOf(valueEntityName)
        }
    }

    @AfterEach
    fun clean(@NeedClean needClean: Boolean) {
        if (!needClean) {
            return
        }

        var entityName = SingletonProvider.firstEntityNameShouldBeDeleted()
        while (entityName != null) {
            deleteAll(entityName)
            entityName = SingletonProvider.firstEntityNameShouldBeDeleted()
        }
    }

    /**
     * Delete from Database, EntityCount (Recursive Function)
     */
    private fun deleteAll(entityName: String) {
        dependencyMap[entityName]
            ?.forEach { dependencyEntityName -> deleteAll(dependencyEntityName) }
        if (hasNotCount(entityName)) {
            return
        }

        log.info("Try to delete all of $entityName")
        val entityManager = entityManagerFactory.createEntityManager()
        try {
            val session = entityManager.unwrap(Session::class.java)
            val tx = session.transaction
            tx.begin()
            entityManager.createQuery(QueryUtils.getQueryString(QueryUtils.DELETE_ALL_QUERY_STRING, entityName))
                .executeUpdate()
            tx.commit()
            log.info("Delete all of $entityName successfully ")
            SingletonProvider.clear(entityName)
        } catch (e: Exception) {
            log.error("{}", e)
            throw e
        }
        entityManager.close()
    }

    object SingletonProvider {
        internal var init: Boolean = false

        // EntityName -> saved count
        internal val entityCountMap: MutableMap<String, Int> = mutableMapOf()

        // EntityJavaType -> EntityName
        internal val entityJavaTypeMap = mutableMapOf<Class<*>, String>()

        /**
         * EntityName -> List<Dependent EntityName>
         * If A depends on B, (which means first delete B before delete A) A to listOf(B) will be saved
         */
        val dependencyMap = mutableMapOf<String, MutableSet<String>>()

        internal fun hasNotCount(entityName: String): Boolean {
            return entityCountMap[entityName] == 0
        }

        internal fun plusCount(entityName: String) {
            entityCountMap[entityName] = entityCountMap[entityName]!! + 1
        }

        internal fun firstEntityNameShouldBeDeleted(): String? {
            return entityCountMap
                .filter { (_, value) -> value != 0 }
                .map { (key, _) -> key }
                .firstOrNull()
        }

        internal fun clear(entityName: String) {
            entityCountMap[entityName] = 0
        }
    }

}
