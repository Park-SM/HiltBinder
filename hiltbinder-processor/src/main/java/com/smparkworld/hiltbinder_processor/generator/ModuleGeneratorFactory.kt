package com.smparkworld.hiltbinder_processor.generator

internal object ModuleGeneratorFactory {

    fun createHiltBindsModuleGenerator(): ModuleGenerator {
        return HiltBindsModuleGenerator()
    }
}