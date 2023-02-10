package com.zxh.domain.vo;

import com.zxh.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGetInfoVo {
    private List<Long> roleIds;
    private List<Role> roles;
    private UserGetByIdVo user;
}
