package com.flowable.modules.group.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.flowable.core.domain.FtDomain;
import lombok.Data;

/**
 * @Author: liping.zheng
 * @Date: 2018/7/27
 */
@Data
@TableName("ft_group")
public class Group extends FtDomain {
    /**
     * 人员架构组名称
     */
    @TableField
    private String groupName;

    /**
     * 父架构组id
     */
    @TableField
    private Long parentGroupId;
}
