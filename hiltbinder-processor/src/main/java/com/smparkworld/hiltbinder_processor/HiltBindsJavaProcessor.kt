package com.smparkworld.hiltbinder_processor

import com.google.auto.service.AutoService
import com.smparkworld.hiltbinder_processor.config.ProcessorConfig
import com.smparkworld.hiltbinder_processor.extension.error
import com.smparkworld.hiltbinder_processor.generator.ModuleGeneratorDispatcher
import com.smparkworld.hiltbinder_processor.manager.AnnotationManager
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
internal class HiltBindsJavaProcessor : AbstractProcessor() {

    override fun process(ignored: MutableSet<out TypeElement>?, environment: RoundEnvironment): Boolean {

        val count = AnnotationManager.detectElementsAnnotatedWithAndPerform(environment) { element, annotation ->

            if (!checkSupportedType(element)) {
                processingEnv.error("@HiltBinds Annotation can only use for class.")
                return@detectElementsAnnotatedWithAndPerform
            }

            ModuleGeneratorDispatcher.dispatchGenerator(processingEnv, element, annotation)
        }

        return count > 0
    }

    private fun checkSupportedType(element: Element): Boolean =
        element.kind == ElementKind.CLASS || element.kind == ElementKind.INTERFACE

    override fun getSupportedAnnotationTypes(): MutableSet<String> =
        ProcessorConfig.getSupportedAnnotationTypeNames()

    override fun getSupportedSourceVersion(): SourceVersion =
        ProcessorConfig.getSupportedJavaVersion()
}