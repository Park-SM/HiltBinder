package com.smparkworld.hiltbinder_processor.core.manager

import com.smparkworld.hiltbinder_processor.core.config.ProcessorConfig
import com.smparkworld.hiltbinder_processor.extension.error
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror

internal object AnnotationManager {

    fun getElementsAnnotatedWith(
        roundEnv: RoundEnvironment,
        perform: (Element, Annotation) -> Unit
    ) : Int {
        var elementCount = 0

        ProcessorConfig.getSupportedAnnotationTypes().forEach { annotationType ->

            roundEnv.getElementsAnnotatedWith(annotationType.java).forEach { element ->

                perform.invoke(element, element.getAnnotation(annotationType.java))
                elementCount++
            }
        }
        return elementCount
    }

    inline fun <reified T : Annotation> getAnnotationValue(env: ProcessingEnvironment, element: Element, key: String): Element? {
        return getAnnotationMirror(element, T::class.java)?.let { mirror ->
            getAnnotationValue(mirror, key)?.let {
                env.typeUtils.asElement(it.value as TypeMirror)
            }
        }
    }

    inline fun <reified T : Annotation> getAnnotationValues(env: ProcessingEnvironment, element: Element): Map<String, Any>? {
        return getAnnotationMirror(element, T::class.java)?.let { mirror ->
            getAnnotationValues(env, mirror)
        }
    }

    fun getAnnotationValueBySuffix(env: ProcessingEnvironment, element: Element, suffix: String, key: String): Element? {
        return getAnnotationMirrorBySuffix(env, element, suffix)?.let { mirror ->
            getAnnotationValue(mirror, key)?.let {
                env.typeUtils.asElement(it.value as TypeMirror)
            }
        }
    }

    fun getAnnotationValuesBySuffix(env: ProcessingEnvironment, element: Element, suffix: String): Map<String, Any>? {
        return getAnnotationMirrorBySuffix(env, element, suffix)?.let { mirror ->
            getAnnotationValues(env, mirror)
        }
    }

    fun getAnnotationBySuffix(env: ProcessingEnvironment, element: Element, suffix: String): Element? {
        return getAnnotationMirrorBySuffix(env, element, suffix)?.annotationType?.asElement()
    }

    private fun getAnnotationMirror(element: Element, clazz: Class<*>): AnnotationMirror? {
        for (mirror in element.annotationMirrors) {
            if (mirror.annotationType.toString() == clazz.name) return mirror
        }
        return null
    }

    private fun getAnnotationMirrorBySuffix(env: ProcessingEnvironment, element: Element, suffix: String): AnnotationMirror? {
        var count = 0
        var result: AnnotationMirror? = null

        env.elementUtils.getAllAnnotationMirrors(element).forEach { mirror ->
            if (mirror.annotationType.toString().endsWith(suffix, true)) {
                result = mirror
                count++
            }
        }
        if (count > 1) {
            env.error(ERROR_MSG_SUFFIX_KEY_DUPLICATION)
        }
        return result
    }

    private fun getAnnotationValue(annotationMirror: AnnotationMirror, key: String): AnnotationValue? {
        for ((k, v) in annotationMirror.elementValues) {
            if (k.simpleName.toString() == key) return v
        }
        return null
    }

    private fun getAnnotationValues(env: ProcessingEnvironment, annotationMirror: AnnotationMirror): Map<String, Any> {
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

    private const val ERROR_MSG_SUFFIX_KEY_DUPLICATION = "There must be only one comment that corresponds to a special suffix."
}