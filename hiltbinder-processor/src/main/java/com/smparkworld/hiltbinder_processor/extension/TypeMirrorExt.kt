package com.smparkworld.hiltbinder_processor.extension

import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import javax.lang.model.type.WildcardType

internal fun TypeMirror.asElement(env: ProcessingEnvironment): Element =
    env.typeUtils.asElement(this)

internal fun TypeMirror.getGenericTypeNames(env: ProcessingEnvironment): TypeName =
    findGenericTypesInternal(env, this) ?: env.typeUtils.asElement(this).asClassName(env)

private fun findGenericTypesInternal(env: ProcessingEnvironment, type: TypeMirror): TypeName? {
    if (type is DeclaredType) {
        val members = type.typeArguments.mapNotNull {
            findGenericTypesInternal(env, it)
        }
        if (members.isNotEmpty()) {
            return ParameterizedTypeName.get(
                type.asElement().asClassName(env),
                *members.toTypedArray()
            )
        }
    }
    return resolveWildCardType(type)
}

private fun resolveWildCardType(type: TypeMirror): TypeName {
    return TypeName.get(
        if (type is WildcardType) type.extendsBound else type
    )
}