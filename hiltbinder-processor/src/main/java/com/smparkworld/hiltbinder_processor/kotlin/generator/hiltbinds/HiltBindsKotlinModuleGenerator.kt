package com.smparkworld.hiltbinder_processor.kotlin.generator.hiltbinds

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.smparkworld.hiltbinder.HiltBinds
import com.smparkworld.hiltbinder_processor.core.Consts
import com.smparkworld.hiltbinder_processor.core.Logger
import com.smparkworld.hiltbinder_processor.core.base.KotlinModuleGenerator
import com.smparkworld.hiltbinder_processor.kotlin.extension.addAnnotationIfNotNull
import com.smparkworld.hiltbinder_processor.kotlin.model.HiltBindsKotlinParamsModel
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import javax.inject.Named
import kotlin.reflect.KClass

@AutoService(KotlinModuleGenerator::class)
internal class HiltBindsKotlinModuleGenerator : KotlinModuleGenerator<HiltBindsKotlinParamsModel>() {

    override fun getSupportedAnnotationType(): KClass<out Annotation> = HiltBinds::class

    override fun checkValidation(declaration: KSDeclaration): Boolean =
        (declaration is KSClassDeclaration)

    override fun generate(env: SymbolProcessorEnvironment, params: HiltBindsKotlinParamsModel, logger: Logger) {
        val namedAnnotation = params.namedValue?.let { named ->
            AnnotationSpec.builder(Named::class)
                .addMember("value = %S", named)
                .build()
        }

        val spec = FunSpec.builder("${Consts.FUNCTION_PREFIX}${params.declarationName}")
            .addAnnotation(Binds::class)
            .addAnnotationIfNotNull(params.scope)
            .addAnnotationIfNotNull(params.qualifier)
            .addAnnotationIfNotNull(namedAnnotation)
            .addModifiers(KModifier.ABSTRACT, KModifier.PUBLIC)
            .addParameter(Consts.PARAMETER_NAME, params.from)
            .returns(params.to)
            .build()

        val installInAnnotation = AnnotationSpec.builder(InstallIn::class)
            .addMember("%T::class", params.component)
            .build()

        val moduleFile = TypeSpec.classBuilder(params.moduleFileName)
            .addAnnotation(Module::class)
            .addAnnotation(installInAnnotation)
            .addModifiers(KModifier.ABSTRACT)
            .addFunction(spec)
            .build()

        FileSpec.get(params.declarationPackageName, moduleFile)
            .writeTo(codeGenerator = env.codeGenerator, aggregating = false)
    }
}
