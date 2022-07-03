package com.smparkworld.hiltbinderexample.sample.basic.qualifier

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import javax.inject.Inject

@HiltBinds
@SampleQualifier1
class QualifierSampleModelImpl1 @Inject constructor(
    private val testString: String
) : QualifierSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in QualifierSampleModelImpl1 class.")
    }
}