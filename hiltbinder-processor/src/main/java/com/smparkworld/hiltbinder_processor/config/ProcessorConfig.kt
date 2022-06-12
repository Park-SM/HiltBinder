package com.smparkworld.hiltbinder_processor.config

import com.smparkworld.hiltbinder.HiltBinds
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import kotlin.reflect.KClass

internal object ProcessorConfig {

    private val JAVA_VERSION = SourceVersion.latest()

    private val ANNOTATION_TYPES = mutableListOf<KClass<out Annotation>>(
        HiltBinds::class,
    )

    private val ELEMENT_TYPES = mutableListOf(
        ElementKind.CLASS, ElementKind.INTERFACE
    )

    fun checkSupportedElementType(elementType: ElementKind): Boolean =
        ELEMENT_TYPES.find { it == elementType } != null

    fun getSupportedAnnotationTypes(): MutableList<KClass<out Annotation>> =
        ANNOTATION_TYPES

    fun getSupportedAnnotationTypeNames(): MutableSet<String> =
        ANNOTATION_TYPES.map { it.java.name }.toMutableSet()

    fun getSupportedJavaVersion(): SourceVersion =
        JAVA_VERSION
}