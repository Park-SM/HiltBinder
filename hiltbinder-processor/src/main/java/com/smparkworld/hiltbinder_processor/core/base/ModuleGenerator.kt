package com.smparkworld.hiltbinder_processor.core.base

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import kotlin.reflect.KClass

internal interface ModuleGenerator {

    fun getSupportedAnnotationType(): KClass<out Annotation>

    fun getSupportedElementTypes(): List<ElementKind>

    fun generate(env: ProcessingEnvironment, element: Element, annotation: Annotation)
}