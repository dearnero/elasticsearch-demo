package com.nero.elasticsearch.elasticsearch;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 用户实体
 * <p>
 * date : 2017/10/31
 * time : 16:07
 * </p>
 *
 * @author Nero
 */
@Data
@ToString
public class User implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String desc;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 所在科室
     */
    private Integer hid;
}
