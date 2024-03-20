package com.example.demo.src.user.model;

import com.example.demo.common.Constant;
import com.example.demo.common.Constant.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchUserReq {
    private String name;
}
