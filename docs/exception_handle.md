## 异常处理
### 自定义异常体系
```java
// 基础业务异常
public class BaseException extends RuntimeException {
    private String code;
    private String message;
    
    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}

// 具体业务异常
public class BusinessException extends BaseException {
    public BusinessException(String message) {
        super("BUSINESS_ERROR", message);
    }
    
    public BusinessException(String code, String message) {
        super(code, message);
    }
}
```
### 全局异常处理
捕获的异常类型：

| 校验场景 | 抛出的异常 | 说明 |
| --- | --- | --- |
| Controller 方法参数 | `MethodArgumentNotValidException` | 校验 `@RequestBody` 参数 |
| 普通方法参数 | `ConstraintViolationException` | 校验普通方法参数 |
| 路径变量/请求参数 | `ConstraintViolationException` | 校验 `@PathVariable`, `@RequestParam` 等 |
| Service 业务逻辑 | `BusinessException` | 业务规则校验、数据一致性校验等 |

```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    // 处理 @RequestBody + @Valid 参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleValidationException(MethodArgumentNotValidException e,
                                                       HttpServletRequest request) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        log.warn("参数校验失败: {}", message);
        return ApiResponse.error("VALIDATION_ERROR", message);
    }

    // 处理普通参数校验异常
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse handleConstraintViolationException(
            ConstraintViolationException e, HttpServletRequest request) {
        String message = e.getConstraintViolations().stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse("参数校验失败");
        
        log.warn("方法参数校验失败: {}", message);
        return ApiResponse.error("VALIDATION_ERROR", message);
    }

    
    // 处理业务异常
    @ExceptionHandler(BusinessException.class)
    public ApiResponse handleBusinessException(BusinessException e, 
                                                     HttpServletRequest request) {
        log.warn("业务异常: {}", e.getMessage());
        return ApiResponse.error(e.getCode(), e.getMessage());
    }
    
    // 处理所有未捕获的异常
    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常: ", e);
        // 生产环境隐藏详细错误信息
        String message = isProduction() ? "系统繁忙，请稍后重试" : e.getMessage();
        return ApiResponse.error("SYSTEM_ERROR", message);
    }
}
```
