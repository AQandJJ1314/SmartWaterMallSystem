package com.atcode.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
//约束。同一个注解可以指定多个不同的校验器，适配不同类型的校验。这里ListValueConstraintValidator.class是数值校验器
@Constraint(validatedBy = {ListValueConstraintValidator.class})
//可以标注在哪些位置。方法、字段等。
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
//注解的时机。这里是可以在运行时获取校验
@Retention(RetentionPolicy.RUNTIME)
public @interface ListValue {
    //    校验出错后，错误信息去哪取。前缀一般是当前全类名，在ValidationMessages.properties配置文件里设置com.atcode.common.validator.ListValue.message=The specified value must be submitted
    String message() default "{com.atcode.common.validator.ListValue.message}";
    //    支持分组校验的功能
    Class<?>[] groups() default {};
    //    自定义负载信息
    Class<? extends Payload>[] payload() default {};
    //    自定义注解里的属性
    int[] vals() default {};
}
