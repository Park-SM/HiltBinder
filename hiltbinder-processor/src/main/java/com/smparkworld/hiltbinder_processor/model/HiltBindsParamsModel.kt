package com.smparkworld.hiltbinder_processor.model

import com.smparkworld.hiltbinder_processor.core.base.ParametersModel
import javax.lang.model.element.Element

internal data class HiltBindsParamsModel(
    val toElement: Element,
    val fromElement: Element,
    val qualifierElement: Element? = null,
    val componentElement: Element? = null
) : ParametersModel