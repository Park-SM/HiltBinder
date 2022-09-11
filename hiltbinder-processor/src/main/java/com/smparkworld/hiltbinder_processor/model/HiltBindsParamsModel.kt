package com.smparkworld.hiltbinder_processor.model

import com.smparkworld.hiltbinder_processor.core.base.ParametersModel
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Element

internal data class HiltBindsParamsModel(
    val to: TypeName,
    val from: TypeName,
    val component: Element? = null,
    val qualifier: Element? = null,
    val scope: Element? = null,
    val namedValue: String? = null
) : ParametersModel