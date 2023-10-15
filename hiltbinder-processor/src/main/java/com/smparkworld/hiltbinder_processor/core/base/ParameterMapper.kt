package com.smparkworld.hiltbinder_processor.core.base

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSDeclaration
import com.smparkworld.hiltbinder_processor.core.Logger
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import kotlin.reflect.KClass

internal interface ParameterMapper<R : ParametersModel, ENV, TARGET> {

    fun getSupportedAnnotationType(): KClass<out Annotation>

    fun toParamsModel(env: ENV, target: TARGET, logger: Logger): R
}

internal interface JavaParameterMapper<R : ParametersModel> : ParameterMapper<R, ProcessingEnvironment, Element>
internal interface KotlinParameterMapper<R : ParametersModel> : ParameterMapper<R, SymbolProcessorEnvironment, KSDeclaration>