package com.smparkworld.hiltbinderexample.sample.intoset.named

import android.util.Log
import com.smparkworld.hiltbinder.HiltSetBinds
import javax.inject.Inject
import javax.inject.Named

@HiltSetBinds
@Named("sampleNamedSet1")
class NamedSetSampleModelImpl1 @Inject constructor(
    private val testString: String
) : NamedSetSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in NamedSetSampleModelImpl1 class.")
    }
}