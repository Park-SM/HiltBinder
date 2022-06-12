package com.smparkworld.hiltbinder_processor.extension

import com.squareup.javapoet.ClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import kotlin.reflect.KClass

fun ProcessingEnvironment.log(message: String) {
    messager.printMessage(Diagnostic.Kind.NOTE, message)
}

fun ProcessingEnvironment.error(message: String) {
    messager.printMessage(Diagnostic.Kind.ERROR, message)
}

fun ProcessingEnvironment.getClassName(element: Element): ClassName =
    ClassName.get(getPackageName(element), element.simpleName.toString())

fun ProcessingEnvironment.getClassName(clazz: Class<*>): ClassName =
    ClassName.get(clazz)

fun ProcessingEnvironment.getSuperClassName(element: Element): ClassName =
    getClassName(getSuperElement(element))

fun ProcessingEnvironment.getSuperElement(element: Element): Element =
    typeUtils.asElement((element as TypeElement).interfaces[0])

fun ProcessingEnvironment.getPackageName(element: Element): String =
    elementUtils.getPackageOf(element).toString()