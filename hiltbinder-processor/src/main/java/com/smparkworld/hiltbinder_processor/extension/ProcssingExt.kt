package com.smparkworld.hiltbinder_processor.extension

import com.squareup.javapoet.ClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

internal fun ProcessingEnvironment.log(message: String) =
    messager.printMessage(Diagnostic.Kind.NOTE, message)

internal fun ProcessingEnvironment.error(message: String) =
    messager.printMessage(Diagnostic.Kind.ERROR, message)

internal fun ProcessingEnvironment.getClassName(element: Element): ClassName =
    ClassName.get(getPackageName(element), element.simpleName.toString())

internal fun ProcessingEnvironment.getPackageName(element: Element): String =
    elementUtils.getPackageOf(element).toString()