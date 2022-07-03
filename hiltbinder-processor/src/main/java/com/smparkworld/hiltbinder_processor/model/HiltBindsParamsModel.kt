package com.smparkworld.hiltbinder_processor.model

import com.smparkworld.hiltbinder_processor.core.base.ParametersModel
import javax.lang.model.element.Element

internal data class HiltBindsParamsModel(
    val to: Element,
    val from: Element,
    val component: Element? = null,
    val qualifier: Element? = null,
    val namedValue: String? = null
) : ParametersModel