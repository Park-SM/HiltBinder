package com.smparkworld.hiltbinder_processor

import com.google.auto.service.AutoService
import com.smparkworld.hiltbinder_processor.core.Logger
import com.smparkworld.hiltbinder_processor.core.ModuleGeneratorDispatcher
import com.smparkworld.hiltbinder_processor.core.config.ProcessorConfig
import com.smparkworld.hiltbinder_processor.core.manager.AnnotationManager
import com.smparkworld.hiltbinder_processor.core.manager.PerformanceManager
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Messager
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
internal class HiltBindsJavaProcessor : AbstractProcessor() {

    override fun process(ignored: MutableSet<out TypeElement>?, environment: RoundEnvironment): Boolean {

        val logger = JavaLogger(processingEnv.messager)

        PerformanceManager.startProcessing()

        val processedCount = AnnotationManager.getElementsAnnotatedWith(environment) { element, annotation ->

            ModuleGeneratorDispatcher.dispatchGenerator(processingEnv, element, annotation, logger)
        }

        if (processedCount > 0) {
            PerformanceManager.stopProcessing()
            PerformanceManager.reportPerformance(logger, processedCount)
        }
        return processedCount > 0
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> =
        ProcessorConfig.getSupportedAnnotationTypeNames()

    override fun getSupportedSourceVersion(): SourceVersion =
        ProcessorConfig.getSupportedJavaVersion()
}

private class JavaLogger(
    private val messenger: Messager
) : Logger {

    override fun log(message: String) =
        messenger.printMessage(Diagnostic.Kind.NOTE, message)
    override fun error(message: String) =
        messenger.printMessage(Diagnostic.Kind.ERROR, message)
}