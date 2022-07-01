package com.smparkworld.hiltbinder

import kotlin.reflect.KClass

/***
 * `@HiltMapBinds` is used for classes that want to `@Binds` and `@IntoMap`.
 * parameter `to` and `from` must not be signed together. Either `to` or `from`
 * must be used. If they are signed at the same time, throws an exception.
 *
 * And you need to apply the Key annotation with `@MapKey` applied.
 *
 * Also, you can use `@ClassKey`, `@StringKey`, `@LongKey`, `@IntKey` provided by Hilt.
 *
 * @throws IllegalStateException thrown when `to` and `from` are signed together.
 * @throws IllegalStateException thrown when the key annotation with `Key` suffix is not found.
 * @throws IllegalArgumentException thrown when the key annotation has no value.
 *
 * @param to The return type of the Binds abstract function.
 * @param from The argument type of the Binds abstract function.
 * @param qualifier The Qualifier annotation to be applied to the return type.
 * @param component Specifies in which component the class to be returned will be installed.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class HiltMapBinds(
    val to: KClass<*> = Nothing::class,
    val from: KClass<*> = Nothing::class,
    val qualifier: KClass<*> = Nothing::class,
    val component: KClass<*> = Nothing::class
)