package com.flowable.security;

import com.flowable.modules.user.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;


/**
 * @Author: liping.zheng
 * @Date: 2018/8/1
 */
public class SecurityUtil {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            user = (User) authentication.getPrincipal();
        }
        Assert.notNull(user,"User not logged in");
        return user;
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
