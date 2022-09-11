package com.smparkworld.hiltbinder_processor.extension

import com.squareup.javapoet.ClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

internal fun ProcessingEnvironment.log(message: String) =
    messager.printMessage(Diagnostic.Kind.NOTE, message)

internal fun ProcessingEnvironment.error(message: String) =
    messager.printMessage(Diagnostic.Kind.ERROR, message)

internal fun ProcessingEnvironment.getPackageName(element: Element): String =
    elementUtils.getPackageOf(element).toString()

internal fun ProcessingEnvironment.getClassName(element: Element): ClassName {
    val nestedClasses = getNestedClassesInternal(element).reversed().toMutableList()
    return if (nestedClasses.isNotEmpty()) {
        val firstClass = nestedClasses.first()
        nestedClasses.removeFirst()
        ClassName.get(getPackageName(element), firstClass, *nestedClasses.toTypedArray())
    } else {
        ClassName.get(getPackageName(element), element.simpleName.toString())
    }
}

private fun getNestedClassesInternal(element: Element, result: MutableList<String>? = null): List<String> {
    val resultInternal = result ?: mutableListOf<String>().apply {
        add(element.simpleName.toString())
    }
    if ((element as? TypeElement)?.nestingKind?.isNested == true) {
        resultInternal.add(element.enclosingElement.simpleName.toString())
        getNestedClassesInternal(element.enclosingElement, resultInternal)
    }
    return resultInternal
}