package com.smparkworld.hiltbinderexample.sample.intomap.complexkey;

import android.util.Log;

import com.smparkworld.hiltbinder.HiltMapBinds;
import com.smparkworld.hiltbinderexample.sample.intomap.SampleType

import javax.inject.Inject;

@HiltMapBinds
@SampleMapComplexKey(
    key1 = "sample1",
    key2 = MapComplexKeySampleModelImpl1::class,
    key3 = ["s1", "s2", "s3"],
    key4 = [1, 2, 3],
    key5 = SampleType.SAMPLE1
)
class MapComplexKeySampleModelImpl1 @Inject constructor(
    private val testString: String
) : MapComplexKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in MapComplexKeySampleModelImpl1 class.");
    }
}