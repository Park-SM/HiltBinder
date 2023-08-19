package com.smparkworld.hiltbinder_processor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.smparkworld.hiltbinder_processor.core.Logger
import com.smparkworld.hiltbinder_processor.core.config.ProcessorConfig
import com.smparkworld.hiltbinder_processor.core.manager.PerformanceManager
import kotlin.reflect.KClass

class HiltBindsKotlinProcessor(
    private val logger: Logger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {

        PerformanceManager.startProcessing()

        val processedCount = resolver.getSymbolsWithAnnotations(ProcessorConfig.getSupportedAnnotationTypes()).size

        if (processedCount > 0) {
            PerformanceManager.stopProcessing()
            PerformanceManager.reportPerformance(logger, processedCount)
        }

        return emptyList()
    }

    private fun Resolver.getSymbolsWithAnnotations(annotations: Set<KClass<out Annotation>>): List<KSAnnotated> =
        mutableSetOf<KSAnnotated>().also { results ->
            annotations.forEach {
                results.addAll(getSymbolsWithAnnotation(it.java.canonicalName))
            }
        }.toList()
}

class HiltBindsKotlinProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        HiltBindsKotlinProcessor(logger = KotlinLogger(environment.logger))
}

private class KotlinLogger(
    private val logger: KSPLogger
) : Logger {

    override fun log(message: String) = logger.logging(message)
    override fun error(message: String) = logger.error(message)
}