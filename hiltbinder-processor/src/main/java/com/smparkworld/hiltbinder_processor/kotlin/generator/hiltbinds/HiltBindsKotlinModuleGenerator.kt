package com.smparkworld.hiltbinder_processor.kotlin.generator.hiltbinds

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.smparkworld.hiltbinder.HiltBindsByKSP
import com.smparkworld.hiltbinder_processor.core.Logger
import com.smparkworld.hiltbinder_processor.core.base.KotlinModuleGenerator
import kotlin.reflect.KClass

//@AutoService(KotlinModuleGenerator::class)
//internal class HiltBindsKotlinModuleGenerator : KotlinModuleGenerator<Any> {
//
//    override fun getSupportedAnnotationType(): KClass<out Annotation> = HiltBindsByKSP::class
//
//    override fun checkValidation(declaration: KSDeclaration): Boolean =
//        (declaration is KSClassDeclaration)
//
//    override fun generate(
//        env: SymbolProcessorEnvironment,
//        declaration: KSDeclaration,
//        annotation: KSAnnotation,
//        logger: Logger
//    ) {
//        val moduleFileName = "${declaration}${Consts.MODULE_SUFFIX}"
//
//        val namedAnnotation = AnnotationSpec.builder(Named::class)
//            .addMember("value", "\$S", "sample")
//            .build()
//
//        val spec = FunSpec.builder("${Consts.FUNCTION_PREFIX}${declaration}")
//            .addAnnotation(Binds::class)
//            .addAnnotation(namedAnnotation)
//            .addModifiers(KModifier.ABSTRACT, KModifier.PUBLIC)
//            .returns(Unit::class)
//            .build()
//
//        val installInAnnotation = AnnotationSpec.builder(InstallIn::class)
//            .addMember("value", "\$T::class", SingletonComponent::class)
//            .build()
//
//        val moduleFile = TypeSpec.classBuilder(moduleFileName)
//            .addAnnotation(Module::class)
//            .addAnnotation(installInAnnotation)
//            .addModifiers(KModifier.ABSTRACT)
//            .addFunction(spec)
//            .build()
//
//        FileSpec.get(declaration.packageName.asString(), moduleFile)
//            .writeTo(codeGenerator = env.codeGenerator, aggregating = false)
//    }
//}
