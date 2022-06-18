package com.smparkworld.hiltbinderexample.data.mapper

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@HiltBinds(component = ActivityComponent::class)
class TestMapperImpl @Inject constructor(
    private val testString: String
) : TestMapper {

    override fun printTestString() {
        Log.d("Test!!", "TestString is $testString in Mapper")
    }
}