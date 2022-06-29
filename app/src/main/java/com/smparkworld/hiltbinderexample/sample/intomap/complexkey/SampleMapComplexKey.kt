package com.smparkworld.hiltbinderexample.sample.intomap.complexkey

import com.smparkworld.hiltbinderexample.sample.intomap.SampleType
import dagger.MapKey
import kotlin.reflect.KClass

/***
 * Complex key require the following dependencies:
 *
 *   def autoValueVersion = "1.9"
 *   implementation "com.google.auto.value:auto-value:$autoValueVersion"
 *   implementation "com.google.auto.value:auto-value-annotations:$autoValueVersion"
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MapKey(unwrapValue = false)
annotation class SampleMapComplexKey(
    val key1: String,
    val key2: KClass<*>,
    val key3: Array<String>,
    val key4: IntArray,
    val key5: SampleType
)