package com.smparkworld.hiltbinder_processor.generator.hiltmapbinds

import com.smparkworld.hiltbinder.HiltMapBinds
import com.smparkworld.hiltbinder_processor.core.base.ParameterMapper
import com.smparkworld.hiltbinder_processor.core.manager.AnnotationManager
import com.smparkworld.hiltbinder_processor.extension.asClassName
import com.smparkworld.hiltbinder_processor.extension.error
import com.smparkworld.hiltbinder_processor.extension.findSuperTypeMirror
import com.smparkworld.hiltbinder_processor.extension.getGenericTypeNames
import com.smparkworld.hiltbinder_processor.extension.getSuperTypeMirror
import com.smparkworld.hiltbinder_processor.generator.hiltbinds.HiltBindsParameterMapper
import com.smparkworld.hiltbinder_processor.model.HiltMapBindsParamsModel
import dagger.MapKey
import javax.annotation.processing.ProcessingEnvironment
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Scope
import javax.lang.model.element.Element

internal class HiltMapBindsParameterMapper : ParameterMapper<HiltMapBindsParamsModel> {

    override fun toParamsModel(env: ProcessingEnvironment, element: Element): HiltMapBindsParamsModel {
        val paramTo = AnnotationManager.getElementFromAnnotation<HiltMapBinds>(env, element, PARAM_TO)
        val paramFrom = AnnotationManager.getElementFromAnnotation<HiltMapBinds>(env, element, PARAM_FROM)
        val paramComponent = AnnotationManager.getElementFromAnnotation<HiltMapBinds>(env, element, PARAM_COMPONENT)
        val paramCombined = AnnotationManager.getValuesFromAnnotation<HiltMapBinds>(env, element)?.get(PARAM_COMBINED) as? Boolean
        val qualifier = AnnotationManager.getAnnotationByParentAnnotation(env, element, Qualifier::class, Named::class)
        val scope = AnnotationManager.getAnnotationByParentAnnotation(env, element, Scope::class)
        val namedValue = AnnotationManager.getValuesFromAnnotation<Named>(env, element)?.get(NAMED_PARAM) as? String

        val mapKey = AnnotationManager.getAnnotationByParentAnnotation(env, element, MapKey::class)
        val mapKeyParams = AnnotationManager.getValuesFromParentAnnotation(env, element, MapKey::class)

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
                    scope,
                    namedValue,
                    mapKey,
                    mapKeyParams
                )
            }
            (paramFrom == null && paramTo != null) -> {
                val to = element.findSuperTypeMirror(paramTo)
                if (to == null) {
                    env.error(ERROR_MSG_NOT_FOUND_SUPER)
                    throw IllegalStateException(ERROR_MSG_NOT_FOUND_SUPER)
                }

                HiltMapBindsParamsModel(
                    to.getGenericTypeNames(env, paramCombined),
                    element.asClassName(env),
                    paramComponent,
                    qualifier,
                    scope,
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
                    to.getGenericTypeNames(env, paramCombined),
                    element.asClassName(env),
                    paramComponent,
                    qualifier,
                    scope,
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
        private const val PARAM_COMBINED = "combined"

        private const val NAMED_PARAM = "value"
    }
}