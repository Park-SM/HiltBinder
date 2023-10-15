package com.smparkworld.hiltbinder_processor.kotlin.extension

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSClassifierReference
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.smparkworld.hiltbinder_processor.core.Logger
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.ksp.toClassName
import kotlin.reflect.KClass

internal fun KSAnnotation.isSameType(annotationType: KClass<out Annotation>): Boolean =
    (this.shortName.asString() == annotationType.java.simpleName)

internal fun KSNode.accept(visit: (declaration: KSDeclaration) -> Unit) {
    this.accept(object: KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit): Unit = visit(classDeclaration)
        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit): Unit = visit(property)
        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit): Unit = visit(function)
    }, Unit)
}

internal fun KSDeclaration.findSuperClassDeclaration(parent: ClassName? = null): KSType? {
    val superTypes = (this as? KSClassDeclaration)?.superTypes ?: return null
    val foundTargetSuperType = superTypes.find { superType ->
        (superType.element as? KSClassifierReference)?.referencedName() == parent?.simpleName
    }
    return foundTargetSuperType?.resolve()
        ?: superTypes.iterator().takeIf(Iterator<KSTypeReference>::hasNext)
            ?.next()
            ?.resolve()
}

internal fun KSType.getGenericTypeName(isCombined: Boolean? = null, logger: Logger? = null): TypeName {
    return findGenericTypesInternal(this, isCombined ?: false, logger)
}

private fun findGenericTypesInternal(
    type: KSType,
    isCombined: Boolean,
    logger: Logger?
): TypeName {

    val members = type.arguments
        .mapNotNull { it.type?.resolve() }
        .map { findGenericTypesInternal(it, isCombined, logger) }

    if (members.isNotEmpty()) {
        val combinedMembers: Array<TypeName> = if (isCombined) {
            Array(members.size) { WildcardTypeName.producerOf(Any::class) }
        } else {
            members.toTypedArray()
        }
        return type.toClassName()
            .parameterizedBy(*combinedMembers)
    }
    return type.toClassName()
}

internal fun KSDeclaration.findAnnotation(annotation: KClass<out Annotation>): KSAnnotation? {
    return this.annotations.find { it.isSameType(annotation) }
}

internal fun KSDeclaration.findAnnotationByParent(
    target: KClass<out Annotation>,
    ignores: List<KClass<out Annotation>> = emptyList()
): KSAnnotation? {
    return this.annotations.associate { (it to it.annotationType.resolve().declaration.annotations) }
        .filter {
            it.value.any { annotation ->
                annotation.isSameType(target)
            }
        }
        .keys
        .firstOrNull { candidate ->
            ignores.all { ignore ->
                !candidate.isSameType(ignore)
            }
        }
}

internal fun KSAnnotation.findClassFromArguments(argument: String): ClassName? {
    val targetArgument = this.arguments.find { it.name?.asString() == argument }
    return (targetArgument?.value as? KSType)
        ?.takeIf { it.declaration.qualifiedName?.asString() != Void::class.qualifiedName }
        ?.toClassName()
}

@Suppress("UNCHECKED_CAST")
internal fun <T> KSAnnotation.findValueFromArguments(argument: String): T? {
    return this.arguments.find { it.name?.asString() == argument }?.value as? T
}