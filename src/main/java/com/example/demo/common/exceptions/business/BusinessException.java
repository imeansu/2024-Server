package com.example.demo.common.exceptions.business;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.common.response.BusinessResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends BaseException {
    private BusinessResponseStatus businessResponseStatus;


    public BusinessException(BusinessResponseStatus status) {
        super(BaseResponseStatus.SUCCESS);
        this.businessResponseStatus = status;
    }
}
