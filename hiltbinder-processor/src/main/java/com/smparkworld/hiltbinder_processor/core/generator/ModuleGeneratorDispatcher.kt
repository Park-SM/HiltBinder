package com.smparkworld.hiltbinder_processor.core.generator

import com.smparkworld.hiltbinder_processor.extension.log
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

internal object ModuleGeneratorDispatcher {

    private val moduleGenerators: Set<ModuleGenerator> =
        ModuleGeneratorFactory.createModuleGenerators()

    fun dispatchGenerator(env: ProcessingEnvironment, element: Element, annotation: Annotation) {

        var isGenerated = false

        env.log("processing ${element.simpleName} class")
        moduleGenerators.forEach { generator ->
            if (generator.getSupportedAnnotationTypes().contains(annotation.annotationClass)) {
                generator.generate(env, element, annotation)
                isGenerated = true
            }
        }
        if (!isGenerated) {
            env.log("${element.simpleName} class not supported.")
        }
    }
}
