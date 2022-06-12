# HiltBinder
An annotating processing example for Hilt's @Binds function and module.

## Sample
No longer need abstract module classes. Just add `@HiltBinds` and the Binds module will be created automatically.
```
package com.smparkworld.hiltbinderexample.domain.usecase

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import javax.inject.Inject

interface TestUseCase {

    fun printTestString()
}

@HiltBinds
class TestUseCaseImpl @Inject constructor(
    private val testString: String
) : TestUseCase {

    override fun printTestString() {
        Log.d("Test!!", "TestString is $testString in UseCase.")
    }
}

```
