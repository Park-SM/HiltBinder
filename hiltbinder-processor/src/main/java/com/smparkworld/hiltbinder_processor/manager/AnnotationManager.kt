package com.smparkworld.hiltbinder_processor.manager

import com.smparkworld.hiltbinder_processor.config.ProcessorConfig
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror

import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue

internal object AnnotationManager {

    fun detectElementsAnnotatedWithAndPerform(
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

    fun getAnnotationMirror(element: Element, clazz: Class<*>): AnnotationMirror? {
        for (mirror in element.annotationMirrors) {
            if (mirror.annotationType.toString() == clazz.name) return mirror
        }
        return null
    }

    fun getAnnotationValue(annotationMirror: AnnotationMirror, key: String): AnnotationValue? {
        for ((k, v) in annotationMirror.elementValues) {
            if (k.simpleName.toString() == key) return v
        }
        return null
    }

    inline fun <reified T : Annotation> getValueFromAnnotation(element: Element, key: String): TypeMirror? {
        val mirror = getAnnotationMirror(element, T::class.java) ?: return null
        return getAnnotationValue(mirror, key)?.let {
            it.value as TypeMirror
        }
    }
}