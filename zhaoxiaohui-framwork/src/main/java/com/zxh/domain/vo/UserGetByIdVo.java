package com.zxh.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGetByIdVo {
    /**
     * 主键
     */
    private Long id;
    //用户名
    private String userName;
    /**
     * 昵称
     */
    private String nickName;


    private String sex;

    private String email;

    //账号状态（0正常 1停用）
    private String status;
}
