package com.flowable.modules.profession.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.flowable.core.domain.FtDomain;
import lombok.Data;


/**
 * @Author: liping.zheng
 * @Date: 2018/7/27
 */
@Data
@TableName("ft_profession")
public class Profession extends FtDomain {
    /**
     * 职业名称
     */
    @TableField
    private String professionName;
}
