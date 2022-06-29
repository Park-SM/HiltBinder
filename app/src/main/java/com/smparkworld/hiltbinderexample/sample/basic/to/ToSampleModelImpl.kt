package com.smparkworld.hiltbinderexample.sample.basic.to

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import javax.inject.Inject

@HiltBinds(to = ToSampleModel::class)
class ToSampleModelImpl @Inject constructor(
    private val testString: String
) : ToSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in ToSampleModelImpl class.")
    }
}