package com.smparkworld.hiltbinder_processor

import com.google.auto.service.AutoService
import com.smparkworld.hiltbinder_processor.core.config.ProcessorConfig
import com.smparkworld.hiltbinder_processor.extension.error
import com.smparkworld.hiltbinder_processor.core.generator.ModuleGeneratorDispatcher
import com.smparkworld.hiltbinder_processor.core.manager.AnnotationManager
import com.smparkworld.hiltbinder_processor.core.manager.PerformanceManager
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
internal class HiltBindsJavaProcessor : AbstractProcessor() {

    override fun process(ignored: MutableSet<out TypeElement>?, environment: RoundEnvironment): Boolean {

        PerformanceManager.startProcessing()

        val processedCount = AnnotationManager.detectElementsAnnotatedWithAndPerform(environment) { element, annotation ->

            if (!checkSupportedType(element)) {
                processingEnv.error("@HiltBinds Annotation can only use for class and interface.")
                return@detectElementsAnnotatedWithAndPerform
            }

            ModuleGeneratorDispatcher.dispatchGenerator(processingEnv, element, annotation)
        }

        if (processedCount > 0) {
            PerformanceManager.stopProcessing()
            PerformanceManager.reportPerformance(processingEnv)
        }
        return processedCount > 0
    }

    private fun checkSupportedType(element: Element): Boolean =
        ProcessorConfig.checkSupportedElementType(element.kind)

    override fun getSupportedAnnotationTypes(): MutableSet<String> =
        ProcessorConfig.getSupportedAnnotationTypeNames()

    override fun getSupportedSourceVersion(): SourceVersion =
        ProcessorConfig.getSupportedJavaVersion()
}