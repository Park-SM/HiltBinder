package com.smparkworld.hiltbinder_processor.java.generator.hiltbinds

import com.google.auto.service.AutoService
import com.smparkworld.hiltbinder.HiltBinds
import com.smparkworld.hiltbinder_processor.core.Consts
import com.smparkworld.hiltbinder_processor.core.Logger
import com.smparkworld.hiltbinder_processor.core.base.JavaParameterMapper
import com.smparkworld.hiltbinder_processor.core.manager.AnnotationManager
import com.smparkworld.hiltbinder_processor.java.extension.asClassName
import com.smparkworld.hiltbinder_processor.java.extension.findSuperTypeMirror
import com.smparkworld.hiltbinder_processor.java.extension.getGenericTypeNames
import com.smparkworld.hiltbinder_processor.java.extension.getPackageName
import com.smparkworld.hiltbinder_processor.java.extension.getSuperTypeMirror
import com.smparkworld.hiltbinder_processor.java.model.HiltBindsJavaParamsModel
import javax.annotation.processing.ProcessingEnvironment
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Scope
import javax.lang.model.element.Element
import kotlin.reflect.KClass

@AutoService(JavaParameterMapper::class)
internal class HiltBindsJavaParameterMapper : JavaParameterMapper<HiltBindsJavaParamsModel> {

    override fun getSupportedAnnotationType(): KClass<out Annotation> = HiltBinds::class

    override fun toParamsModel(env: ProcessingEnvironment, element: Element, logger: Logger): HiltBindsJavaParamsModel {
        val moduleFileName = "${element.simpleName}${Consts.MODULE_SUFFIX}"
        val elementName = element.simpleName.toString()
        val elementPackageName = env.getPackageName(element)

        val paramTo = AnnotationManager.getElementFromAnnotation<HiltBinds>(env, element, Consts.PARAM_TO)
        val paramFrom = AnnotationManager.getElementFromAnnotation<HiltBinds>(env, element, Consts.PARAM_FROM)
        val paramComponent = AnnotationManager.getElementFromAnnotation<HiltBinds>(env, element, Consts.PARAM_COMPONENT)
        val paramCombined = AnnotationManager.getValuesFromAnnotation<HiltBinds>(env, element)?.get(Consts.PARAM_COMBINED) as? Boolean
        val qualifier = AnnotationManager.getAnnotationByParentAnnotation(env, element, Qualifier::class, Named::class)
        val scope = AnnotationManager.getAnnotationByParentAnnotation(env, element, Scope::class)
        val namedValue = AnnotationManager.getValuesFromAnnotation<Named>(env, element)?.get(Consts.NAMED_PARAM) as? String

        return when {
            (paramFrom != null && paramTo == null) -> {
                HiltBindsJavaParamsModel(
                    moduleFileName = moduleFileName,
                    elementName = elementName,
                    elementPackageName = elementPackageName,
                    to = element.asClassName(env),
                    from = paramFrom.asClassName(env),
                    component = paramComponent,
                    qualifier = qualifier,
                    scope = scope,
                    namedValue = namedValue
                )
            }
            (paramFrom == null && paramTo != null) -> {
                val to = element.findSuperTypeMirror(paramTo)
                if (to == null) {
                    logger.error(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                    throw IllegalStateException(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                }

                HiltBindsJavaParamsModel(
                    moduleFileName = moduleFileName,
                    elementName = elementName,
                    elementPackageName = elementPackageName,
                    to = to.getGenericTypeNames(env, paramCombined),
                    from = element.asClassName(env),
                    component = paramComponent,
                    qualifier = qualifier,
                    scope = scope,
                    namedValue = namedValue
                )
            }
            (paramFrom == null && paramTo == null) -> {
                val to = element.getSuperTypeMirror()
                if (to == null) {
                    logger.error(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                    throw IllegalStateException(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                }

                HiltBindsJavaParamsModel(
                    moduleFileName = moduleFileName,
                    elementName = elementName,
                    elementPackageName = elementPackageName,
                    to = to.getGenericTypeNames(env, paramCombined),
                    from = element.asClassName(env),
                    component = paramComponent,
                    qualifier = qualifier,
                    scope = scope,
                    namedValue = namedValue
                )
            }
            else -> {
                logger.error(Consts.ERROR_MSG_SIGNED_TOGETHER)
                throw IllegalStateException(Consts.ERROR_MSG_SIGNED_TOGETHER)
            }
        }
    }
}