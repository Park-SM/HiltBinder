package com.smparkworld.hiltbinder_processor.java.generator.hiltbinds

import com.google.auto.service.AutoService
import com.smparkworld.hiltbinder.HiltBinds
import com.smparkworld.hiltbinder_processor.core.Consts
import com.smparkworld.hiltbinder_processor.core.Logger
import com.smparkworld.hiltbinder_processor.core.base.JavaModuleGenerator
import com.smparkworld.hiltbinder_processor.java.extension.addAnnotationIfNotNull
import com.smparkworld.hiltbinder_processor.java.extension.getPackageName
import com.smparkworld.hiltbinder_processor.java.model.HiltBindsJavaParamsModel
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.annotation.processing.ProcessingEnvironment
import javax.inject.Named
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import kotlin.reflect.KClass

@AutoService(JavaModuleGenerator::class)
internal class HiltBindsJavaModuleGenerator : JavaModuleGenerator<HiltBindsJavaParamsModel>() {

    override fun getSupportedAnnotationType(): KClass<out Annotation> = HiltBinds::class

    override fun checkValidation(element: Element): Boolean =
        when (element.kind) {
            ElementKind.CLASS, ElementKind.INTERFACE -> true
            else -> false
        }

    override fun generate(env: ProcessingEnvironment, params: HiltBindsJavaParamsModel, logger: Logger) {
        val namedAnnotation = params.namedValue?.let { named ->
            AnnotationSpec.builder(Named::class.java)
                .addMember("value", "\$S", named)
                .build()
        }

        val spec = MethodSpec.methodBuilder("${Consts.FUNCTION_PREFIX}${params.elementName}")
            .addAnnotation(Binds::class.java)
            .addAnnotationIfNotNull(env, params.scope)
            .addAnnotationIfNotNull(env, params.qualifier)
            .addAnnotationIfNotNull(namedAnnotation)
            .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
            .addParameter(params.from, Consts.PARAMETER_NAME)
            .returns(params.to)
            .build()

        val installInAnnotation = AnnotationSpec.builder(InstallIn::class.java)
            .addMember(
                "value","\$T.class", params.component ?: SingletonComponent::class.java
            )
            .build()

        val moduleClazz = TypeSpec.classBuilder(params.moduleFileName)
            .addAnnotation(Module::class.java)
            .addAnnotation(installInAnnotation)
            .addModifiers(Modifier.ABSTRACT)
            .addMethod(spec)
            .build()

        val javaFile = JavaFile.builder(params.elementPackageName, moduleClazz)
            .build()

        env.filer.createSourceFile("${params.elementPackageName}.${params.moduleFileName}")
            .openWriter()
            .use { writer -> writer.write(javaFile.toString()) }
    }
}