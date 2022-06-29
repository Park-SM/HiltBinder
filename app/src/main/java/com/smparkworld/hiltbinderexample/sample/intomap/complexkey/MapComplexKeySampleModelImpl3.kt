package com.smparkworld.hiltbinderexample.sample.intomap.complexkey;

import android.util.Log;

import com.smparkworld.hiltbinder.HiltMapBinds;
import com.smparkworld.hiltbinderexample.sample.intomap.SampleType

import javax.inject.Inject;

@HiltMapBinds
@SampleMapComplexKey(
    key1 = "sample3",
    key2 = MapComplexKeySampleModelImpl3::class,
    key3 = ["s7", "s8", "s9"],
    key4 = [7, 8, 9],
    key5 = SampleType.DEFAULT
)
class MapComplexKeySampleModelImpl3 @Inject constructor(
    private val testString: String
) : MapComplexKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in MapComplexKeySampleModelImpl3 class.");
    }
}