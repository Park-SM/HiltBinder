package com.smparkworld.hiltbinderexample.sample.intomap.named

import com.smparkworld.hiltbinderexample.sample.intomap.SampleKey
import dagger.MapKey

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class NamedSampleMapCustomKey(val key: SampleKey)
