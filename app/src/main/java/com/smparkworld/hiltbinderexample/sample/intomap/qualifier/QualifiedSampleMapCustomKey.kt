package com.smparkworld.hiltbinderexample.sample.intomap.qualifier

import com.smparkworld.hiltbinderexample.sample.intomap.SampleKey
import dagger.MapKey

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class QualifiedSampleMapCustomKey(val key: SampleKey)
