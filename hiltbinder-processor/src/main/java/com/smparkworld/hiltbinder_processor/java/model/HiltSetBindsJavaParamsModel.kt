package com.smparkworld.hiltbinder_processor.java.model

import com.smparkworld.hiltbinder_processor.core.base.ParametersModel
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Element

internal data class HiltSetBindsJavaParamsModel(

    val moduleFileName: String,
    val elementName: String,
    val elementPackageName: String,

    val to: TypeName,
    val from: TypeName,
    val component: Element? = null,
    val qualifier: Element? = null,
    val scope: Element? = null,
    val namedValue: String? = null

) : ParametersModel