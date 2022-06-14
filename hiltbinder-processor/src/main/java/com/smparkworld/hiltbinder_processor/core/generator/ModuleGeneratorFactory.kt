package com.smparkworld.hiltbinder_processor.core.generator

import java.util.*
import kotlin.reflect.KClass

internal object ModuleGeneratorFactory {

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
        val types = mutableSetOf<KClass<out Annotation>>()

        createModuleGenerators().forEach { generator ->
            types.addAll(generator.getSupportedAnnotationTypes())
        }
        return types
    }
}