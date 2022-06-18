package com.smparkworld.hiltbinder_processor.generator.hiltbinds

import com.google.auto.service.AutoService
import com.smparkworld.hiltbinder.HiltBinds
import com.smparkworld.hiltbinder_processor.core.base.ModuleGenerator
import com.smparkworld.hiltbinder_processor.core.base.ParameterMapper
import com.smparkworld.hiltbinder_processor.extension.addAnnotationIfNotNull
import com.smparkworld.hiltbinder_processor.extension.addImportIfNestedClass
import com.smparkworld.hiltbinder_processor.extension.asClassName
import com.smparkworld.hiltbinder_processor.extension.getPackageName
import com.smparkworld.hiltbinder_processor.model.HiltBindsParamsModel
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import kotlin.reflect.KClass

@AutoService(ModuleGenerator::class)
internal class HiltBindsModuleGenerator : ModuleGenerator {

    private val parameterMapper: ParameterMapper<HiltBindsParamsModel> = HiltBindsParameterMapper()

    override fun getSupportedAnnotationType(): KClass<out Annotation> = HiltBinds::class

    override fun getSupportedElementTypes(): List<ElementKind> = listOf(
        ElementKind.CLASS, ElementKind.INTERFACE
    )

    override fun generate(env: ProcessingEnvironment, element: Element, annotation: Annotation) {
        val params = parameterMapper.toParamsModel(env, element)

        val moduleFileName = "${element.simpleName}$MODULE_POSTFIX"

        val spec = MethodSpec.methodBuilder("$FUN_PREFIX${element.simpleName}")
            .addAnnotation(Binds::class.java)
            .addAnnotationIfNotNull(env, params.qualifierElement)
            .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
            .addParameter(params.fromElement.asClassName(env), PARAMETER_NAME)
            .returns(params.toElement.asClassName(env))
            .build()

        val installInAnnotation = AnnotationSpec.builder(InstallIn::class.java)
            .addMember(
                "value","\$T.class", params.componentElement ?: SingletonComponent::class.java
            )
            .build()

        val moduleClazz = TypeSpec.classBuilder(moduleFileName)
            .addAnnotation(Module::class.java)
            .addAnnotation(installInAnnotation)
            .addModifiers(Modifier.ABSTRACT)
            .addMethod(spec)
            .build()

        val javaFile = JavaFile.builder(env.getPackageName(element), moduleClazz)
            .addImportIfNestedClass(env, params.toElement)
            .addImportIfNestedClass(env, params.fromElement)
            .build()

        env.filer.createSourceFile("${env.getPackageName(element)}.${moduleFileName}")
            .openWriter()
            .use { writer -> writer.write(javaFile.toString()) }
    }

    companion object {

        private const val PARAMETER_NAME = "target"
        private const val MODULE_POSTFIX = "_BindsModule"
        private const val FUN_PREFIX = "bind"
    }
}