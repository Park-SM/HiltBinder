package com.smparkworld.hiltbinder_processor.core.manager

import com.smparkworld.hiltbinder_processor.core.config.ProcessorConfig
import com.smparkworld.hiltbinder_processor.extension.error
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror

import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue

internal object AnnotationManager {

    fun getElementsAnnotatedWith(
        env: ProcessingEnvironment,
        roundEnv: RoundEnvironment,
        perform: (Element, Annotation) -> Unit
    ) : Int {
        var elementCount = 0

        ProcessorConfig.getSupportedAnnotationTypes().forEach { annotationType ->

            roundEnv.getElementsAnnotatedWith(annotationType.java).forEach { element ->

                if (ProcessorConfig.checkSupportedElementType(element.kind)) {
                    perform.invoke(element, element.getAnnotation(annotationType.java))
                    elementCount++
                } else {
                    env.error("HiltBinds processor can only be used with classes and interfaces.")
                }
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

    inline fun <reified T : Annotation> getValueFromAnnotation(element: Element, key: String): TypeMirror? {
        val mirror = getAnnotationMirror(element, T::class.java) ?: return null
        return getAnnotationValue(mirror, key)?.let {
            it.value as TypeMirror
        }
    }
}