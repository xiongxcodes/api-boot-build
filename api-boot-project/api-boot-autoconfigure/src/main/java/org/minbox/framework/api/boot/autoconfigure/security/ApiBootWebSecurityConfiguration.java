/*
 * Copyright [2019] [恒宇少年 - 于起宇]
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.minbox.framework.api.boot.autoconfigure.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.minbox.framework.security.WebSecurityConfiguration;
import org.minbox.framework.security.handler.DefaultSecurityAccessDeniedHandler;
import org.minbox.framework.security.point.DefaultSecurityAuthenticationEntryPoint;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.util.ObjectUtils;

import de.codecentric.boot.admin.server.config.AdminServerProperties;

/**
 * ApiBoot integrates SpringSecurity's default automation configuration
 *
 * @author 恒宇少年
 * @see ApiBootWebSecurityMemoryConfiguration
 * @see ApiBootWebSecurityJdbcConfiguration
 */
public class ApiBootWebSecurityConfiguration extends WebSecurityConfiguration {

    protected ApiBootSecurityProperties apiBootSecurityProperties;
    protected AdminServerProperties adminServerProperties;
    private AccessDeniedHandler accessDeniedHandler;
    private AuthenticationEntryPoint authenticationEntryPoint;

    public ApiBootWebSecurityConfiguration(ApiBootSecurityProperties apiBootSecurityProperties,
        AdminServerProperties adminServerProperties,
        AccessDeniedHandler accessDeniedHandler, AuthenticationEntryPoint authenticationEntryPoint) {
        this.apiBootSecurityProperties = apiBootSecurityProperties;
        this.adminServerProperties = adminServerProperties;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    /**
     * Configure exclude permissions blocked path
     * <p>
     * By default, use {@link ApiBootSecurityProperties#DEFAULT_IGNORE_URLS}
     *
     * @return Path to be excluded
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected List<String> configureIgnoreUrls() {
        List<String> ignoringUrls = new ArrayList(Arrays.asList(ApiBootSecurityProperties.DEFAULT_IGNORE_URLS));
        if (!ObjectUtils.isEmpty(apiBootSecurityProperties.getIgnoringUrls())) {
            ignoringUrls.addAll(Arrays.asList(apiBootSecurityProperties.getIgnoringUrls()));
        }
        return ignoringUrls;
    }
    /**
     * Disable basic http
     *
     * @param http {@link HttpSecurity}
     * @throws Exception exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      /**
        http
      //表单登录,loginPage为登录请求的url,loginProcessingUrl为表单登录处理的URL
      .formLogin().loginPage("/login").failureForwardUrl("/erroraa")
      //允许访问
      .and().authorizeRequests().antMatchers(
      "/user/hello",
      "/oauthLogin",
      "/grant","/erroraa").permitAll().anyRequest().authenticated()
      //禁用跨站伪造
      .and().csrf();**/
        
        //http.authorizeRequests().anyRequest().authenticated();
        // 自动登录
        /*.and()
            .rememberMe()
            // 加密的秘钥
            .key("unique-and-secret")
            // 存放在浏览器端cookie的key
            .rememberMeCookieName("remember-me-cookie-name")
            // token失效的时间，单位为秒
            .tokenValiditySeconds(60 * 60 * 25)*/
        //.and()
        // 暂时禁用CSRF，否则无法提交登录表单
        //.csrf().disable();

        //http.authorizeRequests().anyRequest().authenticated().and().httpBasic().and().csrf().disable();
        //if (disableHttpBasic()) {
         //   http.httpBasic().disable();
        //}
        //if (disableCsrf()) {
        //    http.csrf().disable();
        //}
        String adminContextPath = adminServerProperties.getContextPath();
        SavedRequestAwareAuthenticationSuccessHandler successHandler =
            new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/");
        //super.configure(http);
        http.authorizeRequests()
        // 静态文件允许访问
        .antMatchers(adminContextPath + "/assets/**").permitAll()
        // 登录页面允许访问
        .antMatchers(adminContextPath + "/login", "/actuator/**", "/css/**", "/js/**", "/image/*").permitAll()
        // 其他所有请求需要登录
        .anyRequest().authenticated().and()
        // 登录页面配置，用于替换security默认页面
        .formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and()
        // 登出页面配置，用于替换security默认页面
        .logout().logoutUrl(adminContextPath + "/logout").and().httpBasic().and().csrf()
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    @Override
    protected AccessDeniedHandler getAccessDeniedHandler() {
        return ObjectUtils.isEmpty(this.accessDeniedHandler) ? new DefaultSecurityAccessDeniedHandler()
            : this.accessDeniedHandler;
    }

    @Override
    protected AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return ObjectUtils.isEmpty(this.authenticationEntryPoint) ? new DefaultSecurityAuthenticationEntryPoint()
            : this.authenticationEntryPoint;
    }

    @Override
    protected boolean disableHttpBasic() {
        return apiBootSecurityProperties.isDisableHttpBasic();
    }

    @Override
    protected boolean disableCsrf() {
        return apiBootSecurityProperties.isDisableCsrf();
    }
}
