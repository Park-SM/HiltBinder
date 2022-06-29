package com.smparkworld.hiltbinderexample.sample.intomap.custom

import com.smparkworld.hiltbinderexample.sample.intomap.SampleType
import dagger.MapKey

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class SampleMapCustomKey(val key: SampleType)
