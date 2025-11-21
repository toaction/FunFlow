## 参数校验

### 需求分析
对于参数的校验工作，应该放在 Controller 层或 Service 层。
- **关注点分离**：每层只关心自己职责范围内的校验
- **代码复用**：避免重复校验
- **安全性**：即使跳过 Controller 直接调用 Service，业务校验依然有效

### 设计方案
分层校验，各有侧重
- Controller 层：主要负责**基础格式校验**
- Service 层：主要负责**业务逻辑校验**

**Controller 层：**
请求参数的格式、类型、基本规则验证
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

**Service 层：**
业务规则、数据一致性、复杂逻辑验证
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

### 存在问题
- Service 方法可能被其他地方调用，是否需要添加参数校验注解？
