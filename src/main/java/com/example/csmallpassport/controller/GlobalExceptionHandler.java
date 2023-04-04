package com.example.csmallpassport.controller;

import com.example.csmallpassport.ex.ServiceException;
import com.example.csmallpassport.web.JsonResult;
import com.example.csmallpassport.web.ServiceCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 全局异常处理器
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public JsonResult handleServiceException(ServiceException e) {
        return JsonResult.fail(e);
    }

    @ExceptionHandler
    public JsonResult handleBindException(BindException e) {
        // 使用以下方式时，如果有多个检查错误，将可以反馈所有错误的信息
        // StringJoiner stringJoiner = new StringJoiner("，", "请求参数格式错误，", "！");
        // List<FieldError> fieldErrors = e.getFieldErrors();
        // for (FieldError fieldError : fieldErrors) {
        //     stringJoiner.add(fieldError.getDefaultMessage());
        // }
        // return stringJoiner.toString();

        // 使用以下方式时，如果有多个检查错误，
        // 将获取其中某1个检查注解中配置的message值，但无法确定到底是哪一个
        String message = e.getFieldError().getDefaultMessage();
        return JsonResult.fail(ServiceCode.ERR_BAD_REQUEST, message);
    }

    @ExceptionHandler({
//            IllegalStateException.class,
            InternalAuthenticationServiceException.class,
            BadCredentialsException.class
    })
    public JsonResult handleAuthenticationException(AuthenticationException e) {
        log.warn("程序运行过程中出现AuthenticationException，将统一处理！");
        log.warn("异常类型：{}", e.getClass().getName());
        log.warn("异常信息：{}", e.getMessage());
        String message = "登录失败，用户名或密码错误！";
        return JsonResult.fail(ServiceCode.ERR_UNAUTHORIZED, message);
    }

    @ExceptionHandler
    public JsonResult handleDisabledException(DisabledException e) {
        log.warn("程序运行过程中出现DisabledException，将统一处理！");
        log.warn("异常信息：{}", e.getMessage());
        String message = "登录失败，账号已经被禁用！";
        return JsonResult.fail(ServiceCode.ERR_UNAUTHORIZED_DISABLED, message);
    }


    @ExceptionHandler
    public JsonResult handleConstraintViolationException(ConstraintViolationException e) {
        String message = null;
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            message = constraintViolation.getMessage();
        }
        return JsonResult.fail(ServiceCode.ERR_BAD_REQUEST, message);
    }

    // 关于处理Throwable
    // 这是一能够避免项目出现500错误的做法
    // 这并不是处理每种异常的最优解，因为各种没有被针对性处理的异常都会由此处处理，所以，无法准确响应信息到客户端
    // 在处理过程中，必须了解并记录异常的类型、出现异常的原因等信息
    // 并且，后续应该补充针对性处理这些异常的方法
    // 在处理过程中，可以使用 e.printStackTrace() 以查看异常的跟踪信息
    // 注意：在生产环境中，通常是禁止使用 e.printStackTrace() 的
    @ExceptionHandler
    public JsonResult handleThrowable(Throwable e) {
        String message = "服务器忙，请稍后再尝试，或联系系统管理员！（在开发过程中，看到此消息，应该检查出现异常的原因，并补充对此类异常的针对性的处理）";
        log.warn("程序执行过程中出现Throwable！");
        log.warn("异常类型：{}", e.getClass().getName());
        log.warn("异常信息：{}", e.getMessage());
        log.warn("异常跟踪信息如下：", e); // 输出异常信息时，第1个参数中不要使用占位符
        // e.printStackTrace();
        return JsonResult.fail(ServiceCode.ERR_UNKNOWN, message);
    }

}
