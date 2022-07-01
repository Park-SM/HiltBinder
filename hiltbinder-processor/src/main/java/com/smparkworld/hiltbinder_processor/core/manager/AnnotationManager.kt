package com.smparkworld.hiltbinder_processor.core.manager

import com.smparkworld.hiltbinder_processor.core.config.ProcessorConfig
import com.smparkworld.hiltbinder_processor.extension.error
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass

internal object AnnotationManager {

    fun getElementsAnnotatedWith(
        roundEnv: RoundEnvironment,
        perform: (Element, Annotation) -> Unit
    ): Int {
        var elementCount = 0

        ProcessorConfig.getSupportedAnnotationTypes().forEach { annotationType ->

            roundEnv.getElementsAnnotatedWith(annotationType.java).forEach { element ->

                perform.invoke(element, element.getAnnotation(annotationType.java))
                elementCount++
            }
        }
        return elementCount
    }

    inline fun <reified T : Annotation> getAnnotationValue(
        env: ProcessingEnvironment,
        element: Element,
        key: String
    ): Element? {
        return getAnnotationMirror(element, T::class.java)?.let { mirror ->
            getAnnotationValue(mirror, key)?.let {
                env.typeUtils.asElement(it.value as TypeMirror)
            }
        }
    }

    inline fun <reified T : Annotation> getAnnotationValues(
        env: ProcessingEnvironment,
        element: Element
    ): Map<String, Any>? {
        return getAnnotationMirror(element, T::class.java)?.let { mirror ->
            getAnnotationValues(env, mirror)
        }
    }

    fun getAnnotationValueBySuffix(
        env: ProcessingEnvironment,
        element: Element,
        parent: KClass<out Annotation>,
        key: String
    ): Element? {
        return getAnnotationMirrorByParentAnnotation(env, element, parent)?.let { mirror ->
            getAnnotationValue(mirror, key)?.let {
                env.typeUtils.asElement(it.value as TypeMirror)
            }
        }
    }

    fun getAnnotationValuesByParentAnnotation(
        env: ProcessingEnvironment,
        element: Element,
        parent: KClass<out Annotation>
    ): Map<String, Any>? {
        return getAnnotationMirrorByParentAnnotation(env, element, parent)?.let { mirror ->
            getAnnotationValues(env, mirror)
        }
    }

    fun getAnnotationByParentAnnotation(
        env: ProcessingEnvironment,
        element: Element,
        parent: KClass<out Annotation>
    ): Element? {
        return getAnnotationMirrorByParentAnnotation(
            env,
            element,
            parent
        )?.annotationType?.asElement()
    }

    private fun getAnnotationMirror(element: Element, clazz: Class<*>): AnnotationMirror? {
        for (mirror in element.annotationMirrors) {
            if (mirror.annotationType.toString() == clazz.name) return mirror
        }
        return null
    }

    private fun getAnnotationMirrorByParentAnnotation(
        env: ProcessingEnvironment,
        element: Element,
        parent: KClass<out Annotation>
    ): AnnotationMirror? {
        var count = 0
        var result: AnnotationMirror? = null

        env.elementUtils.getAllAnnotationMirrors(element).forEach { mirror ->
            mirror.annotationType.asElement().annotationMirrors.forEach { parentMirror ->
                if (parentMirror.annotationType.toString() == parent.qualifiedName) {
                    result = mirror
                    count++
                }
            }
        }
        if (count > 1) {
            env.error(ERROR_MSG_KEY_DUPLICATION)
        }
        return result
    }

    private fun getAnnotationValue(
        annotationMirror: AnnotationMirror,
        key: String
    ): AnnotationValue? {
        for ((k, v) in annotationMirror.elementValues) {
            if (k.simpleName.toString() == key) return v
        }
        return null
    }

    private fun getAnnotationValues(
        env: ProcessingEnvironment,
        annotationMirror: AnnotationMirror
    ): Map<String, Any> {
        val params = mutableMapOf<String, Any>()
        for ((k, v) in annotationMirror.elementValues) {
            when (val value = v.value) {
                is Number,
                is String,
                is Array<*>,
                is Element -> {
                    params[k.simpleName.toString()] = value
                }
                is List<*> -> {
                    params[k.simpleName.toString()] = value.toTypedArray()
                }
                is TypeMirror -> {
                    params[k.simpleName.toString()] = env.typeUtils.asElement(value)
                }
            }
        }
        return params
    }

    private const val ERROR_MSG_KEY_DUPLICATION = "Only one key annotation must be applied."
}