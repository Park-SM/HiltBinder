package com.smparkworld.hiltbinderexample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.smparkworld.hiltbinderexample.sample.supported.generic.single.SingleGenericSampleModel
import com.smparkworld.hiltbinderexample.sample.basic.component.ComponentSampleModel
import com.smparkworld.hiltbinderexample.sample.basic.from.FromSampleModel
import com.smparkworld.hiltbinderexample.sample.basic.named.NamedSampleModel
import com.smparkworld.hiltbinderexample.sample.basic.qualifier.QualifierSampleModel
import com.smparkworld.hiltbinderexample.sample.basic.qualifier.SampleQualifierA
import com.smparkworld.hiltbinderexample.sample.basic.qualifier.SampleQualifierB
import com.smparkworld.hiltbinderexample.sample.basic.to.ToSampleModel
import com.smparkworld.hiltbinderexample.sample.intomap.SampleType
import com.smparkworld.hiltbinderexample.sample.intomap.complexkey.MapComplexKeySampleModel
import com.smparkworld.hiltbinderexample.sample.intomap.complexkey.SampleMapComplexKey
import com.smparkworld.hiltbinderexample.sample.intomap.custom.MapCustomKeySampleModel
import com.smparkworld.hiltbinderexample.sample.intomap.hiltdefault.classkey.MapClassKeySampleModel
import com.smparkworld.hiltbinderexample.sample.intomap.hiltdefault.intkey.MapIntKeySampleModel
import com.smparkworld.hiltbinderexample.sample.intomap.hiltdefault.longkey.MapLongKeySampleModel
import com.smparkworld.hiltbinderexample.sample.intomap.hiltdefault.stringkey.MapStringKeySampleModel
import com.smparkworld.hiltbinderexample.sample.intomap.qualifier.QualifiedMapCustomKeySampleModel
import com.smparkworld.hiltbinderexample.sample.intomap.SampleKey
import com.smparkworld.hiltbinderexample.sample.intomap.named.NamedMapCustomKeySampleModel
import com.smparkworld.hiltbinderexample.sample.intomap.qualifier.SampleMapQualifierA
import com.smparkworld.hiltbinderexample.sample.intomap.qualifier.SampleMapQualifierB
import com.smparkworld.hiltbinderexample.sample.intoset.SetSampleModel
import com.smparkworld.hiltbinderexample.sample.intoset.named.NamedSetSampleModel
import com.smparkworld.hiltbinderexample.sample.intoset.qualifier.SampleSetQualifierA
import com.smparkworld.hiltbinderexample.sample.intoset.qualifier.SampleSetQualifierB
import com.smparkworld.hiltbinderexample.sample.intoset.qualifier.QualifiedSetSampleModel
import com.smparkworld.hiltbinderexample.sample.supported.generic.intoset.SetGenericSampleModel
import com.smparkworld.hiltbinderexample.sample.supported.generic.multiple.MultipleGenericSampleModel
import com.smparkworld.hiltbinderexample.sample.supported.nested.NestedSampleModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    ////////////////////////////////////////////
    // Basic usage
    @Inject
    lateinit var toSampleModel: ToSampleModel

    @Inject
    lateinit var fromSampleModel: FromSampleModel

    @Inject
    lateinit var componentSampleModel: ComponentSampleModel
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Basic usage - qualifier
    @Inject
    @SampleQualifierA
    lateinit var qualifierSampleModelA: QualifierSampleModel

    @Inject
    @SampleQualifierB
    lateinit var qualifierSampleModelB: QualifierSampleModel
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Basic usage - named
    @Inject
    @Named("modelA")
    lateinit var namedSampleModelA: NamedSampleModel

    @Inject
    @Named("modelB")
    lateinit var namedSampleModelB: NamedSampleModel
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Set Multibinding - basics
    @Inject
    lateinit var sampleSet: @JvmSuppressWildcards Set<SetSampleModel>
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Set Multibinding - qualifier
    @Inject
    @SampleSetQualifierA
    lateinit var sampleQualifiedSetA: @JvmSuppressWildcards Set<QualifiedSetSampleModel>

    @Inject
    @SampleSetQualifierB
    lateinit var sampleQualifiedSetB: @JvmSuppressWildcards Set<QualifiedSetSampleModel>
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Set Multibinding - named
    @Inject
    @Named("sampleNamedSetA")
    lateinit var sampleNamedSetA: @JvmSuppressWildcards Set<NamedSetSampleModel>

    @Inject
    @Named("sampleNamedSetB")
    lateinit var sampleNamedSetB: @JvmSuppressWildcards Set<NamedSetSampleModel>
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Map Multibinding - basic (Using @IntKey, @LongKey, @StringKey, @ClassKey provided by Hilt)
    @Inject
    lateinit var intKeySampleMap: @JvmSuppressWildcards Map<Int, Provider<MapIntKeySampleModel>>

    @Inject
    lateinit var longKeySampleMap: @JvmSuppressWildcards Map<Long, Provider<MapLongKeySampleModel>>

    @Inject
    lateinit var stringKeySampleMap: @JvmSuppressWildcards Map<String, Provider<MapStringKeySampleModel>>

    @Inject
    lateinit var classKeySampleMap: @JvmSuppressWildcards Map<Class<*>, Provider<MapClassKeySampleModel>>
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Map Multibinding - custom key
    @Inject
    lateinit var customKeySampleMap: @JvmSuppressWildcards Map<SampleType, Provider<MapCustomKeySampleModel>>
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Map Multibinding - complex custom key
    @Inject
    lateinit var complexKeySampleMap: @JvmSuppressWildcards Map<SampleMapComplexKey, Provider<MapComplexKeySampleModel>>
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Map Multibinding - qualifier
    @Inject
    @SampleMapQualifierA
    lateinit var qualifiedCustomKeySampleMapA: @JvmSuppressWildcards Map<SampleKey, Provider<QualifiedMapCustomKeySampleModel>>

    @Inject
    @SampleMapQualifierB
    lateinit var qualifiedCustomKeySampleMapB: @JvmSuppressWildcards Map<SampleKey, Provider<QualifiedMapCustomKeySampleModel>>
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Map Multibinding - named
    @Inject
    @Named("sampleNamedMapA")
    lateinit var namedCustomKeySampleMapA: @JvmSuppressWildcards Map<SampleKey, Provider<NamedMapCustomKeySampleModel>>

    @Inject
    @Named("sampleNamedMapB")
    lateinit var namedCustomKeySampleMapB: @JvmSuppressWildcards Map<SampleKey, Provider<NamedMapCustomKeySampleModel>>
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // supported - single generic type
    @Inject
    lateinit var singleGenericSampleModel1: SingleGenericSampleModel<Int>

    @Inject
    lateinit var singleGenericSampleModel2: SingleGenericSampleModel<String>

    @Inject
    lateinit var singleGenericSampleModel3: SingleGenericSampleModel<Any>
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // supported - single generic type
    @Inject
    lateinit var multipleGenericSampleModel: MultipleGenericSampleModel<Int, Any>
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // supported - set multibinding
    @Inject
    lateinit var setGenericSampleModelA: @JvmSuppressWildcards Set<SetGenericSampleModel<Int>>

    @Inject
    lateinit var setGenericSampleModelB: @JvmSuppressWildcards Set<SetGenericSampleModel<String>>
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // supported - nested type
    @Inject
    lateinit var nestedSampleModel: NestedSampleModel.SampleModel
    ////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toSampleModel.printTestString()
        fromSampleModel.printTestString()
        componentSampleModel.printTestString()
        qualifierSampleModelA.printTestString()
        qualifierSampleModelB.printTestString()
        namedSampleModelA.printTestString()
        namedSampleModelB.printTestString()

        sampleSet.forEach {
            it.printTestString()
        }

        Log.d("Test!!", "qualifier: SampleSetQualifierA")
        sampleQualifiedSetA.forEach {
            it.printTestString()
        }

        Log.d("Test!!", "qualifier: SampleSetQualifierB")
        sampleQualifiedSetB.forEach {
            it.printTestString()
        }

        Log.d("Test!!", "named: sampleNamedSetA")
        sampleNamedSetA.forEach {
            it.printTestString()
        }

        Log.d("Test!!", "named: sampleNamedSetB")
        sampleNamedSetB.forEach {
            it.printTestString()
        }

        for ((k, v) in intKeySampleMap) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        for ((k, v) in longKeySampleMap) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        for ((k, v) in stringKeySampleMap) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        for ((k, v) in classKeySampleMap) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        for ((k, v) in customKeySampleMap) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        for ((k, v) in complexKeySampleMap) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        Log.d("Test!!", "qualifier: SampleMapQualifierA")
        for ((k, v) in qualifiedCustomKeySampleMapA) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        Log.d("Test!!", "qualifier: SampleMapQualifierB")
        for ((k, v) in qualifiedCustomKeySampleMapB) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        Log.d("Test!!", "named: sampleNamedMapA")
        for ((k, v) in namedCustomKeySampleMapA) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        Log.d("Test!!", "named: sampleNamedMapB")
        for ((k, v) in namedCustomKeySampleMapB) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        singleGenericSampleModel1.printTestString(1205)
        singleGenericSampleModel2.printTestString("String")
        singleGenericSampleModel3.printTestString(1205.97)
        multipleGenericSampleModel.printTestString(97, 1205)

        setGenericSampleModelA.forEach {
            it.printTestString(1)
        }

        setGenericSampleModelB.forEach {
            it.printTestString("String1")
        }

        nestedSampleModel.printTestString()
    }
}