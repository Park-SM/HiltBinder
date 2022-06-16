package com.smparkworld.hiltbinder_processor.core.config

import com.smparkworld.hiltbinder_processor.core.ModuleGeneratorFactory
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import kotlin.reflect.KClass

internal object ProcessorConfig {

    private val JAVA_VERSION = SourceVersion.latest()

    fun getSupportedAnnotationTypes(): Set<KClass<out Annotation>> =
        ModuleGeneratorFactory.getSupportedAnnotationTypes()

    fun getSupportedAnnotationTypeNames(): MutableSet<String> =
        getSupportedAnnotationTypes().map { it.java.name }.toMutableSet()

    fun getSupportedJavaVersion(): SourceVersion =
        JAVA_VERSION
}