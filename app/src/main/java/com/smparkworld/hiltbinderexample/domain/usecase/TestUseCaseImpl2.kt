package com.smparkworld.hiltbinderexample.domain.usecase

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import com.smparkworld.hiltbinderexample.di.TestUseCaseQualifier2
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@HiltBinds(
    to = TestUseCase::class,
    qualifier = TestUseCaseQualifier2::class,
    component = ActivityComponent::class
)
class TestUseCaseImpl2 @Inject constructor(
    private val testString: String
) : TestUseCase {

    override fun printTestString() {
        Log.d("Test!!", "TestString is $testString in UseCase2.")
    }
}