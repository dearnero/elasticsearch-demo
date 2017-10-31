package com.nero.elasticsearch;

import lombok.Data;

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
public class User implements Serializable {

    /**
     * id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 所在科室
     */
    private Integer hid;
}
