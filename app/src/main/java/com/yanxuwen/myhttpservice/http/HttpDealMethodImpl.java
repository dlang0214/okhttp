package com.yanxuwen.myhttpservice.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.http.compiler.HttpDealMethod;
import com.http.compiler.bean.CallBack;
import com.http.compiler.bean.DealParams;
import java.util.Map;

/**
 * 统一处理方法跟回调
 */
public class HttpDealMethodImpl implements HttpDealMethod {

    /**
     * 处理请求
     * 如果处理后，各种字段都有，则会优先表单请求，然后再者json请求
     * 注意一定要return dealParams 不然不处理
     */
    @Override
    public DealParams dealRequest(DealParams dealParams) {
        //设置Cookie
        dealParams.addHeader("Cookie","JSESSIONID=AE7B1C9D73D448EEAECF5EC8363C55B0"
                + ";ClientVersion=6.8.1");
        //设置表单参数
        dealParams.addField("reqcodeversion","6.8");
        //获取@Params里的参数，然后设置成json串，设置到表单body里
        Map<String, Object> mapParams = dealParams.getParams();
        JSONObject jb = new JSONObject();
        String json = "";
        for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
            try {
                jb.put(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
            }
        }
        json = jb.toString();
        dealParams.addField("body",json);
        return dealParams;
    }

    /**
     * 处理回调,
     * 如果要设置返回错误，则new CallBack(-1,"请求失败") ，第一个参数不能为0即可，0代表成功
     * 如果要请求成功，直接 new CallBack(jsonStr)
     * return null 则不做任何处理
     */
    @Override
    public CallBack dealCallBack(int httpCode , String json) {
        String jsonStr = null;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            jsonStr = jsonObject.getString("body");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new CallBack(jsonStr);
    }
}
