package com.example.demo.common.exceptions.business;

import com.example.demo.common.Constant.TermsType;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;

import java.util.List;
import java.util.Set;

public class TermsNotAgreedException  extends BaseException {

    private Set<TermsType> notAgreedTerms;
    public TermsNotAgreedException(BaseResponseStatus status) {
        super(status);
    }

    public TermsNotAgreedException(BaseResponseStatus status, Set<TermsType> notAgreedTerms) {
        super(status);
        this.notAgreedTerms = notAgreedTerms;
    }

    @Override
    public Set<TermsType> getResult() {
        return notAgreedTerms;
    }
}
