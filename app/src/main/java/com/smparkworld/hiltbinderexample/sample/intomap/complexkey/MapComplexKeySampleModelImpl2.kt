package com.smparkworld.hiltbinderexample.sample.intomap.complexkey;

import android.util.Log;

import com.smparkworld.hiltbinder.HiltMapBinds;
import com.smparkworld.hiltbinderexample.sample.intomap.SampleType

import javax.inject.Inject;

@HiltMapBinds
@SampleMapComplexKey(
    key1 = "sample2",
    key2 = MapComplexKeySampleModelImpl2::class,
    key3 = ["s4", "s5", "s6"],
    key4 = [4, 5, 6],
    key5 = SampleType.SAMPLE2
)
class MapComplexKeySampleModelImpl2 @Inject constructor(
    private val testString: String
) : MapComplexKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in MapComplexKeySampleModelImpl2 class.");
    }
}