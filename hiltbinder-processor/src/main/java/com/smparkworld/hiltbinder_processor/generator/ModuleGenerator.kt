package com.smparkworld.hiltbinder_processor.generator

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import kotlin.reflect.KClass

interface ModuleGenerator {

    fun generate(env: ProcessingEnvironment, element: Element, annotation: Annotation)

    fun getSupportedAnnotation(): Set<KClass<out Annotation>>
}