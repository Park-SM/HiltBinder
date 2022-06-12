package com.smparkworld.hiltbinder_processor.generator

import com.smparkworld.hiltbinder_processor.extension.log
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

internal object ModuleGeneratorDispatcher {

    private val hiltBindsGenerator: ModuleGenerator = ModuleGeneratorFactory.createHiltBindsModuleGenerator()

    fun dispatchGenerator(env: ProcessingEnvironment, element: Element, annotation: Annotation) {

        env.log("Processing: ${element.simpleName}")
        when {
            hiltBindsGenerator.getSupportedAnnotation().contains(annotation.annotationClass) -> {
                hiltBindsGenerator.generate(env, element, annotation)
            }
            else -> {
                env.log("${element.simpleName} class not supported.")
            }
        }
    }
}