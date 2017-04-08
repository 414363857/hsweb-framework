/*
 *  Copyright 2016 http://www.hswebframework.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package org.hswebframework.web.service.oauth2.client.simple.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.hswebframework.web.authorization.oauth2.client.response.OAuth2Response;
import org.hswebframework.web.service.oauth2.client.request.ProviderSupport;
import org.hswebframework.web.service.oauth2.client.request.definition.ResponseJudgeForProviderDefinition;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO 完成注释
 *
 * @author zhouhao
 */
@Component
public class HswebResponseJudgeSupport implements ResponseJudgeForProviderDefinition {
    static Map<Integer, OAuth2Response.ErrorType> errorTypeMap = new HashMap<>();

    static {
        // success
        errorTypeMap.put(401, OAuth2Response.ErrorType.ILLEGAL_RESPONSE_TYPE);
        errorTypeMap.put(500, OAuth2Response.ErrorType.ILLEGAL_RESPONSE_TYPE);

    }

    @Override
    public String getProvider() {
        return ProviderSupport.hsweb;
    }

    @Override
    public OAuth2Response.ErrorType judge(OAuth2Response response) {
        String result = response.asString();
        if (result == null) return OAuth2Response.ErrorType.OTHER;
        JSONObject jsonRes = JSON.parseObject(result);
        Integer status = jsonRes.getInteger("status");
        if (status == null && response.status() == 200) return null;
        if (status != null) {
            if (status == 200) return null;
            return errorTypeMap.getOrDefault(status, OAuth2Response.ErrorType.OTHER);
        }
        return errorTypeMap.getOrDefault(response.status(), OAuth2Response.ErrorType.OTHER);
    }
}
