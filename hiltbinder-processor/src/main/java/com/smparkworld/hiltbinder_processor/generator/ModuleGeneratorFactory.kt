package com.smparkworld.hiltbinder_processor.generator

object ModuleGeneratorFactory {

    fun createHiltBindsModuleGenerator(): ModuleGenerator {
        return HiltBindsModuleGenerator()
    }
}