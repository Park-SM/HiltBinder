package com.smparkworld.hiltbinder_processor.core

import com.squareup.javapoet.ClassName
import javax.lang.model.type.TypeMirror

internal object Utils {

    fun isObjectClass(type: TypeMirror): Boolean =
        type.toString() == ClassName.get(Object::class.java).canonicalName()

    fun isSameType(origin: TypeMirror, target: TypeMirror): Boolean {
        val originString = origin.toString()
        val targetString = target.toString()
        return originString == targetString
    }

    fun isSameClass(origin: TypeMirror, target: TypeMirror): Boolean {
        val originString = origin.toString().removeGeneric()
        val targetString = target.toString().removeGeneric()
        return originString == targetString
    }

    private fun String.removeGeneric(): String =
        if (this.contains("<")) substring(0, indexOf("<")) else this
}
