package com.example.csmallpassport.ex;

import com.example.csmallpassport.web.ServiceCode;
import lombok.Getter;

public class ServiceException extends RuntimeException{

    /**
     * 状态代码
     * @param message
     */
    @Getter
    private ServiceCode serviceCode;

    public ServiceException(ServiceCode serviceCode, String message){
        super(message);
        this.serviceCode = serviceCode;
    }


}
