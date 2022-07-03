package com.smparkworld.hiltbinder

import kotlin.reflect.KClass

/***
 * `@HiltBinds` is used for classes that want to `@Binds`.
 * parameter `to` and `from` must not be signed together. Either `to` or `from`
 * must be used. If they are signed at the same time, throws an exception.
 *
 * @throws IllegalStateException thrown when `to` and `from` are signed together.
 *
 * @param to The return type of the Binds abstract function.
 * @param from The argument type of the Binds abstract function.
 * @param component Specifies in which component the class to be returned will be installed.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class HiltBinds(
    val to: KClass<*> = Nothing::class,
    val from: KClass<*> = Nothing::class,
    val component: KClass<*> = Nothing::class
)