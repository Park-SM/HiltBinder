package com.smparkworld.hiltbinder_processor.core.base

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSDeclaration
import com.smparkworld.hiltbinder_processor.core.Logger
import kotlin.reflect.KClass

internal interface KotlinModuleGenerator {

    fun getSupportedAnnotationType(): KClass<out Annotation>

    fun checkValidation(declaration: KSDeclaration): Boolean

    fun generate(env: SymbolProcessorEnvironment, declaration: KSDeclaration, annotation: KSAnnotation, logger: Logger)
}