package com.smparkworld.hiltbinder_processor.extension

import com.squareup.javapoet.ClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

private const val TAG = "HiltBinds"

internal fun ProcessingEnvironment.log(message: String) =
    messager.printMessage(Diagnostic.Kind.NOTE, "$TAG: $message")

internal fun ProcessingEnvironment.error(message: String) =
    messager.printMessage(Diagnostic.Kind.ERROR, message)

internal fun ProcessingEnvironment.getClassName(element: Element): ClassName =
    ClassName.get(getPackageName(element), element.simpleName.toString())

internal fun ProcessingEnvironment.getSuperClassName(element: Element): ClassName =
    getClassName(getSuperInterfaceElement(element))

internal fun ProcessingEnvironment.getSuperInterfaceElement(element: Element): Element =
    typeUtils.asElement((element as TypeElement).interfaces[0])

internal fun ProcessingEnvironment.getPackageName(element: Element): String =
    elementUtils.getPackageOf(element).toString()

internal fun isNestedClass(element: Element): Boolean =
    (element as TypeElement).enclosingElement.kind != ElementKind.PACKAGE
