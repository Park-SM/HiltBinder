package com.smparkworld.hiltbinder_processor.kotlin.extension

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.toAnnotationSpec
import com.squareup.kotlinpoet.ksp.toClassName
import kotlin.reflect.KClass

internal fun KSDeclaration.asClassName(): ClassName =
    (this as KSClassDeclaration).toClassName()

internal fun FunSpec.Builder.addAnnotationIfNotNull(clazz: KClass<*>?): FunSpec.Builder {
    if (clazz != null) this.addAnnotation(clazz)
    return this
}

internal fun FunSpec.Builder.addAnnotationIfNotNull(annotation: KSAnnotation?): FunSpec.Builder {
    if (annotation != null) this.addAnnotation(annotation.toAnnotationSpec())
    return this
}

internal fun FunSpec.Builder.addAnnotationIfNotNull(spec: AnnotationSpec?): FunSpec.Builder {
    if (spec != null) this.addAnnotation(spec)
    return this
}