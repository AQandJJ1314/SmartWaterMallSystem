package com.atcode.watermall.product.exception;

import com.atcode.common.exception.BizCodeEnum;
import com.atcode.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一的异常处理  使用SpringMVC提供的 @ControllerAdvice
 * 集中处理所有异常
 * @ResponseBody 该注解用于返回json的数据
 * @RestControllerAdvice =  @ControllerAdvice + @ResponseBody
 * bindingResult.getFieldErrors() 获得数据校验的异常的结果
 */

/**
 * handleVaildException方法相当于一个精确的异常处理，当这个方法不能捕获到异常时，采用下面的更大范围的异常处理方法handleException
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.atcode.watermall.product.controller")
public class WatermallExceptionControllerAdvice {


    // 处理数据校验异常
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleVaildException(MethodArgumentNotValidException e){
        log.error("数据校验错误,错误原因{}，异常类型: {}",e.getMessage(),e.getClass());
        //拿到错误结果
        BindingResult bindingResult = e.getBindingResult();

        Map<String, String> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach((fieldError)->{
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(),BizCodeEnum.VALID_EXCEPTION.getMsg()).put("data", errorMap);
    }

    //处理全局异常
    @ExceptionHandler(value = Throwable.class)    //Exception和Error继承自Throwable
    public R handleException(Throwable throwable){
        log.error("错误: ",throwable);
        return R.error(BizCodeEnum.UNKNOW_EXEPTION.getCode(), BizCodeEnum.UNKNOW_EXEPTION.getMsg());
    }

}
