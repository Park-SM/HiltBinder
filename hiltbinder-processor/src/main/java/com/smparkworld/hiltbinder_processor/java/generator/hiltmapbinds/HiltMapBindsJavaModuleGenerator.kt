package com.smparkworld.hiltbinder_processor.java.generator.hiltmapbinds

import com.google.auto.service.AutoService
import com.smparkworld.hiltbinder.HiltMapBinds
import com.smparkworld.hiltbinder_processor.core.Consts
import com.smparkworld.hiltbinder_processor.core.Logger
import com.smparkworld.hiltbinder_processor.core.base.JavaModuleGenerator
import com.smparkworld.hiltbinder_processor.core.base.ParameterMapper
import com.smparkworld.hiltbinder_processor.java.extension.addAnnotationIfNotNull
import com.smparkworld.hiltbinder_processor.java.extension.asClassName
import com.smparkworld.hiltbinder_processor.java.extension.getPackageName
import com.smparkworld.hiltbinder_processor.java.model.HiltMapBindsParamsModel
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
import javax.inject.Named
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import kotlin.reflect.KClass

@AutoService(JavaModuleGenerator::class)
internal class HiltMapBindsJavaModuleGenerator : JavaModuleGenerator {

    private val parameterMapper: ParameterMapper<HiltMapBindsParamsModel> = HiltMapBindsParameterMapper()

    override fun getSupportedAnnotationType(): KClass<out Annotation> = HiltMapBinds::class

    override fun checkValidation(element: Element): Boolean =
        when (element.kind) {
            ElementKind.CLASS, ElementKind.INTERFACE -> true
            else -> false
        }

    override fun generate(env: ProcessingEnvironment, element: Element, annotation: Annotation, logger: Logger) {
        val params = parameterMapper.toParamsModel(env, element, logger)

        val moduleFileName = "${element.simpleName}${Consts.MODULE_SUFFIX}"

        val namedAnnotation = params.namedValue?.let { named ->
            AnnotationSpec.builder(Named::class.java)
                .addMember("value", "\$S", named)
                .build()
        }

        val mapKeyAnnotation = AnnotationSpec.builder(params.mapKey.asClassName(env)).apply {
                for ((key, value) in params.mapKeyParams) {
                    when (value) {
                        is Element -> {
                            when(value.kind) {
                                ElementKind.CLASS,
                                ElementKind.INTERFACE -> {
                                    addMember(key, "\$T.class", value)
                                }
                                ElementKind.ENUM_CONSTANT -> {
                                    addMember(key, "\$T.\$L", value, value.simpleName)
                                }
                                else -> {
                                    // do nothing.
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

        val spec = MethodSpec.methodBuilder("${Consts.FUNCTION_PREFIX}${element.simpleName}")
            .addAnnotation(Binds::class.java)
            .addAnnotation(IntoMap::class.java)
            .addAnnotation(mapKeyAnnotation)
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

        val moduleClazz = TypeSpec.classBuilder(moduleFileName)
            .addAnnotation(Module::class.java)
            .addAnnotation(installInAnnotation)
            .addModifiers(Modifier.ABSTRACT)
            .addMethod(spec)
            .build()

        val javaFile = JavaFile.builder(env.getPackageName(element), moduleClazz)
            .build()

        env.filer.createSourceFile("${env.getPackageName(element)}.${moduleFileName}")
            .openWriter()
            .use { writer -> writer.write(javaFile.toString()) }
    }
}