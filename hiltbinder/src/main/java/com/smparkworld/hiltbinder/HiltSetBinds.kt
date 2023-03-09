package com.smparkworld.hiltbinder

import kotlin.reflect.KClass

/***
 * `@HiltSetBinds` is used for classes that want to `@Binds` and `@IntoSet`.
 * parameter `to` and `from` must not be signed together. Either `to` or `from`
 * must be used. If they are signed at the same time, throws an exception.
 *
 * @throws IllegalStateException thrown when `to` and `from` are signed together.
 *
 * @param to The return type of the Binds abstract function.
 * @param from The argument type of the Binds abstract function.
 * @param component Specifies in which component the class to be returned will be installed.
 * @param combined Generic type of parent class return format is generated as a star-projections.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class HiltSetBinds(
    val to: KClass<*> = Nothing::class,
    val from: KClass<*> = Nothing::class,
    val component: KClass<*> = Nothing::class,
    val combined: Boolean = false
)
