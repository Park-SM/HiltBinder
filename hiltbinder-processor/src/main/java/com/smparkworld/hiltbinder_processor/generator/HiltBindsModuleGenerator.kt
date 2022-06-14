package com.smparkworld.hiltbinder_processor.generator

import com.google.auto.service.AutoService
import com.smparkworld.hiltbinder.HiltBinds
import com.smparkworld.hiltbinder_processor.core.generator.ModuleGenerator
import com.smparkworld.hiltbinder_processor.extension.error
import com.smparkworld.hiltbinder_processor.extension.getClassName
import com.smparkworld.hiltbinder_processor.extension.getPackageName
import com.smparkworld.hiltbinder_processor.extension.getSuperClassName
import com.smparkworld.hiltbinder_processor.extension.getSuperInterfaceElement
import com.smparkworld.hiltbinder_processor.core.manager.AnnotationManager
import com.smparkworld.hiltbinder_processor.extension.isNestedClass
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
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

@AutoService(ModuleGenerator::class)
internal class HiltBindsModuleGenerator : ModuleGenerator {

    // TODO: Need to improve readability
    override fun generate(env: ProcessingEnvironment, element: Element, annotation: Annotation) {
        val to = AnnotationManager.getValueFromAnnotation<HiltBinds>(element, "to")?.let {
            env.typeUtils.asElement(it)
        }
        val from = AnnotationManager.getValueFromAnnotation<HiltBinds>(element, "from")?.let {
            env.typeUtils.asElement(it)
        }

        val (fromClazz, toClazz) = when {
            (from != null && to == null) -> {
                (env.getClassName(from) to env.getClassName(element))
            }
            (from == null && to != null) -> {
                (env.getClassName(element) to env.getClassName(to))
            }
            (from == null && to == null) -> {
                (env.getClassName(element) to env.getSuperClassName(element))
            }
            else -> {
                val errorMessage = "`to` and `from` cannot be signed together."
                env.error(errorMessage)
                throw IllegalStateException(errorMessage)
            }
        }

        val moduleFileName = "${element.simpleName}${MODULE_POSTFIX}"

        val spec = MethodSpec.methodBuilder("${FUN_PREFIX}${element.simpleName}")
            .addAnnotation(Binds::class.java)
            .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
            .addParameter(fromClazz, PARAMETER_NAME)
            .returns(toClazz)
            .build()

        val installInAnnotation = AnnotationSpec.builder(InstallIn::class.java)
            .addMember("value","\$T.class", SingletonComponent::class.java)
            .build()

        val moduleClazz = TypeSpec.classBuilder(moduleFileName)
            .addAnnotation(Module::class.java)
            .addAnnotation(installInAnnotation)
            .addModifiers(Modifier.ABSTRACT)
            .addMethod(spec)
            .build()

        val javaFile = JavaFile.builder(env.getPackageName(element), moduleClazz).apply {
                if (from == null && to == null) {
                    addImportIfNestedClass(env, env.getSuperInterfaceElement(element))
                }
            }
            .build()

        env.filer.createSourceFile("${env.getPackageName(element)}.${moduleFileName}")
            .openWriter()
            .use { writer -> writer.write(javaFile.toString()) }
    }

    override fun getSupportedAnnotationTypes(): Set<KClass<out Annotation>> = setOf(
        HiltBinds::class,
    )

    private fun JavaFile.Builder.addImportIfNestedClass(
        env: ProcessingEnvironment,
        element: Element
    ): JavaFile.Builder {
        if (isNestedClass(element)) {
            addStaticImport(env.getClassName((element as TypeElement).enclosingElement), element.simpleName.toString())
        }
        return this
    }

    companion object {
        private const val PARAMETER_NAME = "target"
        private const val MODULE_POSTFIX = "_BindsModule"
        private const val FUN_PREFIX = "bind"
    }
}