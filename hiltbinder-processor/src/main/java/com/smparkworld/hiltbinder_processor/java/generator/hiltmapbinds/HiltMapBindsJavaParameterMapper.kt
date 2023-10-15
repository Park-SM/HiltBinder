package com.smparkworld.hiltbinder_processor.java.generator.hiltmapbinds

import com.google.auto.service.AutoService
import com.smparkworld.hiltbinder.HiltMapBinds
import com.smparkworld.hiltbinder_processor.core.Consts
import com.smparkworld.hiltbinder_processor.core.Logger
import com.smparkworld.hiltbinder_processor.core.base.JavaParameterMapper
import com.smparkworld.hiltbinder_processor.core.manager.AnnotationManager
import com.smparkworld.hiltbinder_processor.java.extension.asClassName
import com.smparkworld.hiltbinder_processor.java.extension.findSuperTypeMirror
import com.smparkworld.hiltbinder_processor.java.extension.getGenericTypeNames
import com.smparkworld.hiltbinder_processor.java.extension.getPackageName
import com.smparkworld.hiltbinder_processor.java.extension.getSuperTypeMirror
import com.smparkworld.hiltbinder_processor.java.model.HiltMapBindsJavaParamsModel
import dagger.MapKey
import javax.annotation.processing.ProcessingEnvironment
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Scope
import javax.lang.model.element.Element
import kotlin.reflect.KClass

@AutoService(JavaParameterMapper::class)
internal class HiltMapBindsJavaParameterMapper : JavaParameterMapper<HiltMapBindsJavaParamsModel> {

    override fun getSupportedAnnotationType(): KClass<out Annotation> = HiltMapBinds::class

    override fun toParamsModel(env: ProcessingEnvironment, target: Element, logger: Logger): HiltMapBindsJavaParamsModel {
        val moduleFileName = "${target.simpleName}${Consts.MODULE_SUFFIX}"
        val elementName = target.simpleName.toString()
        val elementPackageName = env.getPackageName(target)

        val paramTo = AnnotationManager.getElementFromAnnotation<HiltMapBinds>(env, target, Consts.PARAM_TO)
        val paramFrom = AnnotationManager.getElementFromAnnotation<HiltMapBinds>(env, target, Consts.PARAM_FROM)
        val paramComponent = AnnotationManager.getElementFromAnnotation<HiltMapBinds>(env, target, Consts.PARAM_COMPONENT)
        val paramCombined = AnnotationManager.getValuesFromAnnotation<HiltMapBinds>(env, target)?.get(Consts.PARAM_COMBINED) as? Boolean
        val qualifier = AnnotationManager.getAnnotationByParentAnnotation(env, target, Qualifier::class, Named::class)
        val scope = AnnotationManager.getAnnotationByParentAnnotation(env, target, Scope::class)
        val namedValue = AnnotationManager.getValuesFromAnnotation<Named>(env, target)?.get(Consts.NAMED_PARAM) as? String

        val mapKey = AnnotationManager.getAnnotationByParentAnnotation(env, target, MapKey::class)
        val mapKeyParams = AnnotationManager.getValuesFromParentAnnotation(env, target, MapKey::class)

        if (mapKey == null || mapKeyParams == null) {
            logger.error(ERROR_MSG_NOT_FOUND_KEY)
            throw IllegalStateException(ERROR_MSG_NOT_FOUND_KEY)
        }
        if (mapKeyParams.isEmpty()) {
            logger.error(ERROR_MSG_PARAMS_EMPTY)
            throw IllegalArgumentException(ERROR_MSG_PARAMS_EMPTY)
        }

        return when {
            (paramFrom != null && paramTo == null) -> {
                HiltMapBindsJavaParamsModel(
                    moduleFileName = moduleFileName,
                    elementName = elementName,
                    elementPackageName = elementPackageName,
                    to = target.asClassName(env),
                    from = paramFrom.asClassName(env),
                    component = paramComponent,
                    qualifier = qualifier,
                    scope = scope,
                    namedValue = namedValue,
                    mapKey = mapKey,
                    mapKeyParams = mapKeyParams
                )
            }
            (paramFrom == null && paramTo != null) -> {
                val to = target.findSuperTypeMirror(paramTo)
                if (to == null) {
                    logger.error(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                    throw IllegalStateException(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                }

                HiltMapBindsJavaParamsModel(
                    moduleFileName = moduleFileName,
                    elementName = elementName,
                    elementPackageName = elementPackageName,
                    to = to.getGenericTypeNames(env, paramCombined),
                    from = target.asClassName(env),
                    component = paramComponent,
                    qualifier = qualifier,
                    scope = scope,
                    namedValue = namedValue,
                    mapKey = mapKey,
                    mapKeyParams = mapKeyParams
                )
            }
            (paramFrom == null && paramTo == null) -> {
                val to = target.getSuperTypeMirror()
                if (to == null) {
                    logger.error(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                    throw IllegalStateException(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                }

                HiltMapBindsJavaParamsModel(
                    moduleFileName = moduleFileName,
                    elementName = elementName,
                    elementPackageName = elementPackageName,
                    to = to.getGenericTypeNames(env, paramCombined),
                    from = target.asClassName(env),
                    component = paramComponent,
                    qualifier = qualifier,
                    scope = scope,
                    namedValue = namedValue,
                    mapKey = mapKey,
                    mapKeyParams = mapKeyParams
                )
            }
            else -> {
                logger.error(Consts.ERROR_MSG_SIGNED_TOGETHER)
                throw IllegalStateException(Consts.ERROR_MSG_SIGNED_TOGETHER)
            }
        }
    }

    companion object {
        private const val ERROR_MSG_NOT_FOUND_KEY = "@HiltMapBinds must have Key annotation."
        private const val ERROR_MSG_PARAMS_EMPTY = "key annotation must not be empty."
    }
}