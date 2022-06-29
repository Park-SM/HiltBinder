package com.smparkworld.hiltbinder_processor.generator.hiltmapbinds

import com.smparkworld.hiltbinder.HiltMapBinds
import com.smparkworld.hiltbinder_processor.core.base.ParameterMapper
import com.smparkworld.hiltbinder_processor.core.manager.AnnotationManager
import com.smparkworld.hiltbinder_processor.extension.error
import com.smparkworld.hiltbinder_processor.extension.getSuperInterfaceElement
import com.smparkworld.hiltbinder_processor.extension.log
import com.smparkworld.hiltbinder_processor.model.HiltMapBindsParamsModel
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind

internal class HiltMapBindsParameterMapper : ParameterMapper<HiltMapBindsParamsModel> {

    override fun toParamsModel(env: ProcessingEnvironment, element: Element): HiltMapBindsParamsModel {
        val paramTo = AnnotationManager.getAnnotationValue<HiltMapBinds>(env, element, PARAM_TO)
        val paramFrom = AnnotationManager.getAnnotationValue<HiltMapBinds>(env, element, PARAM_FROM)
        val paramQualifier = AnnotationManager.getAnnotationValue<HiltMapBinds>(env, element, PARAM_QUALIFIER)?.takeIf {
            it.kind == ElementKind.ANNOTATION_TYPE
        }
        val paramComponent = AnnotationManager.getAnnotationValue<HiltMapBinds>(env, element, PARAM_COMPONENT)
        val keyElement = AnnotationManager.getAnnotationBySuffix(env, element, MAP_ANNOTATION_KEY_SUFFIX)
        val keyElementParams = AnnotationManager.getAnnotationValuesBySuffix(env, element, MAP_ANNOTATION_KEY_SUFFIX)

        if (keyElement == null || keyElementParams == null) {
            env.error(ERROR_MSG_NOT_FOUND_KEY)
            throw IllegalStateException(ERROR_MSG_NOT_FOUND_KEY)
        }
        if (keyElementParams.isEmpty()) {
            env.log("Test!!:: $keyElement")
            env.error(ERROR_MSG_PARAMS_EMPTY)
            throw IllegalArgumentException(ERROR_MSG_PARAMS_EMPTY)
        }

        return when {
            (paramFrom != null && paramTo == null) -> {
                HiltMapBindsParamsModel(
                    element,
                    paramFrom,
                    paramQualifier,
                    paramComponent,
                    keyElement,
                    keyElementParams
                )
            }
            (paramFrom == null && paramTo != null) -> {
                HiltMapBindsParamsModel(
                    paramTo,
                    element,
                    paramQualifier,
                    paramComponent,
                    keyElement,
                    keyElementParams
                )
            }
            (paramFrom == null && paramTo == null) -> {
                HiltMapBindsParamsModel(
                    env.getSuperInterfaceElement(element),
                    element,
                    paramQualifier,
                    paramComponent,
                    keyElement,
                    keyElementParams
                )
            }
            else -> {
                env.error(ERROR_MSG_SIGNED_TOGETHER)
                throw IllegalStateException(ERROR_MSG_SIGNED_TOGETHER)
            }
        }
    }

    companion object {
        private const val ERROR_MSG_NOT_FOUND_KEY = "@HiltMapBinds must have Key annotation."
        private const val ERROR_MSG_PARAMS_EMPTY = "key annotation must not be empty."
        private const val ERROR_MSG_SIGNED_TOGETHER = "`to` and `from` cannot be signed together."

        private const val MAP_ANNOTATION_KEY_SUFFIX = "Key"

        private const val PARAM_TO = "to"
        private const val PARAM_FROM = "from"
        private const val PARAM_QUALIFIER = "qualifier"
        private const val PARAM_COMPONENT = "component"
    }
}