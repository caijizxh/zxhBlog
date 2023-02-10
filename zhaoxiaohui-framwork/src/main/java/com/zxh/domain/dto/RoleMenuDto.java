package com.zxh.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RoleMenuDto {
    private List<MenuTreeDto> menus;
    private List<Long> checkedKeys;
}
