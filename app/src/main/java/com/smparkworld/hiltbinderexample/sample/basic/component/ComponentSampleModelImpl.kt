package com.smparkworld.hiltbinderexample.sample.basic.component

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import dagger.hilt.android.components.ActivityRetainedComponent
import javax.inject.Inject

@HiltBinds(component = ActivityRetainedComponent::class)
class ComponentSampleModelImpl @Inject constructor(
    private val testString: String
) : ComponentSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in ComponentSampleModelImpl class.")
    }
}