package com.smparkworld.hiltbinder_processor.generator.hiltmapbinds

import com.google.auto.service.AutoService
import com.smparkworld.hiltbinder.HiltMapBinds
import com.smparkworld.hiltbinder_processor.core.base.ModuleGenerator
import com.smparkworld.hiltbinder_processor.core.base.ParameterMapper
import com.smparkworld.hiltbinder_processor.extension.addAnnotationIfNotNull
import com.smparkworld.hiltbinder_processor.extension.addImportIfNestedClass
import com.smparkworld.hiltbinder_processor.extension.asClassName
import com.smparkworld.hiltbinder_processor.extension.getPackageName
import com.smparkworld.hiltbinder_processor.model.HiltMapBindsParamsModel
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import kotlin.reflect.KClass

@AutoService(ModuleGenerator::class)
internal class HiltMapBindsModuleGenerator : ModuleGenerator {

    private val parameterMapper: ParameterMapper<HiltMapBindsParamsModel> = HiltMapBindsParameterMapper()

    override fun getSupportedAnnotationType(): KClass<out Annotation> = HiltMapBinds::class

    override fun getSupportedElementTypes(): List<ElementKind> = listOf(
        ElementKind.CLASS, ElementKind.INTERFACE
    )

    override fun generate(env: ProcessingEnvironment, element: Element, annotation: Annotation) {
        val params = parameterMapper.toParamsModel(env, element)

        val moduleFileName = "${element.simpleName}$MODULE_SUFFIX"

        val mapKeyAnnotation = AnnotationSpec.builder(params.keyElement.asClassName(env)).apply {
                for ((key, value) in params.keyElementParams) {
                    when (value) {
                        is Element -> {
                            when(value.kind) {
                                ElementKind.CLASS -> {
                                    addMember(key, "\$T.class", value)
                                }
                                ElementKind.ENUM_CONSTANT -> {
                                    addMember(key, "\$T.\$L", value, value.simpleName)
                                }
                            }
                        }
                        is Number -> {
                            addMember(key, "\$L", value)
                        }
                        is String -> {
                            addMember(key, "\$S", value)
                        }
                        is Array<*> -> {
                            val format = StringBuilder().run {
                                append("{")
                                for (i in value.indices) {
                                    append("\$L")
                                    if (i < value.lastIndex) append(",")
                                }
                                append("}")
                                toString()
                            }
                            addMember(key, format, *value)
                        }
                    }
                }
            }
            .build()

        val spec = MethodSpec.methodBuilder("$FUN_PREFIX${element.simpleName}")
            .addAnnotation(Binds::class.java)
            .addAnnotation(IntoMap::class.java)
            .addAnnotation(mapKeyAnnotation)
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
        private const val MODULE_SUFFIX = "_BindsModule"
        private const val FUN_PREFIX = "bind"
    }
}