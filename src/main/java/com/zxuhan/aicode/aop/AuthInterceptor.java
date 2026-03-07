package com.zxuhan.aicode.aop;

import com.zxuhan.aicode.annotation.AuthCheck;
import com.zxuhan.aicode.exception.BusinessException;
import com.zxuhan.aicode.exception.ErrorCode;
import com.zxuhan.aicode.model.entity.User;
import com.zxuhan.aicode.model.enums.UserRoleEnum;
import com.zxuhan.aicode.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * Intercept invocations and apply role checks.
     *
     * @param joinPoint the join point
     * @param authCheck the auth check annotation
     * @return the original method result
     * @throws Throwable any error thrown by the proceeding method
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // Get the currently logged-in user
        User loginUser = userService.getLoginUser(request);
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        // No required role, allow through
        if (mustRoleEnum == null) {
            return joinPoint.proceed();
        }
        // The remaining code requires the user to have the specified role
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        // No role at all, reject
        if (userRoleEnum == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // Admin role required but the user is not an admin
        if (UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // Authenticated user has the required role, allow through
        return joinPoint.proceed();
    }
}

























