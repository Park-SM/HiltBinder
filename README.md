![Generic badge](https://img.shields.io/badge/Platform-Android-green.svg)&nbsp;
![Generic badge](https://img.shields.io/badge/Repository-MavenCentral-blue.svg)&nbsp;
![Generic badge](https://img.shields.io/badge/Version-v1.2.0-red.svg)&nbsp;
![Generic badge](https://img.shields.io/badge/License-Apache2.0-3DB7CC.svg)&nbsp;

# HiltBinder
An annotation processor example that automatically creates [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)'s `@Binds` functions and modules.  
If you think this library is useful, please press `Star` button at upside : )
- [How to use](https://github.com/Park-SM/HiltBinder#-how-to-use)
- [Basic usage](https://github.com/Park-SM/HiltBinder#-basic-usage)
- [Options](https://github.com/Park-SM/HiltBinder#-options)
  - [to property](https://github.com/Park-SM/HiltBinder#to)
  - [from property](https://github.com/Park-SM/HiltBinder#from)
  - [component property](https://github.com/Park-SM/HiltBinder#component)  
  - [scope](https://github.com/Park-SM/HiltBinder#scope)
  - [qualifier](https://github.com/Park-SM/HiltBinder#qualifier)
  - [named](https://github.com/Park-SM/HiltBinder#named)
- [Caution](https://github.com/Park-SM/HiltBinder#caution-here-)
- [Multibinding](https://github.com/Park-SM/HiltBinder#-multibinding)
  - [Set Multibinding - basics](https://github.com/Park-SM/HiltBinder#set-multibinding---basics)
  - [Set Multibinding - qualifier](https://github.com/Park-SM/HiltBinder#set-multibinding---qualifier)
  - [Set Multibinding - named](https://github.com/Park-SM/HiltBinder#set-multibinding---named)
  - [Map Multibinding - basics](https://github.com/Park-SM/HiltBinder#map-multibinding---basics)
  - [Map Multibinding - custom key](https://github.com/Park-SM/HiltBinder#map-multibinding---custom-key)
  - [Map Multibinding - complex custom key](https://github.com/Park-SM/HiltBinder#map-multibinding---complex-custom-key)
  - [Map Multibinding - qualifier](https://github.com/Park-SM/HiltBinder#map-multibinding---qualifier)
  - [Map Multibinding - named](https://github.com/Park-SM/HiltBinder#map-multibinding---named)
- [Supported](https://github.com/Park-SM/HiltBinder#-supported)
  - [Generic Type - single](https://github.com/Park-SM/HiltBinder#generic-type---single)
  - [Generic Type - multiple](https://github.com/Park-SM/HiltBinder#generic-type---multiple)
  - [Generic Type - set multibinding](https://github.com/Park-SM/HiltBinder#generic-type---set-multibinding)
  - [Nested Type](https://github.com/Park-SM/HiltBinder#nested-type)
- [More Sample Code](https://github.com/Park-SM/HiltBinder/tree/develop/app/src/main/java/com/smparkworld/hiltbinderexample)
- [Performance monitoring](https://github.com/Park-SM/HiltBinder#-performance-monitoring)
- [License](https://github.com/Park-SM/HiltBinder#-license)

<br><br>
## # How to use
Can be used in android project with hilt applied. And add dependency like below code.
```groovy
// build.gradle(:project)
repositories {
    google()
    mavenCentral()
}

// build.gradle(:app)
dependencies {

    def hiltBinderVersion = "1.2.0"
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
```java
@Module
@InstallIn(SingletonComponent.class)
abstract class TestUseCaseImpl_BindsModule {
    @Binds
    public abstract TestUseCase bindTestUseCaseImpl(TestUseCaseImpl target);
}
```

<br><br>
## # Options
#### *to*<br>
> The return type of the Binds abstract function.
```kotlin
interface OtherSpec {
  ...
}

interface ToSampleModel {
    fun printTestString()
}

@HiltBinds(to = ToSampleModel::class)
class ToSampleModelImpl @Inject constructor(
    private val testString: String
) : OtherSpec, ToSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in ToSampleModelImpl class.")
    }
}
```

#### *from*<br>
> The argument type of the Binds abstract function. However, from an architectural point of view, this is not recommended.
```kotlin
@HiltBinds(from = FromSampleModelImpl::class)
interface FromSampleModel {
    fun printTestString()
}

class FromSampleModelImpl @Inject constructor(
    private val testString: String
) : FromSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in FromSampleModelImpl class.")
    }
}
```

#### *component*<br>
> Specifies in which component the class to be returned will be installed.
```kotlin
interface ComponentSampleModel {
    fun printTestString()
}

@HiltBinds(component = ActivityRetainedComponent::class)
class ComponentSampleModelImpl @Inject constructor(
    private val testString: String
) : ComponentSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in ComponentSampleModelImpl class.")
    }
}
```

#### *scope*<br>
> To specify ranges separately, apply scope annotations as in the following code snippet. The reason this can work is that applying a scope to the implementing class works to keep the singleton in scope via the `dagger.internal.DoubleCheck` class within the Hilt.
```kotlin
interface ComponentSampleModel {
    fun printTestString()
}

@HiltBinds(component = ActivityRetainedComponent::class)
@ActivityRetainedScope
class ComponentSampleModelImpl @Inject constructor(
    private val testString: String
) : ComponentSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in ComponentSampleModelImpl class.")
    }
}

or

@HiltBinds
@Singleton
class SomethingSampleModelImpl @Inject constructor(
    private val testString: String
) : SomethingSampleModel {
  ...
}
```

#### *qualifier*<br>
> The Qualifier annotation to be applied to the return type.
```kotlin
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SampleQualifier1

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SampleQualifier2

interface QualifierSampleModel {
    fun printTestString()
}

@HiltBinds
@SampleQualifier1
class QualifierSampleModelImpl1 @Inject constructor(
    private val testString: String
) : QualifierSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in QualifierSampleModelImpl1 class.")
    }
}

@HiltBinds
@SampleQualifier2
class QualifierSampleModelImpl2 @Inject constructor(
    private val testString: String
) : QualifierSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in QualifierSampleModelImpl2 class.")
    }
}
```

#### *named*<br>
> The Qualifier annotation to be applied to the return type.
```kotlin
interface NamedSampleModel {
    fun printTestString()
}

@HiltBinds
@Named("model1")
class NamedSampleModelImpl1 @Inject constructor(
   private val testString: String
) : NamedSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in NamedSampleModelImpl1 class.")
    }
}

@HiltBinds
@Named("model2")
class NamedSampleModelImpl2 @Inject constructor(
   private val testString: String
) : NamedSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in NamedSampleModelImpl2 class.")
    }
}
```

<br><br>
#### *CAUTION HERE* ✋<br>
> parameter `to` and `from` must not be signed together. Either `to` or `from` must be used. If they are signed at the same time, throws an exception. Because dependency injection can be attempted from other unrelated classes as in the code below.
```kotlin
@HiltBinds(
    to = SampleModel::class,
    from = SampleModelImpl::class
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

### *Set Multibinding - qualifier*<br>
> If you want to configure multiple `Set Multibinding` of the same type, you can use @Qualifier(`javax.inject.Qualifier`) annotations like this:
```kotlin
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SampleSetQualifierA

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SampleSetQualifierB

interface QualifiedSetSampleModel {
    fun printTestString()
}

@HiltSetBinds
@SampleSetQualifierA
class QualifiedSetSampleModelImpl1 @Inject constructor(
    private val testString: String
) : QualifiedSetSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in QualifiedSetSampleModelImpl1 class.")
    }
}

@HiltSetBinds
@SampleSetQualifierB
class QualifiedSetSampleModelImpl2 @Inject constructor(
    private val testString: String
) : QualifiedSetSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in QualifiedSetSampleModelImpl2 class.")
    }
}

// This is the code to get Set Multibinding - qualifier.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    @SampleSetQualifierA
    lateinit var sampleQualifiedSetA: @JvmSuppressWildcards Set<QualifiedSetSampleModel>

    @Inject
    @SampleSetQualifierB
    lateinit var sampleQualifiedSetB: @JvmSuppressWildcards Set<QualifiedSetSampleModel>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        sampleQualifiedSetA.forEach { it.printTestString() }
        sampleQualifiedSetB.forEach { it.printTestString() }
    }
}
```

### *Set Multibinding - named*<br>
> If you want to configure multiple `Set Multibinding` of the same type, you can use @Named(`javax.inject.Named`) annotations like this:
```kotlin
interface NamedSetSampleModel {
    fun printTestString()
}

@HiltSetBinds
@Named("sampleNamedSetA")
class NamedSetSampleModelImpl1 @Inject constructor(
    private val testString: String
) : NamedSetSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in NamedSetSampleModelImpl1 class.")
    }
}

@HiltSetBinds
@Named("sampleNamedSetB")
class NamedSetSampleModelImpl2 @Inject constructor(
    private val testString: String
) : NamedSetSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in NamedSetSampleModelImpl2 class.")
    }
}

// This is the code to get Set Multibinding - named.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    @Named("sampleNamedSetA")
    lateinit var sampleNamedSetA: @JvmSuppressWildcards Set<NamedSetSampleModel>

    @Inject
    @Named("sampleNamedSetB")
    lateinit var sampleNamedSetB: @JvmSuppressWildcards Set<NamedSetSampleModel>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        sampleNamedSetA.forEach { it.printTestString() }
        sampleNamedSetB.forEach { it.printTestString() }
    }
}
```

### *Map Multibinding - basics*<br>
> You must use `@HiltMapBinds` to apply `Map Multibinding`. And you must to add a Key annotation with hilt's `@MapKey` applied, as in the code below. You can use the `@ClassKey`, `@StringKey`, `@IntKey`, `@LongKey` provided by hilt.
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
        Log.d("Test!!", "TestString is `$testString` in MapCustomKeySampleModelImpl1 class.")
    }
}

@HiltMapBinds
@SampleMapCustomKey(SampleType.SAMPLE2)
class MapCustomKeySampleModelImpl2 @Inject constructor(
    private val testString: String
) : MapCustomKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in MapCustomKeySampleModelImpl2 class.")
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
> You can use key annotations with multiple parameters as in the code below. Complex custom keys require dependencies from the `auto-value` and `auto-value-annotation` libraries. For more information, see [References](https://dagger.dev/dev-guide/multibindings).
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

### *Map Multibinding - qualifier*<br>
> If you want to configure multiple `Map Multibinding` of the same type, you can use @Qualifier(`javax.inject.Qualifier`) annotations like this:
```kotlin
enum class SampleKey {
    KEY1, KEY2, KEY3, KEY4, DEFAULT
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class QualifiedSampleMapCustomKey(val key: SampleKey)

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SampleMapQualifierA

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SampleMapQualifierB

interface QualifiedMapCustomKeySampleModel {
    fun printTestString()
}

@HiltMapBinds
@QualifiedSampleMapCustomKey(SampleKey.KEY1)
@SampleMapQualifierA
class QualifiedMapCustomKeySampleModelImpl1 @Inject constructor(
    private val testString: String
) : QualifiedMapCustomKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in QualifiedMapCustomKeySampleModelImpl1 class.")
    }
}

@HiltMapBinds
@QualifiedSampleMapCustomKey(SampleKey.KEY2)
@SampleMapQualifierB
class QualifiedMapCustomKeySampleModelImpl2 @Inject constructor(
    private val testString: String
) : QualifiedMapCustomKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in QualifiedMapCustomKeySampleModelImpl2 class.")
    }
}

// This is the code to get Map Multibinding - qualifier.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    @SampleMapQualifierA
    lateinit var qualifiedCustomKeySampleMapA: @JvmSuppressWildcards Map<SampleKey, Provider<QualifiedMapCustomKeySampleModel>>

    @Inject
    @SampleMapQualifierB
    lateinit var qualifiedCustomKeySampleMapB: @JvmSuppressWildcards Map<SampleKey, Provider<QualifiedMapCustomKeySampleModel>>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        for ((k, v) in qualifiedCustomKeySampleMapA) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        for ((k, v) in qualifiedCustomKeySampleMapB) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }
    }
}
```

### *Map Multibinding - named*<br>
> If you want to configure multiple `Map Multibinding` of the same type, you can use @Named(`javax.inject.Named`) annotations like this:
```kotlin
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class NamedSampleMapCustomKey(val key: SampleKey)

interface NamedMapCustomKeySampleModel {
    fun printTestString()
}

@HiltMapBinds
@NamedSampleMapCustomKey(SampleKey.KEY1)
@Named("sampleNamedMapA")
class NamedMapCustomKeySampleModelImpl1 @Inject constructor(
    private val testString: String
) : NamedMapCustomKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in NamedMapCustomKeySampleModelImpl1 class.")
    }
}

@HiltMapBinds
@NamedSampleMapCustomKey(SampleKey.KEY2)
@Named("sampleNamedMapB")
class NamedMapCustomKeySampleModelImpl2 @Inject constructor(
    private val testString: String
) : NamedMapCustomKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in NamedMapCustomKeySampleModelImpl2 class.")
    }
}

// This is the code to get Map Multibinding - named.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    @Named("sampleNamedMapA")
    lateinit var namedCustomKeySampleMapA: @JvmSuppressWildcards Map<SampleKey, Provider<NamedMapCustomKeySampleModel>>

    @Inject
    @Named("sampleNamedMapB")
    lateinit var namedCustomKeySampleMapB: @JvmSuppressWildcards Map<SampleKey, Provider<NamedMapCustomKeySampleModel>>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        for ((k, v) in namedCustomKeySampleMapA) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        for ((k, v) in namedCustomKeySampleMapB) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }
    }
}
```

<br><br>
## # Supported
### *Generic Type - single*<br>
> You can set the return type to a single generic type through @HiltBinds.
```kotlin
interface SingleGenericSampleModel<T> {
    fun printTestString(data: T)
}

@HiltBinds
class SingleGenericSampleModelImpl1 @Inject constructor(
    private val testString: String
) : SingleGenericSampleModel<Int> {

    override fun printTestString(data: Int) {
        Log.d("Test!!", "TestString is `$testString` in GenericSampleModelImpl1 class. :: Generic type is <Int>")
    }
}

@HiltBinds
class SingleGenericSampleModelImpl2 @Inject constructor(
    private val testString: String
) : SingleGenericSampleModel<String> {

    override fun printTestString(model: String) {
        Log.d("Test!!", "TestString is `$testString` in GenericSampleModelImpl2 class. :: Generic type is <String>")
    }
}

@HiltBinds
class SingleGenericSampleModelImpl3 @Inject constructor(
    private val testString: String
) : SingleGenericSampleModel<Any> {

    override fun printTestString(data: Any) {
        Log.d("Test!!", "TestString is `$testString` in GenericSampleModelImpl3 class. :: Generic type is <Any>")
    }
}

// This is the code to get Generic Type - single.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var singleGenericSampleModel1: SingleGenericSampleModel<Int>

    @Inject
    lateinit var singleGenericSampleModel2: SingleGenericSampleModel<String>

    @Inject
    lateinit var singleGenericSampleModel3: SingleGenericSampleModel<Any>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        singleGenericSampleModel1.printTestString(1205)
        singleGenericSampleModel2.printTestString("String")
        singleGenericSampleModel3.printTestString(1205.97)
    }
}
```

### *Generic Type - multiple*<br>
> You can set the return type to multiple generic types through @HiltBinds.
```kotlin
interface MultipleGenericSampleModel<T1, T2> {
    fun printTestString(data1: T1, data2: T2)
}

@HiltBinds
class MultipleGenericSampleModelImpl @Inject constructor(
    private val testString: String
) : MultipleGenericSampleModel<Int, Any> {

    override fun printTestString(data1: Int, data2: Any) {
        Log.d("Test!!", "TestString is `$testString` in GenericSampleModelImpl1 class. :: Generic type is <Int, Any>")
    }
}

// This is the code to get Generic Type - multiple.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var multipleGenericSampleModel: MultipleGenericSampleModel<Int, Any>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        multipleGenericSampleModel.printTestString(97, 1205)
    }
}
```

### *Generic Type - set multibinding*<br>
> You can set the return type as a generic type through @HiltSetBinds. Of course, multiple generic types are possible.
```kotlin
interface SetGenericSampleModel<T> {
    fun printTestString(data: T)
}

@HiltSetBinds
class SetGenericSampleModelImpl1 @Inject constructor(
  private val testString: String
) : SetGenericSampleModel<Int> {

  override fun printTestString(data: Int) {
    Log.d("Test!!", "TestString is `$testString` in SetGenericSampleModelImpl1 class. :: Generic type is <Int>")
  }
}

@HiltSetBinds
class SetGenericSampleModelImpl2 @Inject constructor(
  private val testString: String
) : SetGenericSampleModel<Int> {

  override fun printTestString(data: Int) {
    Log.d("Test!!", "TestString is `$testString` in SetGenericSampleModelImpl2 class. :: Generic type is <Int>")
  }
}

@HiltSetBinds
class SetGenericSampleModelImpl3 @Inject constructor(
  private val testString: String
) : SetGenericSampleModel<String> {

  override fun printTestString(data: String) {
    Log.d("Test!!", "TestString is `$testString` in SetGenericSampleModelImpl3 class. :: Generic type is <String>")
  }
}

@HiltSetBinds
class SetGenericSampleModelImpl4 @Inject constructor(
  private val testString: String
) : SetGenericSampleModel<String> {

  override fun printTestString(data: String) {
    Log.d("Test!!", "TestString is `$testString` in SetGenericSampleModelImpl4 class. :: Generic type is <String>")
  }
}

// This is the code to get Generic Type - Set Multibinding.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  @Inject
  lateinit var setGenericSampleModelA: @JvmSuppressWildcards Set<SetGenericSampleModel<Int>>

  @Inject
  lateinit var setGenericSampleModelB: @JvmSuppressWildcards Set<SetGenericSampleModel<String>>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setGenericSampleModelA.forEach {
      it.printTestString(1)
    }

    setGenericSampleModelB.forEach {
      it.printTestString("String1")
    }
  }
}
```

### *Nested Type*<br>
> It also supports nested class as below code.
```kotlin
interface NestedSampleModel {
    interface SampleModel {
        fun printTestString()
    }
}

@HiltBinds
class NestedSampleModelImpl @Inject constructor(
    private val testString: String
) : NestedSampleModel.SampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in NestedSampleModelImpl class.")
    }
}

// This is the code to get Nested Type.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var nestedSampleModel: NestedSampleModel.SampleModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nestedSampleModel.printTestString()
    }
}
```

<br><br>
## # Performance monitoring
You can monitor the elapsed time during annotation processing in the `Build` > `Build Output` tab of Android Studio.<br><br>
<img width="1125" src="https://user-images.githubusercontent.com/47319426/177049395-11156685-5707-4c15-a898-2f8cc086faf0.png">


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
