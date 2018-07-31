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
@TableName("ft_profession_")
public class ProfessionInfo extends FtDomain {
    /**
     * 所属职业Id
     */
    @TableField
    private Long professionId;

    /**
     * 职业信息名称
     */
    @TableField
    private String professionInfoName;

    /**
     * 职业等级
     */
    @TableField
    private Integer professionLevel;
}
