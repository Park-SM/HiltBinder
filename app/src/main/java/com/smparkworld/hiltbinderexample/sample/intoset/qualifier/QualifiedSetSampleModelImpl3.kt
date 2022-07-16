package com.smparkworld.hiltbinderexample.sample.intoset.qualifier

import android.util.Log
import com.smparkworld.hiltbinder.HiltSetBinds
import javax.inject.Inject

@HiltSetBinds
@SampleSetQualifierB
class QualifiedSetSampleModelImpl3 @Inject constructor(
    private val testString: String
) : QualifiedSetSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in QualifiedSetSampleModelImpl3 class.")
    }
}