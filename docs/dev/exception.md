## 参数校验

### 需求分析
方法参数的校验工作分为基础格式校验和业务逻辑校验。基础格式校验用于判断后端接收到的参数是否符合预定义的规则，格式是否正确；业务逻辑校验用于确保参数符合业务规则，保证数据一致性。

对于参数的校验工作，应该放在 Controller 层和 Service 层：
- Controller 层负责基础格式校验
- Service 层负责业务逻辑校验
- 每层只关心自己职责范围内的校验，避免重复校验

### 设计方案
**Controller**: 请求参数的格式、类型、基本规则验证
```java
// DTO 中使用校验注解
@Data
public class UserCreateRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度2-20位")
    private String username;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", 
             message = "密码必须包含大小写字母和数字，至少8位")
    private String password;
}
```
```java
@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    
    @PostMapping
    public Response createUser(@Valid @RequestBody UserCreateRequest request) {
        return Response.ok(userService.createUser(request));
    }
}
```

**Service**：业务规则、数据一致性、复杂逻辑验证
```java
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User createUser(UserCreateRequest request) {
        // 业务逻辑校验：邮箱唯一性 （示例，可以放在事务提交阶段校验唯一性）
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("邮箱已被注册");
        }
        
        // 复杂业务校验
        if (weakPasswordRepository.isWeakPassword(request.getPassword())) {
            throw new BusinessException("密码在弱密码库中，请更换");
        }
        //....
    }
}
```

## 异常处理

### 异常类型

| 校验场景 | 抛出的异常 | 说明                                       |
| --- | --- |------------------------------------------|
| Controller 方法参数 | `MethodArgumentNotValidException` | 使用 `@Valid` + `@RequestBody` 注解校验参数          |
| 路径变量/请求参数 | `ConstraintViolationException` | 使用 `@PathVariable`, `@RequestParam` 等校验参数 |
| Service 业务逻辑 | `BusinessException` | 业务规则校验、数据一致性校验等                          |

### 自定义异常体系

基础业务异常：
```java
public class BaseException extends RuntimeException {
    private String code;
    private String message;
    
    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
```

具体业务异常：
```java
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
使用 `@ControllerAdvice` 来统一处理异常，返回自定义的错误格式。

```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    // 处理 @RequestBody + @Valid 参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidationException(MethodArgumentNotValidException e,
                                                       HttpServletRequest request) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        log.warn("后端接收参数校验失败: {}", message);
        return Result.error(Code.VALIDATION_ERROR, message);
    }

    // 处理普通参数校验异常
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleConstraintViolationException(
            ConstraintViolationException e, HttpServletRequest request) {
        String message = e.getConstraintViolations().stream()
                .findFirst()
                .map(ConstraintViolation::getMessage);
        
        log.warn("方法参数校验失败: {}", message);
        return ApiResponse.error(Code.VALIDATION_ERROR, message);
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
        return ApiResponse.error(Code.SYSTEM_ERROR, message);
    }
}
```
