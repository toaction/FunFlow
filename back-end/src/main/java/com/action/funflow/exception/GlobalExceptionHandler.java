package com.action.funflow.exception;

import com.action.funflow.common.Code;
import com.action.funflow.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 统一处理系统中的各类异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * test：测试环境
     * dev：开发环境
     * prod：生产环境
     */
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;
    
    /**
     * 处理 @RequestBody + @Valid 参数校验异常
     * 主要用于 Controller 方法参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e,
                                                  HttpServletRequest request) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        log.warn("参数校验失败 [URI: {}]: {}", request.getRequestURI(), message);
        return Result.error(Code.VALIDATION_ERROR, message);
    }
    
    /**
     * 处理普通参数校验异常
     * 主要用于 @PathVariable, @RequestParam 等参数校验
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(
            ConstraintViolationException e, HttpServletRequest request) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        
        log.warn("方法参数校验失败 [URI: {}]: {}", request.getRequestURI(), message);
        return Result.error(Code.VALIDATION_ERROR, message);
    }
    
    /**
     * 处理业务异常
     * 自定义的业务逻辑异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e,
                                                 HttpServletRequest request) {
        log.warn("业务异常 [URI: {}]: {}", request.getRequestURI(), e.getMessage());
        return Result.error(Code.BUSINESS_ERROR, e.getMessage());
    }
    
    /**
     * 处理所有未捕获的异常
     * 兜底异常处理
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常 [URI: {}]: ", request.getRequestURI(), e);
        
        // 生产环境隐藏详细错误信息
        String message = isProduction() ? "系统繁忙，请稍后重试" : e.getMessage();
        return Result.error(Code.SYSTEM_ERROR, message);
    }
    
    /**
     * 判断是否为生产环境
     */
    private boolean isProduction() {
        return "prod".equalsIgnoreCase(activeProfile) || "production".equalsIgnoreCase(activeProfile);
    }

}
