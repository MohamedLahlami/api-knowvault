<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=!messagesPerField.existsError('username','password') displayInfo=realm.password && realm.registrationAllowed && !registrationDisabled??>
    <#if section = "header">
        ${msg("doLogIn")}
    <#elseif section = "form">
    <div id="kc-form">
      <div id="kc-form-wrapper">
        <#if realm.password>
            <form id="kc-form-login" onsubmit="login.disabled = true; return true;" action="${url.loginAction}" method="post">
                <div class="form-group">
                    <label for="username" class="${properties.kcLabelClass!} <#if !realm.loginWithEmailAllowed>required</#if>">
                        <#if !realm.loginWithEmailAllowed>${msg("username")}<#elseif !realm.duplicateEmailsAllowed>${msg("usernameOrEmail")}<#else>${msg("username")}</#if>
                    </label>

                    <#if usernameEditDisabled??>
                        <input tabindex="1" id="username" class="form-control" name="username" value="${(login.username!'')}" type="text" disabled />
                    <#else>
                        <input tabindex="1" id="username" class="form-control" name="username" value="${(login.username!'')}"  type="text" autofocus autocomplete="off"
                               aria-invalid="<#if messagesPerField.existsError('username','password')>true</#if>"
                        />

                        <#if messagesPerField.existsError('username','password')>
                            <span id="input-error-username-password" class="kc-feedback-text" aria-live="polite">
                                        ${kcSanitize(messagesPerField.getFirstError('username','password'))?no_esc}
                            </span>
                        </#if>

                    </#if>
                </div>

                <div class="form-group">
                    <label for="password" class="${properties.kcLabelClass!} required">${msg("password")}</label>
                    <input tabindex="2" id="password" class="form-control" name="password" type="password" autocomplete="off"
                           aria-invalid="<#if messagesPerField.existsError('username','password')>true</#if>"
                    />
                </div>

                <div class="login-pf-settings">
                    <#if realm.rememberMe && !usernameEditDisabled??>
                        <div class="checkbox">
                            <input tabindex="3" id="rememberMe" name="rememberMe" type="checkbox" <#if login.rememberMe??>checked</#if>>
                            <label for="rememberMe">${msg("rememberMe")}</label>
                        </div>
                    </#if>
                    <#if realm.resetPasswordAllowed>
                        <div>
                            <a tabindex="5" href="${url.loginResetCredentialsUrl}">${msg("doForgotPassword")}</a>
                        </div>
                    </#if>
                </div>

                <div id="kc-form-buttons" class="form-group">
                    <input type="hidden" id="id-hidden-input" name="credentialId" <#if auth.selectedCredential?has_content>value="${auth.selectedCredential}"</#if>/>
                    <input tabindex="4" class="btn btn-primary" name="login" id="kc-login" type="submit" value="${msg("doLogIn")}"/>
                </div>
            </form>
        </#if>
        </div>

        <#if realm.password && social.providers??>
            <div id="kc-social-providers" class="kc-social-links">
                <div class="divider">
                    <span>${msg("identity-provider-login-label")}</span>
                </div>
                <ul>
                    <#list social.providers as p>
                        <li><a id="social-${p.alias}" class="btn btn-secondary" type="button" href="${p.loginUrl}">${p.displayName!}</a></li>
                    </#list>
                </ul>
            </div>
        </#if>

        <#if realm.password && realm.registrationAllowed && !registrationDisabled??>
            <div id="kc-registration" class="login-pf-signup">
                <p>${msg("noAccount")}</p>
                <a tabindex="6" href="${url.registrationUrl}">${msg("doRegister")}</a>
            </div>
        </#if>
    </div>
    </#if>

</@layout.registrationLayout>
