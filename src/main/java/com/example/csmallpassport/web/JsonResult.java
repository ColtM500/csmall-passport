package com.example.csmallpassport.web;

import com.example.csmallpassport.ex.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor //无参的构造方法
@AllArgsConstructor   //全参的构造方法
@Accessors(chain = true) //支持链式写法
public class JsonResult implements Serializable {

    /**
     * 状态码
     */
    private Integer state;
    /**
     * 操作"失败"时的描述文本
     */
    private String message;
    /**
     * 操作成功时响应的数据
     */
    private Object data;

    /**
     * 调整代码 设置成功时为ok 状态码都为1
     * @return jsonResult结果值
     */
    public static JsonResult ok(){
        return ok(null);
    }

    public static JsonResult ok(Object data){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setState(com.example.csmallpassport.web.ServiceCode.OK.getValue());
        jsonResult.setData(data);
        return jsonResult;
    }


    public static JsonResult fail(ServiceException e){
//        JsonResult jsonResult = new JsonResult();
//        jsonResult.setState(state);
//        jsonResult.setMessage(message);
        return fail(e.getServiceCode(), e.getMessage());
    }

    /**
     * 调整代码 设置失败时 有不同的状态码和信息
     * @param serviceCode
     * @param message
     * @return
     */
    public static JsonResult fail(com.example.csmallpassport.web.ServiceCode serviceCode, String message){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setState(serviceCode.getValue());
        jsonResult.setMessage(message);
        return jsonResult;
    }


}
