package com.smparkworld.hiltbinderexample.sample.intomap.named

import android.util.Log
import com.smparkworld.hiltbinder.HiltMapBinds
import com.smparkworld.hiltbinderexample.sample.intomap.SampleKey
import javax.inject.Inject
import javax.inject.Named

@HiltMapBinds
@NamedSampleMapCustomKey(SampleKey.KEY3)
@Named("sampleNamedMap2")
class NamedMapCustomKeySampleModelImpl3 @Inject constructor(
    private val testString: String
) : NamedMapCustomKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in NamedMapCustomKeySampleModelImpl3 class.")
    }
}