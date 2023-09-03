package com.smparkworld.hiltbinder_processor.kotlin.extension

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.ksp.toClassName
import kotlin.reflect.KClass

internal fun KSAnnotation.isSameType(annotationType: KClass<out Annotation>): Boolean =
    (this.annotationType.resolve().toClassName().toString() == annotationType.java.name)

internal fun KSNode.accept(visit: (declaration: KSDeclaration) -> Unit) {
    this.accept(object: KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit): Unit = visit(classDeclaration)
        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit): Unit = visit(property)
        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit): Unit = visit(function)
    }, Unit)
}