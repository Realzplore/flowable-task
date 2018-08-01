package com.flowable.modules.user.service;

import com.flowable.modules.user.domain.User;
import com.flowable.modules.user.mapping.UserAuthorityMapper;
import com.flowable.modules.user.mapping.UserMapper;
import com.flowable.core.service.BaseService;
import org.apache.ibatis.javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author: liping.zheng
 * @Date: 2018/7/27
 */
@Service
public class UserService extends BaseService<UserMapper, User> implements UserDetailsService {
    @Autowired
    UserAuthorityMapper userAuthorityMapper;

    public User getParentUserById(Long id) throws ObjectNotFoundException {
        User user = baseMapper.selectById(id);
        if (user == null) {
            throw new ObjectNotFoundException("该用户不存在");
        }

        //忽略系统层级
        if (user.getParentUserId() == 0) {
            return user;
        }
        User parent = baseMapper.selectById(user.getParentUserId());
        return Optional.ofNullable(getUserAuthorities(parent)).orElse(null);
    }


    public User getAssignee(Long userId, Double decisionLevel) throws ObjectNotFoundException {
        User assignee = baseMapper.selectById(userId);
        if (assignee == null) {
            throw new ObjectNotFoundException("该用户不存在");
        }
        if (decisionLevel <= 0) {
            return assignee;
        }
        for (int i = 0; i < decisionLevel; i++) {
            User parent = getParentUserById(assignee.getId());
            if (parent.getId() == assignee.getId()) {
                break;
            }
            assignee = parent;
        }
        return getUserAuthorities(assignee);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = baseMapper.selectByUsername(username);
        return getUserAuthorities(user);
    }

    /**
     * 获取用户权限
     * @param user
     * @return
     */
    private User getUserAuthorities(User user) {
        if (user == null || user.getId() == null) {
            return null;
        }
        user.setAuthorityList(userAuthorityMapper.getAuthoritesByUserId(user.getId()));
        return user;
    }
}
