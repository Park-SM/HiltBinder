package com.smparkworld.hiltbinderexample.sample.basic.scope

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@HiltBinds(component = ActivityRetainedComponent::class)
@ActivityRetainedScoped
class ScopeSampleModelImpl @Inject constructor(
    private val testString: String
) : ScopeSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in ScopeSampleModelImpl class.");
    }
}