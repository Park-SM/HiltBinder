package com.smparkworld.hiltbinder_processor.extension

import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.WildcardTypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import javax.lang.model.type.WildcardType

internal fun TypeMirror.asElement(env: ProcessingEnvironment): Element =
    env.typeUtils.asElement(this)

internal fun TypeMirror.getGenericTypeNames(env: ProcessingEnvironment, isCombined: Boolean? = null): TypeName =
    findGenericTypesInternal(env, this, isCombined ?: false)
        ?: env.typeUtils.asElement(this).asClassName(env)

private fun findGenericTypesInternal(
    env: ProcessingEnvironment,
    type: TypeMirror,
    isCombined: Boolean
): TypeName? {
    if (type is DeclaredType) {
        val members = type.typeArguments.mapNotNull {
            findGenericTypesInternal(env, it, isCombined)
        }
        if (members.isNotEmpty()) {
            val combinedMembers: Array<TypeName> = if (isCombined) {
                Array(members.size) { WildcardTypeName.subtypeOf(Object::class.java) }
            } else {
                members.toTypedArray()
            }
            return ParameterizedTypeName.get(
                type.asElement().asClassName(env),
                *combinedMembers
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