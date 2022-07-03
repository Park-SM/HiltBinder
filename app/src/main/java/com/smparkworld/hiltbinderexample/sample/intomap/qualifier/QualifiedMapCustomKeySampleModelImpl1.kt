package com.smparkworld.hiltbinderexample.sample.intomap.qualifier

import android.util.Log
import com.smparkworld.hiltbinder.HiltMapBinds
import com.smparkworld.hiltbinderexample.sample.intomap.SampleKey
import javax.inject.Inject

@HiltMapBinds
@QualifiedSampleMapCustomKey(SampleKey.KEY1)
@SampleMapQualifier1
class QualifiedMapCustomKeySampleModelImpl1 @Inject constructor(
    private val testString: String
) : QualifiedMapCustomKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in QualifiedMapCustomKeySampleModelImpl1 class.")
    }
}