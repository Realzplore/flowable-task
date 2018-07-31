package com.flowable.core.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: liping.zheng
 * @Date: 2018/7/27
 */
@Data
public class FtDomain implements Serializable {
    @TableId
    private Long id;
}
