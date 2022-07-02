![Generic badge](https://img.shields.io/badge/Platform-Android-green.svg)&nbsp;
![Generic badge](https://img.shields.io/badge/Repository-MavenCentral-blue.svg)&nbsp;
![Generic badge](https://img.shields.io/badge/Version-v1.1.0-red.svg)&nbsp;
![Generic badge](https://img.shields.io/badge/License-Apache2.0-3DB7CC.svg)&nbsp;

# HiltBinder
An annotation processor example that automatically creates [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)'s `@Binds` functions and modules
- [How to use](https://github.com/Park-SM/HiltBinder#-how-to-use)
- [Basic usage](https://github.com/Park-SM/HiltBinder#-basic-usage)
- [Options](https://github.com/Park-SM/HiltBinder#-options)
  - [to option](https://github.com/Park-SM/HiltBinder#to)
  - [from option](https://github.com/Park-SM/HiltBinder#from)
  - [qualifier option](https://github.com/Park-SM/HiltBinder#qualifier)
  - [component option](https://github.com/Park-SM/HiltBinder#component)
- [Caution](https://github.com/Park-SM/HiltBinder#caution-here-)
- [Multibinding](https://github.com/Park-SM/HiltBinder#-multibinding)
  - [Set Multibinding - basics](https://github.com/Park-SM/HiltBinder#set-multibinding---basics)
  - [Map Multibinding - basics](https://github.com/Park-SM/HiltBinder#map-multibinding---basics)
  - [Map Multibinding - custom key](https://github.com/Park-SM/HiltBinder#map-multibinding---custom-key)
  - [Map Multibinding - complex custom key](https://github.com/Park-SM/HiltBinder#map-multibinding---complex-custom-key)
- [Performance monitoring](https://github.com/Park-SM/HiltBinder#-performance-monitoring)
- [License](https://github.com/Park-SM/HiltBinder#-license)

<br><br>
## # How to use
Add dependency like below code.
```groovy
// build.gradle(:project)
repositories {
    google()
    mavenCentral()
}

// build.gradle(:app)
dependencies {

    def hiltBinderVersion = "1.1.0"
    implementation "com.smparkworld.hiltbinder:hiltbinder:$hiltBinderVersion"
    kapt "com.smparkworld.hiltbinder:hiltbinder-processor:$hiltBinderVersion"
}
```
<br><br>
## # Basic usage
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
#### *`to`*<br>
> The return type of the Binds abstract function.
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

#### *`from`*<br>
> The argument type of the Binds abstract function. However, from an architectural point of view, this is not recommended.
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

#### *`qualifier`*
> The Qualifier annotation to be applied to the return type.
```kotlin
@HiltBinds(qualifier = TestUseCaseQualifier1::class)
class TestUseCaseImpl1 @Inject constructor(
    private val testString: String
) : TestUseCase {

    override fun printTestString() {
        Log.d("Test!!", "TestString is $testString in UseCase1.")
    }
}

@HiltBinds(qualifier = TestUseCaseQualifier2::class)
class TestUseCaseImpl2 @Inject constructor(
    private val testString: String
) : TestUseCase {

    override fun printTestString() {
        Log.d("Test!!", "TestString is $testString in UseCase2.")
    }
}
```

#### *`component`*
> Specifies in which component the class to be returned will be installed.
```kotlin
interface TestUseCase {
    fun printTestString()
}

@HiltBinds(component = ActivityComponent::class)
class TestUseCaseImpl @Inject constructor(
    private val testString: String
) : TestUseCase {

    override fun printTestString() {
        Log.d("Test!!", "TestString is $testString in UseCase.")
    }
}
```
<br><br>
#### *CAUTION HERE* ✋<br>
> parameter `to` and `from` must not be signed together. Either `to` or `from` must be used. If they are signed at the same time, throws an exception. Because dependency injection can be attempted from other unrelated classes as in the code below.
```kotlin
@HiltBinds(
    to = TestUseCase::class,
    from = TestUseCaseImpl::class
)
interface SomethingClass    // throws an exception.
```
<br><br>
## # MultiBinding
### *Set Multibinding - basics*<br>
> You must use `@HiltSetBinds` to apply Set Multibinding.
```kotlin
interface SetSampleModel {
    fun printTestString()
}

@HiltSetBinds
class SetSampleModelImpl1 @Inject constructor(
    private val testString: String
) : SetSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in SetSampleModelImpl1 class.")
    }
}

@HiltSetBinds
class SetSampleModelImpl2 @Inject constructor(
    private val testString: String
) : SetSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in SetSampleModelImpl2 class.")
    }
}

// This is the code to get Set Multibinding.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sampleSet: @JvmSuppressWildcards Set<SetSampleModel>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        sampleSet.forEach {
            it.printTestString()
        }
    }
}
```

### *Map Multibinding - basics*<br>
> You must use `@HiltMapBinds` to apply Map Multibinding. And you must to add a Key annotation with hilt's `@MapKey` applied, as in the code below. You can use the `@ClassKey`, `@StringKey`, `@IntKey`, `@LongKey` provided by hilt.
```kotlin
interface MapStringKeySampleModel {
    fun printTestString()
}

@HiltMapBinds
@StringKey("model1")
class MapStringKeySampleModelImpl1 @Inject constructor(
    private val testString: String
) : MapStringKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in MapStringKeySampleModelImpl1 class.")
    }
}

@HiltMapBinds
@StringKey("model2")
class MapStringKeySampleModelImpl2 @Inject constructor(
    private val testString: String
) : MapStringKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in MapStringKeySampleModelImpl2 class.")
    }
}

// This is the code to get Map Multibinding.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var stringKeySampleMap: @JvmSuppressWildcards Map<String, Provider<MapStringKeySampleModel>>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        for ((k, v) in stringKeySampleMap) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }
    }
}
```
  
### *Map Multibinding - custom key*<br>
> And you can define and use map key annotations. In addition to enum classes, you can define other types.
```kotlin
enum class SampleType {
    SAMPLE1, SAMPLE2, DEFAULT
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class SampleMapCustomKey(val key: SampleType)

interface MapCustomKeySampleModel {
    fun printTestString()
}

@HiltMapBinds
@SampleMapCustomKey(SampleType.SAMPLE1)
class MapCustomKeySampleModelImpl1 @Inject constructor(
    private val testString: String
) : MapCustomKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in MapSampleModelImpl1 class.")
    }
}

@HiltMapBinds
@SampleMapCustomKey(SampleType.SAMPLE2)
class MapCustomKeySampleModelImpl2 @Inject constructor(
    private val testString: String
) : MapCustomKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in MapSampleModelImpl2 class.")
    }
}

// This is the code to get Map Multibinding.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var customKeySampleMap: @JvmSuppressWildcards Map<SampleType, Provider<MapCustomKeySampleModel>>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        for ((k, v) in customKeySampleMap) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }
    }
}
```
  
### *Map Multibinding - complex custom key*<br>
> You can use key annotations with multiple parameters as in the code below. Complex custom keys require dependencies from the auto-value and auto-value annotation libraries. For more information, see [References](https://dagger.dev/dev-guide/multibindings).
```kotlin
/***
 * Complex key require the following dependencies:
 *
 *   def autoValueVersion = "1.9"
 *   implementation "com.google.auto.value:auto-value:$autoValueVersion"
 *   implementation "com.google.auto.value:auto-value-annotations:$autoValueVersion"
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MapKey(unwrapValue = false)
annotation class SampleMapComplexKey(
    val key1: String,
    val key2: KClass<*>,
    val key3: Array<String>,
    val key4: IntArray,
    val key5: SampleType
)

interface MapComplexKeySampleModel {
    fun printTestString()
}

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

@HiltMapBinds
@SampleMapComplexKey(
    key1 = "sample2",
    key2 = MapComplexKeySampleModelImpl2::class,
    key3 = ["s4", "s5", "s6"],
    key4 = [4, 5, 6],
    key5 = SampleType.SAMPLE2
)
class MapComplexKeySampleModelImpl2 @Inject constructor(
    private val testString: String
) : MapComplexKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in MapComplexKeySampleModelImpl2 class.");
    }
}

// This is the code to get Map Multibinding.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var complexKeySampleMap: @JvmSuppressWildcards Map<SampleMapComplexKey, Provider<MapComplexKeySampleModel>>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        for ((k, v) in complexKeySampleMap) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }
    }
}
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

<br><br>
## # Performance monitoring
You can monitor the elapsed time during annotation processing in the `Build` > `Build Output` tab of Android Studio.<br><br>
<img width="1121" src="https://user-images.githubusercontent.com/47319426/177004803-1e068a5b-f485-44a8-ad79-2609b9e21b86.png">

<br><br>
## # License
```
Copyright 2022 ParkSM

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
