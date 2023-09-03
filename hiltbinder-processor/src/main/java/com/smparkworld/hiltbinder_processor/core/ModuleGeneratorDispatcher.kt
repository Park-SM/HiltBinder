package com.smparkworld.hiltbinder_processor.core

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSDeclaration
import com.smparkworld.hiltbinder_processor.core.base.JavaModuleGenerator
import com.smparkworld.hiltbinder_processor.core.base.JavaParameterMapper
import com.smparkworld.hiltbinder_processor.core.base.KotlinModuleGenerator
import com.smparkworld.hiltbinder_processor.core.base.KotlinParameterMapper
import com.smparkworld.hiltbinder_processor.core.base.ParametersModel
import com.smparkworld.hiltbinder_processor.java.extension.isSameType
import com.smparkworld.hiltbinder_processor.kotlin.extension.isSameType
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

internal class ModuleGeneratorDispatcher(
    private val logger: Logger,
    private val javaModuleGenerators: Set<JavaModuleGenerator<*>> = ModuleGeneratorFactory.createJavaModuleGenerators(),
    private val javaParameterMappers: Set<JavaParameterMapper<*>> = ParameterMapperFactory.createJavaParameterMappers(),
    private val kotlinModuleGenerators: Set<KotlinModuleGenerator<*>> = ModuleGeneratorFactory.createKotlinModuleGenerators(),
    private val kotlinParameterMappers: Set<KotlinParameterMapper<*>> = ParameterMapperFactory.createKotlinParameterMappers(),
) {

    fun dispatchGenerator(
        env: ProcessingEnvironment,
        element: Element,
        annotation: Annotation
    ): Boolean {
        var isGenerated = false

        javaModuleGenerators.forEach { generator ->
            if (!isGenerated && isAppropriateGenerator(generator, element, annotation)) {
                generator.initialize(mapToParamModel(element, annotation, env))
                generator.generate(env, logger)
                isGenerated = true
            }
        }
        if (isGenerated) {
            logger.log("$TAG $element")
        } else {
            logger.error("$TAG $element annotated with ${annotation.annotationClass.simpleName} are not supported.")
        }
        return isGenerated
    }

    fun dispatchGenerator(
        env: SymbolProcessorEnvironment,
        declaration: KSDeclaration,
        annotation: KSAnnotation
    ): Boolean {
        var isGenerated = false

        kotlinModuleGenerators.forEach { generator ->
            if (!isGenerated && isAppropriateGenerator(generator, declaration, annotation)) {
                generator.initialize(mapToParamModel(declaration, annotation, env))
                generator.generate(env, logger)
                isGenerated = true
            }
        }
        if (isGenerated) {
            logger.log("$TAG $declaration")
        } else {
            logger.error("$TAG $declaration annotated with $annotation are not supported.")
        }
        return isGenerated
    }

    private fun isAppropriateGenerator(
        generator: JavaModuleGenerator<*>,
        element: Element,
        annotation: Annotation
    ): Boolean = (annotation.isSameType(generator.getSupportedAnnotationType()) && generator.checkValidation(element))

    private fun isAppropriateGenerator(
        generator: KotlinModuleGenerator<*>,
        declaration: KSDeclaration,
        annotation: KSAnnotation
    ): Boolean = (annotation.isSameType(generator.getSupportedAnnotationType()) && generator.checkValidation(declaration))

    private fun mapToParamModel(
        element: Element,
        annotation: Annotation,
        env: ProcessingEnvironment
    ): ParametersModel {
        return javaParameterMappers.find { annotation.isSameType(it.getSupportedAnnotationType()) }
            ?.toParamsModel(env, element, logger)
            ?: throw IllegalStateException("Not found matched mapper.")
    }

    private fun mapToParamModel(
        declaration: KSDeclaration,
        annotation: KSAnnotation,
        env: SymbolProcessorEnvironment
    ): ParametersModel {
        return kotlinParameterMappers.find { annotation.isSameType(it.getSupportedAnnotationType()) }
            ?.toParamsModel(env, declaration, logger)
            ?: throw IllegalStateException("Not found matched mapper.")
    }

    companion object {

        private const val TAG = "HiltBinder: processing:"
    }
}