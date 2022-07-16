package com.smparkworld.hiltbinderexample.sample.intomap.qualifier

import android.util.Log
import com.smparkworld.hiltbinder.HiltMapBinds
import com.smparkworld.hiltbinderexample.sample.intomap.SampleKey
import javax.inject.Inject

@HiltMapBinds
@QualifiedSampleMapCustomKey(SampleKey.KEY2)
@SampleMapQualifierA
class QualifiedMapCustomKeySampleModelImpl2 @Inject constructor(
    private val testString: String
) : QualifiedMapCustomKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in QualifiedMapCustomKeySampleModelImpl2 class.")
    }
}