package com.smparkworld.hiltbinderexample.sample.intomap.custom

import android.util.Log
import com.smparkworld.hiltbinder.HiltMapBinds
import com.smparkworld.hiltbinderexample.sample.intomap.SampleType
import javax.inject.Inject

@HiltMapBinds
@SampleMapCustomKey(SampleType.SAMPLE2)
class MapCustomKeySampleModelImpl2 @Inject constructor(
    private val testString: String
) : MapCustomKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in MapSampleModelImpl2 class.")
    }
}