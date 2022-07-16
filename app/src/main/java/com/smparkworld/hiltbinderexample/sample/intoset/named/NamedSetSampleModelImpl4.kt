package com.smparkworld.hiltbinderexample.sample.intoset.named

import android.util.Log
import com.smparkworld.hiltbinder.HiltSetBinds
import javax.inject.Inject
import javax.inject.Named

@HiltSetBinds
@Named("sampleNamedSetB")
class NamedSetSampleModelImpl4 @Inject constructor(
    private val testString: String
) : NamedSetSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in NamedSetSampleModelImpl4 class.")
    }
}