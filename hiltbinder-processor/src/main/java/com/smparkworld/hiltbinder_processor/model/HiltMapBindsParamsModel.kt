package com.smparkworld.hiltbinder_processor.model

import com.smparkworld.hiltbinder_processor.core.base.ParametersModel
import javax.lang.model.element.Element

internal data class HiltMapBindsParamsModel(
    val toElement: Element,
    val fromElement: Element,
    val qualifierElement: Element? = null,
    val componentElement: Element? = null,
    val keyElement: Element,
    val keyElementParams: Map<String, Any>
) : ParametersModel