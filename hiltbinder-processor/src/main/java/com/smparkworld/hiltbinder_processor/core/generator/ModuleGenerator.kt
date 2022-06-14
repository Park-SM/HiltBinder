package com.smparkworld.hiltbinder_processor.core.generator

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import kotlin.reflect.KClass

internal interface ModuleGenerator {

    fun generate(env: ProcessingEnvironment, element: Element, annotation: Annotation)

    fun getSupportedAnnotationTypes(): Set<KClass<out Annotation>>
}