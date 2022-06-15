package com.smparkworld.hiltbinder_processor.core.generator

import com.smparkworld.hiltbinder_processor.extension.log
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

internal object ModuleGeneratorDispatcher {

    private const val TAG = "HiltBinds: processing:"

    private val moduleGenerators: Set<ModuleGenerator> =
        ModuleGeneratorFactory.createModuleGenerators()

    fun dispatchGenerator(env: ProcessingEnvironment, element: Element, annotation: Annotation) {

        var isGenerated = false

        env.log("$TAG ${element.simpleName}")
        moduleGenerators.forEach { generator ->
            if (generator.getSupportedAnnotationTypes().contains(annotation.annotationClass)) {
                generator.generate(env, element, annotation)
                isGenerated = true
            }
        }
        if (!isGenerated) {
            env.log("$TAG ${element.simpleName} class not supported.")
        }
    }
}
