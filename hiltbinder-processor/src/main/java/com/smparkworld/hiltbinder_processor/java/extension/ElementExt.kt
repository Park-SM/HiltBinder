package com.smparkworld.hiltbinder_processor.java.extension

import com.smparkworld.hiltbinder_processor.core.Utils
import com.squareup.javapoet.ClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

internal fun Element.asClassName(env: ProcessingEnvironment): ClassName =
    env.getClassName(this)

internal fun Element.getSuperTypeMirror(): TypeMirror? {
    val superClass = (this as? TypeElement)?.superclass
    if (superClass != null && !Utils.isObjectClass(superClass)) {
        return superClass
    }
    return (this as? TypeElement)?.interfaces?.getOrNull(0)
}

internal fun Element.findSuperTypeMirror(target: Element): TypeMirror? {
    val superClass = (this as? TypeElement)?.superclass
    if (superClass != null && !Utils.isObjectClass(superClass) && Utils.isSameClass(superClass, target.asType())) {
        return superClass
    }
    return (this as? TypeElement)?.interfaces.orEmpty().find { interfaceClass ->
        Utils.isSameClass(interfaceClass, target.asType())
    }
}