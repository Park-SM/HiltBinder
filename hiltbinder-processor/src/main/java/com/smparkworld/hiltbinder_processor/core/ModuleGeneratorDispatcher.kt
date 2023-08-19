package com.smparkworld.hiltbinder_processor.core

import com.smparkworld.hiltbinder_processor.core.base.ModuleGenerator
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

internal object ModuleGeneratorDispatcher {

    private const val TAG = "HiltBinder: processing:"

    private val moduleGenerators: Set<ModuleGenerator> = ModuleGeneratorFactory.createModuleGenerators()

    fun dispatchGenerator(
        env: ProcessingEnvironment,
        element: Element,
        annotation: Annotation,
        logger: Logger
    ) {

        var isGenerated = false

        moduleGenerators.forEach { generator ->
            if (!isGenerated && isAppropriateGenerator(generator, element, annotation)) {
                generator.generate(env, element, annotation, logger)
                isGenerated = true
            }
        }

        if (isGenerated) {
            logger.log("$TAG ${element.simpleName}")
        } else {
            logger.error("$TAG ${element.simpleName} annotated with ${annotation.javaClass.name} are not supported.")
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
