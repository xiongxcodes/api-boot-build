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

import org.minbox.framework.api.boot.autoconfigure.security.ApiBootSecurityProperties;
import org.minbox.framework.oauth.AuthorizationServerConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * Authorization server configuration
 *
 * @author 恒宇少年
 */
@SuppressWarnings("deprecation")
@Configuration
@ConditionalOnClass(AuthorizationServerConfiguration.class)
@EnableConfigurationProperties({ApiBootSecurityProperties.class, ApiBootOauthProperties.class})
@ConditionalOnProperty(prefix = API_BOOT_OAUTH_PREFIX, name = "enable-authorization", havingValue = "true", matchIfMissing = true)
@EnableAuthorizationServer
@Import({ApiBootAuthorizationMemoryServerConfiguration.class,ApiBootAuthorizationServerJdbcConfiguration.class,ApiBootAuthorizationServerRedisConfiguration.class})
public class ApiBootAuthorizationServerAutoConfiguration {

}
