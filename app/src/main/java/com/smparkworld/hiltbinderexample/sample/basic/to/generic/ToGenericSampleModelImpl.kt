package com.smparkworld.hiltbinderexample.sample.basic.to.generic

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import com.smparkworld.hiltbinderexample.sample.basic.to.BaseToSampleModel
import javax.inject.Inject

@HiltBinds(to = ToGenericSampleModel::class)
class ToGenericSampleModelImpl @Inject constructor(
    private val testString: String
) : BaseToSampleModel(), ToGenericSampleModel<String> {

    override fun printTestString(model: String) {
        Log.d("Test!!", "TestString is `$testString` in ToGenericSampleModelImpl class.")
    }
}