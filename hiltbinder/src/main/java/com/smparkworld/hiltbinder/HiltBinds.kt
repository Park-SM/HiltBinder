package com.smparkworld.hiltbinder

import kotlin.reflect.KClass

/***
 * CAUTION.
 * parameter `to` and `from` must not be signed together. Either `to` or `from`
 * must be used. If they are signed at the same time, throws an exception.
 *
 * @throws IllegalStateException thrown when `to` and `from` are signed together.
 *
 * @param to The return type of the Binds abstract function.
 * @param from The argument type of the Binds abstract function.
 * @param qualifier The Qualifier annotation to be applied to the return type.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class HiltBinds(
    val to: KClass<*> = Nothing::class,
    val from: KClass<*> = Nothing::class,
    val qualifier: KClass<*> = Nothing::class
)