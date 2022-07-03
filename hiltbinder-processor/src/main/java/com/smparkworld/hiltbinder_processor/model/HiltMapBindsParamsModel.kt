package com.smparkworld.hiltbinder_processor.model

import com.smparkworld.hiltbinder_processor.core.base.ParametersModel
import javax.lang.model.element.Element

internal data class HiltMapBindsParamsModel(
    val to: Element,
    val from: Element,
    val component: Element? = null,
    val qualifier: Element? = null,
    val mapKey: Element,
    val mapKeyParams: Map<String, Any>
) : ParametersModel