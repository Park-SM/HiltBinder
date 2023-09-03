package com.smparkworld.hiltbinder_processor.core.config

import com.smparkworld.hiltbinder.HiltBinds
import com.smparkworld.hiltbinder.HiltMapBinds
import com.smparkworld.hiltbinder.HiltSetBinds
import javax.lang.model.SourceVersion
import kotlin.reflect.KClass

internal object ProcessorConfig {

    private val JAVA_VERSION = SourceVersion.latest()

    fun getSupportedAnnotationTypes(): Set<KClass<out Annotation>> = setOf(
        HiltBinds::class,
        HiltSetBinds::class,
        HiltMapBinds::class
    )

    fun getSupportedAnnotationTypeNames(): MutableSet<String> =
        getSupportedAnnotationTypes().map { it.java.name }.toMutableSet()

    fun getSupportedJavaVersion(): SourceVersion =
        JAVA_VERSION
}