/*
 * Copyright [2019] [恒宇少年 - 于起宇]
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */

package org.minbox.framework.api.boot.push.support;

import org.minbox.framework.api.boot.common.exception.ApiBootException;
import org.minbox.framework.api.boot.push.ApiBootMessagePushService;
import org.minbox.framework.api.boot.push.aop.holder.MessagePushContextHolder;
import org.minbox.framework.api.boot.push.model.PushClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * ApiBoot Message Push Service
 * Abstract Support
 *
 * @author 恒宇少年
 */
public abstract class ApiBootAbstractMessagePushServiceImpl implements ApiBootMessagePushService {
    /**
     * logger instance
     */
    static Logger logger = LoggerFactory.getLogger(ApiBootMessagePushJiGuangServiceImpl.class);
    /**
     * default client name
     */
    static final String DEFAULT_CLIENT_NAME = "default";
    /**
     * config push clients
     */
    private Map<String, PushClientConfig> pushClientConfigs;
    /**
     * is production
     */
    private boolean production;

    public ApiBootAbstractMessagePushServiceImpl(Map<String, PushClientConfig> pushClientConfigs, boolean production) {
        this.pushClientConfigs = pushClientConfigs;
        this.production = production;
    }

    /**
     * get current thread push client name
     *
     * @return push client name
     * @throws ApiBootException ApiBoot Exception
     */
    @Override
    public String getCurrentPushClientName() throws ApiBootException {
        String clientName = MessagePushContextHolder.get();
        logger.debug("Use [{}] push message client", clientName);
        // if client name is null
        // use default client name
        return StringUtils.isEmpty(clientName) ? DEFAULT_CLIENT_NAME : clientName;
    }

    /**
     * get current thread push client
     *
     * @return push message client
     * @throws ApiBootException ApiBoot Exception
     */
    @Override
    public PushClientConfig getCurrentPushClient() throws ApiBootException {
        String currentClientName = getCurrentPushClientName();
        return pushClientConfigs.get(currentClientName);
    }

    /**
     * is production
     *
     * @return true：production
     */
    public boolean isProduction() {
        return production;
    }
}
