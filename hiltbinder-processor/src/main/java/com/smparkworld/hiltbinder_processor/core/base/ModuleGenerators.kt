package com.smparkworld.hiltbinder_processor.core.base

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSDeclaration
import com.smparkworld.hiltbinder_processor.core.Logger
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import kotlin.reflect.KClass

internal abstract class ModuleGenerator<PARAM_MODEL : ParametersModel, ENV, TARGET> {

    private var params: PARAM_MODEL? = null

    @Suppress("UNCHECKED_CAST")
    fun initialize(params: ParametersModel) {
        this.params = (params as? PARAM_MODEL)
            ?: throw IllegalAccessException("Invalid initialization.")
    }

    fun generate(env: ENV, logger: Logger) {
        params?.let { generate(env, it, logger) }
            ?: throw IllegalAccessException("Uninitialized ModuleGenerator.")
    }

    abstract fun getSupportedAnnotationType(): KClass<out Annotation>
    abstract fun checkValidation(element: TARGET): Boolean
    protected abstract fun generate(env: ENV, params: PARAM_MODEL, logger: Logger)
}

internal abstract class JavaModuleGenerator<PARAM_MODEL : ParametersModel>
    : ModuleGenerator<PARAM_MODEL, ProcessingEnvironment, Element>()

internal abstract class KotlinModuleGenerator<PARAM_MODEL : ParametersModel>
    : ModuleGenerator<PARAM_MODEL, SymbolProcessorEnvironment, KSDeclaration>()