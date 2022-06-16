package com.smparkworld.hiltbinder_processor.core

import com.smparkworld.hiltbinder_processor.core.base.ModuleGenerator
import com.smparkworld.hiltbinder_processor.extension.log
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

internal object ModuleGeneratorDispatcher {

    private const val TAG = "HiltBinds: processing:"

    private val moduleGenerators: Set<ModuleGenerator> = ModuleGeneratorFactory.createModuleGenerators()

    fun dispatchGenerator(env: ProcessingEnvironment, element: Element, annotation: Annotation) {

        var isGenerated = false

        moduleGenerators.forEach { generator ->
            if (isAppropriateGenerator(generator, element, annotation)) {
                generator.generate(env, element, annotation)
                isGenerated = true
            }
        }

        if (isGenerated) {
            env.log("$TAG ${element.simpleName}")
        } else {
            env.log("$TAG ${element.simpleName} class not supported.")
        }
    }

    private fun isAppropriateGenerator(
        generator: ModuleGenerator,
        element: Element,
        annotation: Annotation
    ): Boolean {
        return (generator.getSupportedAnnotationType() == annotation.annotationClass)
                && (generator.getSupportedElementTypes().contains(element.kind))
    }
}
