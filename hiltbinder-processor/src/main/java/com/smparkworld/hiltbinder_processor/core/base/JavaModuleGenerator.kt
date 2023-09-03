package com.smparkworld.hiltbinder_processor.core.base

import com.smparkworld.hiltbinder_processor.core.Logger
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import kotlin.reflect.KClass

internal interface JavaModuleGenerator {

    fun getSupportedAnnotationType(): KClass<out Annotation>

    fun checkValidation(element: Element): Boolean

    fun generate(env: ProcessingEnvironment, element: Element, annotation: Annotation, logger: Logger)
}