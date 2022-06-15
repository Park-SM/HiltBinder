# HiltBinder
An annotation processor example that automatically creates Hilt's `@Binds` functions and modules.<br><br>

## # Description
No longer need abstract module classes. Just add `@HiltBinds` and the Binds module will be created automatically.
```kotlin
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

And the processor automatically generates the following Java files:
```kotlin
@Module
@InstallIn(SingletonComponent.class)
abstract class TestUseCaseImpl_BindsModule {
    @Binds
    public abstract TestUseCase bindTestUseCaseImpl(TestUseCaseImpl target);
}

```
<br><br>
## # Options
- `to`: The return type of the Binds abstract function.
```kotlin
interface TestUseCase {
    fun printTestString()
}

@HiltBinds(to = TestUseCase::class)
class TestUseCaseImpl @Inject constructor(
    private val testString: String
) : BaseUseCase(), TestUseCase {

    override fun printTestString() {
        Log.d("Test!!", "TestString is $testString in UseCase.")
    }
}
```

- `from`: The argument type of the Binds abstract function. However, from an architectural point of view, this is not recommended.
```kotlin
@HiltBinds(from = TestUseCaseImpl::class)
interface TestUseCase {
    fun printTestString()
}

class TestUseCaseImpl @Inject constructor(
    private val testString: String
) : BaseUseCase(), TestUseCase {

    override fun printTestString() {
        Log.d("Test!!", "TestString is $testString in UseCase.")
    }
}
```
<br><br>
*CAUTION HERE* ✋<br>
> parameter `to` and `from` must not be signed together. Either `to` or `from` must be used. If they are signed at the same time, throws an exception. This is because dependency injection can be attempted from other unrelated classes as in the code below.
```kotlin
@HiltBinds(
    to = TestUseCase::class,
    from = TestUseCaseImpl::class
)
interface SomethingClass    // throws an exception.
```
<br><br>
## # Supported
It also supports nested class as below code.
```kotlin
interface TestContract {
    interface TestClass {
        fun printSomething()
    }
}

@HiltBinds
class TestClassImpl @Inject constructor(
    private val testString: String
) : TestContract.TestClass {
    
    override fun printSomething() {
        Log.d("Test!!", "TestString is $testString")
    }
}
```
