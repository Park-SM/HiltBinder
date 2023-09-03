package com.smparkworld.hiltbinder_processor.core

import com.smparkworld.hiltbinder_processor.core.base.JavaModuleGenerator
import com.smparkworld.hiltbinder_processor.core.base.KotlinModuleGenerator
import java.util.ServiceLoader

internal object ModuleGeneratorFactory {

    fun createJavaModuleGenerators(): Set<JavaModuleGenerator> =
        createModuleGeneratorInternal()

    fun createKotlinModuleGenerators(): Set<KotlinModuleGenerator> =
        createModuleGeneratorInternal()

    private inline fun <reified T> createModuleGeneratorInternal(): Set<T> {
        val generators = mutableSetOf<T>()

        val iterator = ServiceLoader.load(
            T::class.java,
            T::class.java.classLoader
        ).iterator()

        for (generator in iterator) {
            generators.add(generator)
        }
        return generators
    }
}