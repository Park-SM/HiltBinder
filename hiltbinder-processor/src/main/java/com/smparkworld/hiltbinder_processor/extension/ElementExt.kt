package com.smparkworld.hiltbinder_processor.extension

import com.squareup.javapoet.ClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

internal fun Element.asClassName(env: ProcessingEnvironment): ClassName =
    env.getClassName(this)

internal fun Element.getGenericTypes(env: ProcessingEnvironment): List<Element> {
    return (this as TypeElement).interfaces[0].let { type ->
        (type as DeclaredType).typeArguments.map {
            env.typeUtils.asElement(it)
        }
    }
}