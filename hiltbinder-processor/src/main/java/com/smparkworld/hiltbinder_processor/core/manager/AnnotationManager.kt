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

    private fun getAnnotationMirror(element: Element, clazz: Class<*>): AnnotationMirror? {
        for (mirror in element.annotationMirrors) {
            if (mirror.annotationType.toString() == clazz.name) return mirror
        }
        return null
    }

    private fun getAnnotationValue(annotationMirror: AnnotationMirror, key: String): AnnotationValue? {
        for ((k, v) in annotationMirror.elementValues) {
            if (k.simpleName.toString() == key) return v
        }
        return null
    }

    inline fun <reified T : Annotation> getValueFromAnnotation(env: ProcessingEnvironment, element: Element, key: String): Element? {
        val mirror = getAnnotationMirror(element, T::class.java) ?: return null
        return getAnnotationValue(mirror, key)?.let {
            env.typeUtils.asElement(it.value as TypeMirror)
        }
    }
}