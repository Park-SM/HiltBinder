package com.smparkworld.hiltbinder_processor.extension

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.MethodSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

internal fun MethodSpec.Builder.addAnnotationIfNotNull(
    env: ProcessingEnvironment,
    element: Element?
): MethodSpec.Builder {
    if (element != null) {
        addAnnotation(element.asClassName(env))
    }
    return this
}

internal fun MethodSpec.Builder.addAnnotationIfNotNull(
    spec: AnnotationSpec?
): MethodSpec.Builder {
    if (spec != null) {
        addAnnotation(spec)
    }
    return this
}