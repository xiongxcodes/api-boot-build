/*
 * Copyright [2019] [恒宇少年 - 于起宇]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.minbox.framework.api.boot.autoconfigure.oauth;

import static org.minbox.framework.api.boot.autoconfigure.oauth.ApiBootOauthProperties.API_BOOT_OAUTH_PREFIX;

import java.util.List;

import javax.sql.DataSource;

import org.minbox.framework.oauth.grant.OAuth2TokenGranter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

/**
 * Jdbc authorization server
 *
 * @author 恒宇少年
 */
@SuppressWarnings("deprecation")
@ConditionalOnBean(DataSource.class)
@ConditionalOnProperty(prefix = API_BOOT_OAUTH_PREFIX, name = "away", havingValue = "jdbc")
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class ApiBootAuthorizationServerJdbcConfiguration extends ApiBootAuthorizationServerConfiguration {
    /**
     * logger instance
     */
    static Logger logger = LoggerFactory.getLogger(ApiBootAuthorizationServerJdbcConfiguration.class);
    private DataSource dataSource;

    public ApiBootAuthorizationServerJdbcConfiguration(ObjectProvider<List<OAuth2TokenGranter>> objectProvider, ApiBootOauthProperties apiBootOauthProperties, DataSource dataSource) {
        super(objectProvider, apiBootOauthProperties);
        this.dataSource = dataSource;
        logger.info("ApiBoot Oauth2 initialize using jdbc.");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    /**
     * Configure Jdbc {@link TokenStore}
     *
     * @return {@link JdbcTokenStore} instance
     */
    @Bean
    @Primary
    public TokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }
}
