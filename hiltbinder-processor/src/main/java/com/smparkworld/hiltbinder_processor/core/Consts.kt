package com.smparkworld.hiltbinder_processor.core

internal object Consts {

    const val PARAMETER_NAME = "target"
    const val MODULE_SUFFIX = "_BindsModule"
    const val FUNCTION_PREFIX = "bind"

    const val PARAM_TO = "to"
    const val PARAM_FROM = "from"
    const val PARAM_COMPONENT = "component"
    const val PARAM_COMBINED = "combined"

    const val NAMED_PARAM = "value"

    const val ERROR_MSG_SIGNED_TOGETHER = "`to` and `from` cannot be signed together."
    const val ERROR_MSG_NOT_FOUND_SUPER = "Super class not found."
}