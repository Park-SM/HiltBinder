package com.smparkworld.hiltbinderexample.sample.basic.named

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import javax.inject.Inject
import javax.inject.Named

@HiltBinds
@Named("model2")
class NamedSampleModelImpl2 @Inject constructor(
   private val testString: String
) : NamedSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in NamedSampleModelImpl2 class.")
    }
}