package com.smparkworld.hiltbinder_processor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.smparkworld.hiltbinder_processor.core.Logger
import com.smparkworld.hiltbinder_processor.core.ModuleGeneratorDispatcher
import com.smparkworld.hiltbinder_processor.core.config.ProcessorConfig
import com.smparkworld.hiltbinder_processor.core.manager.PerformanceManager
import com.smparkworld.hiltbinder_processor.kotlin.extension.accept
import com.smparkworld.hiltbinder_processor.kotlin.extension.isSameType
import kotlin.reflect.KClass

internal class HiltBindsKotlinProcessor(
    private val env: SymbolProcessorEnvironment,
    private val logger: Logger,
    private val dispatcher: ModuleGeneratorDispatcher = ModuleGeneratorDispatcher(logger)
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        PerformanceManager.startProcessing()

        val processedCount = resolver.getAnnotatedSymbolsCountOnEach { annotated, annotationType ->
            annotated.accept { declaration -> visit(declaration, annotationType) }
        }
        if (processedCount > 0) {
            PerformanceManager.stopProcessing()
            PerformanceManager.reportPerformance(logger, processedCount)
        }
        return emptyList()
    }

    private fun visit(declaration: KSDeclaration, annotationType: KClass<out Annotation>) {
        val annotation = declaration.annotations.find { it.isSameType(annotationType) }
            ?: throw IllegalStateException("Not found annotation :: ${annotationType.simpleName}")

        dispatcher.dispatchGenerator(env, declaration, annotation)
    }

    private fun Resolver.getAnnotatedSymbolsCountOnEach(perform: (KSAnnotated, KClass<out Annotation>) -> Unit): Int {
        var count = 0
        ProcessorConfig.getSupportedAnnotationTypes().forEach { annotationType ->
            getSymbolsWithAnnotation(annotationType.java.canonicalName).forEach { annotated ->
                perform.invoke(annotated, annotationType)
                count++
            }
        }
        return count
    }
}

internal class HiltBindsKotlinProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        HiltBindsKotlinProcessor(env = environment, logger = KotlinLogger(environment.logger))
}

private class KotlinLogger(
    private val logger: KSPLogger
) : Logger {

    override fun log(message: String) = logger.logging(message)
    override fun warn(message: String) = logger.warn(message)
    override fun error(message: String) = logger.error(message)
}