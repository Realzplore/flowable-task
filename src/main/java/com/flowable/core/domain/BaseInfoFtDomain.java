package com.flowable.core.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * @Author: liping.zheng
 * @Date: 2018/8/3
 */
@Data
public abstract class BaseInfoFtDomain extends FtDomain {
    @TableField
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createdBy;

    @TableField
    private ZonedDateTime createdDate;

    @TableField
    @JsonSerialize(using = ToStringSerializer.class)
    private Long lastUpdatedBy;

    @TableField
    private ZonedDateTime lstUpdatedDate = ZonedDateTime.now();
}
