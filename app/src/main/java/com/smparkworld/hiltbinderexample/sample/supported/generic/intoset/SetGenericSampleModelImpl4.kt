package com.smparkworld.hiltbinderexample.sample.supported.generic.intoset

import android.util.Log
import com.smparkworld.hiltbinder.HiltSetBinds
import javax.inject.Inject

@HiltSetBinds
class SetGenericSampleModelImpl4 @Inject constructor(
    private val testString: String
) : SetGenericSampleModel<String> {

    override fun printTestString(data: String) {
        Log.d("Test!!", "TestString is `$testString` in SetGenericSampleModelImpl4 class. :: Generic type is <String>")
    }
}