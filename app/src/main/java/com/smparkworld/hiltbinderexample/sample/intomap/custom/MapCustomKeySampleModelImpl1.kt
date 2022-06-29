package com.smparkworld.hiltbinderexample.sample.intomap.custom

import android.util.Log
import com.smparkworld.hiltbinder.HiltMapBinds
import com.smparkworld.hiltbinderexample.sample.intomap.SampleType
import javax.inject.Inject

@HiltMapBinds
@SampleMapCustomKey(SampleType.SAMPLE1)
class MapCustomKeySampleModelImpl1 @Inject constructor(
    private val testString: String
) : MapCustomKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in MapSampleModelImpl1 class.")
    }
}