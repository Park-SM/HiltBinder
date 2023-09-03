package com.smparkworld.hiltbinder_processor.core

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSDeclaration
import com.smparkworld.hiltbinder_processor.core.base.JavaModuleGenerator
import com.smparkworld.hiltbinder_processor.core.base.KotlinModuleGenerator
import com.smparkworld.hiltbinder_processor.kotlin.extension.isSameType
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

// FixMe Abstract ModuleGenerator class.
internal class ModuleGeneratorDispatcher(
    private val javaModuleGenerators: Set<JavaModuleGenerator> = ModuleGeneratorFactory.createJavaModuleGenerators(),
    private val kotlinModuleGenerators: Set<KotlinModuleGenerator> = ModuleGeneratorFactory.createKotlinModuleGenerators()
) {

    fun dispatchGenerator(
        env: ProcessingEnvironment,
        element: Element,
        annotation: Annotation,
        logger: Logger
    ): Boolean {
        var isGenerated = false

        javaModuleGenerators.forEach { generator ->
            if (!isGenerated && isAppropriateGenerator(generator, element, annotation)) {
                generator.generate(env, element, annotation, logger)
                isGenerated = true
            }
        }
        if (isGenerated) {
            logger.log("$TAG ${element.simpleName}")
        } else {
            logger.error("$TAG ${element.simpleName} annotated with ${annotation.annotationClass.simpleName} are not supported.")
        }
        return isGenerated
    }

    fun dispatchGenerator(
        env: SymbolProcessorEnvironment,
        declaration: KSDeclaration,
        annotation: KSAnnotation,
        logger: Logger
    ): Boolean {
        var isGenerated = false

        kotlinModuleGenerators.forEach { generator ->
            if (!isGenerated && isAppropriateGenerator(generator, declaration, annotation)) {
                generator.generate(env, declaration, annotation, logger)
                isGenerated = true
            }
        }
        if (isGenerated) {
            logger.log("$TAG $declaration")
        } else {
            logger.error("$TAG $declaration annotated with $annotation are not supported.")
        }
        return isGenerated
    }

    private fun isAppropriateGenerator(
        generator: JavaModuleGenerator,
        element: Element,
        annotation: Annotation
    ): Boolean =
        (generator.getSupportedAnnotationType() == annotation.annotationClass) && (generator.checkValidation(element))

    private fun isAppropriateGenerator(
        generator: KotlinModuleGenerator,
        declaration: KSDeclaration,
        annotation: KSAnnotation
    ): Boolean =
        (annotation.isSameType(generator.getSupportedAnnotationType()) && generator.checkValidation(declaration))

    companion object {

        private const val TAG = "HiltBinder: processing:"
    }
}
