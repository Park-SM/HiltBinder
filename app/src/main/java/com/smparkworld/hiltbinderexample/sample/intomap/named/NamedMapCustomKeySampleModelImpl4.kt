package com.smparkworld.hiltbinderexample.sample.intomap.named

import android.util.Log
import com.smparkworld.hiltbinder.HiltMapBinds
import com.smparkworld.hiltbinderexample.sample.intomap.SampleKey
import javax.inject.Inject
import javax.inject.Named

@HiltMapBinds
@NamedSampleMapCustomKey(SampleKey.KEY4)
@Named("sampleNamedMapB")
class NamedMapCustomKeySampleModelImpl4 @Inject constructor(
    private val testString: String
) : NamedMapCustomKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in NamedMapCustomKeySampleModelImpl4 class.")
    }
}