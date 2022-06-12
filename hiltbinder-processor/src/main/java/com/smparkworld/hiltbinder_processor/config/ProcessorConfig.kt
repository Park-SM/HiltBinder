package com.smparkworld.hiltbinder_processor.config

import com.smparkworld.hiltbinder.HiltBinds
import javax.lang.model.SourceVersion
import kotlin.reflect.KClass

object ProcessorConfig {

    private val JAVA_VERSION = SourceVersion.latest()
    private val ANNOTATION_TYPES = mutableListOf<KClass<out Annotation>>(
        HiltBinds::class,
    )

    fun getSupportedAnnotationTypes(): MutableList<KClass<out Annotation>> =
        ANNOTATION_TYPES

    fun getSupportedAnnotationTypeNames(): MutableSet<String> =
        ANNOTATION_TYPES.map { it.java.name }.toMutableSet()

    fun getSupportedJavaVersion(): SourceVersion =
        JAVA_VERSION
}