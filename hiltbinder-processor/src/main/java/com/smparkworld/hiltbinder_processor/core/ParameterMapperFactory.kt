package com.smparkworld.hiltbinder_processor.core

import com.smparkworld.hiltbinder_processor.core.base.JavaParameterMapper
import com.smparkworld.hiltbinder_processor.core.base.KotlinParameterMapper
import java.util.ServiceLoader

internal object ParameterMapperFactory {

    fun createJavaParameterMappers(): Set<JavaParameterMapper<*>> =
        createModuleGeneratorInternal()

    fun createKotlinParameterMappers(): Set<KotlinParameterMapper<*>> =
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