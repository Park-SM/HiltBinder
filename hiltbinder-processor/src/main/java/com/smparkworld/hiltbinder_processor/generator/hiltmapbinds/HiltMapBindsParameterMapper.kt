package com.smparkworld.hiltbinder_processor.generator.hiltmapbinds

import com.smparkworld.hiltbinder.HiltMapBinds
import com.smparkworld.hiltbinder_processor.core.base.ParameterMapper
import com.smparkworld.hiltbinder_processor.core.manager.AnnotationManager
import com.smparkworld.hiltbinder_processor.extension.asClassName
import com.smparkworld.hiltbinder_processor.extension.error
import com.smparkworld.hiltbinder_processor.extension.getGenericTypeNames
import com.smparkworld.hiltbinder_processor.extension.getSuperTypeMirror
import com.smparkworld.hiltbinder_processor.model.HiltMapBindsParamsModel
import dagger.MapKey
import javax.annotation.processing.ProcessingEnvironment
import javax.inject.Named
import javax.inject.Qualifier
import javax.lang.model.element.Element

internal class HiltMapBindsParameterMapper : ParameterMapper<HiltMapBindsParamsModel> {

    override fun toParamsModel(env: ProcessingEnvironment, element: Element): HiltMapBindsParamsModel {
        val paramTo = AnnotationManager.getAnnotationValue<HiltMapBinds>(env, element, PARAM_TO)
        val paramFrom = AnnotationManager.getAnnotationValue<HiltMapBinds>(env, element, PARAM_FROM)
        val paramComponent = AnnotationManager.getAnnotationValue<HiltMapBinds>(env, element, PARAM_COMPONENT)

        val qualifier = AnnotationManager.getAnnotationByParentAnnotation(env, element, Qualifier::class, Named::class)
        val namedValue = AnnotationManager.getAnnotationValues<Named>(env, element)?.get(NAMED_PARAM) as? String
        val mapKey = AnnotationManager.getAnnotationByParentAnnotation(env, element, MapKey::class)
        val mapKeyParams = AnnotationManager.getAnnotationValuesByParentAnnotation(env, element, MapKey::class)

        if (mapKey == null || mapKeyParams == null) {
            env.error(ERROR_MSG_NOT_FOUND_KEY)
            throw IllegalStateException(ERROR_MSG_NOT_FOUND_KEY)
        }
        if (mapKeyParams.isEmpty()) {
            env.error(ERROR_MSG_PARAMS_EMPTY)
            throw IllegalArgumentException(ERROR_MSG_PARAMS_EMPTY)
        }

        return when {
            (paramFrom != null && paramTo == null) -> {
                HiltMapBindsParamsModel(
                    element.asClassName(env),
                    paramFrom.asClassName(env),
                    paramComponent,
                    qualifier,
                    namedValue,
                    mapKey,
                    mapKeyParams
                )
            }
            (paramFrom == null && paramTo != null) -> {
                HiltMapBindsParamsModel(
                    paramTo.asClassName(env),
                    element.asClassName(env),
                    paramComponent,
                    qualifier,
                    namedValue,
                    mapKey,
                    mapKeyParams
                )
            }
            (paramFrom == null && paramTo == null) -> {
                val to = element.getSuperTypeMirror()
                if (to == null) {
                    env.error(ERROR_MSG_NOT_FOUND_SUPER)
                    throw IllegalStateException(ERROR_MSG_NOT_FOUND_SUPER)
                }

                HiltMapBindsParamsModel(
                    to.getGenericTypeNames(env),
                    element.asClassName(env),
                    paramComponent,
                    qualifier,
                    namedValue,
                    mapKey,
                    mapKeyParams
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
        private const val ERROR_MSG_NOT_FOUND_SUPER = "Super class not found."

        private const val PARAM_TO = "to"
        private const val PARAM_FROM = "from"
        private const val PARAM_COMPONENT = "component"

        private const val NAMED_PARAM = "value"
    }
}