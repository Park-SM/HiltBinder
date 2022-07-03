package com.smparkworld.hiltbinder_processor.extension

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

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

internal fun JavaFile.Builder.addImportIfNestedClass(
    env: ProcessingEnvironment,
    element: Element
): JavaFile.Builder {
    if (isNestedClass(element)) {
        addStaticImport(env.getClassName((element as TypeElement).enclosingElement), element.simpleName.toString())
    }
    return this
}

private fun isNestedClass(element: Element): Boolean =
    (element as TypeElement).enclosingElement.kind != ElementKind.PACKAGE