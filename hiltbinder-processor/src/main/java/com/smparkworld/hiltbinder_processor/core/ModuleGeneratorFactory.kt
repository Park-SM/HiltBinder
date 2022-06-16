package com.smparkworld.hiltbinder_processor.core

import com.smparkworld.hiltbinder_processor.core.base.ModuleGenerator
import java.util.*
import kotlin.reflect.KClass

internal object ModuleGeneratorFactory {

    private var cachedAnnotationTypes: Set<KClass<out Annotation>>? = null

    fun createModuleGenerators(): Set<ModuleGenerator> {
        val generators = mutableSetOf<ModuleGenerator>()

        val iterator = ServiceLoader.load(
            ModuleGenerator::class.java,
            ModuleGenerator::class.java.classLoader
        ).iterator()

        for (generator in iterator) {
            generators.add(generator)
        }
        return generators
    }

    fun getSupportedAnnotationTypes(): Set<KClass<out Annotation>> {
        return cachedAnnotationTypes ?: mutableSetOf<KClass<out Annotation>>().also { types ->

            createModuleGenerators().forEach { generator ->
                types.add(generator.getSupportedAnnotationType())
            }
            cachedAnnotationTypes = types
        }
    }
}