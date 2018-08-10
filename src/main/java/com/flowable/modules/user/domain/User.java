package com.flowable.modules.user.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.flowable.core.domain.FtDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * @Author: liping.zheng
 * @Date: 2018/7/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ft_user")
public class User extends FtDomain implements UserDetails {
    /**
     * 登录名
     */
    @TableField
    private String username;

    /**
     * 用户密码
     */
    @TableField
    @JsonIgnore
    private String password;

    /**
     * 用户名称
     */
    @TableField
    private String name;

    /**
     * 人员架构组Id
     */
    @TableField
    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;

    /**
     * 职业信息Id
     */
    @TableField
    @JsonSerialize(using = ToStringSerializer.class)
    private Long professionInfoId;

    /**
     * 上级Id
     */
    @TableField
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentUserId;

    @TableField(exist = false)
    private Set<String> authorityList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        for (String authority : authorityList) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
            authorities.add(simpleGrantedAuthority);
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User(" +
                "id="+ this.getId()+
                ",username="+this.getUsername()+
                ",name="+this.getName()+
                ",groupId="+this.getGroupId()+
                ",authorityList="+this.getAuthorityList()
                +")";
    }

}
