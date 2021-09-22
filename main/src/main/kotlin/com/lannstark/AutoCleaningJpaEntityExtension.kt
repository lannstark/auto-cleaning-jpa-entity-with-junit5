package com.lannstark

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver

class AutoCleaningJpaEntityExtension : ParameterResolver {

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.findAnnotation(NeedClean::class.java).isPresent &&
                parameterContext.parameter.type == Boolean::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        val method = extensionContext.requiredTestMethod
        return !method.isAnnotationPresent(NoClean::class.java)
    }

}