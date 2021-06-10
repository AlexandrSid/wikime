<#include  "security.ftlh">

<#macro login path isRegisterForm>
    <form action="${path}" method="post">
        <#--username field-->
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> User Name :</label>
            <div class="col-sm-6">
                <input type="text"
                       class="form-control ${(usernameError??)?string('is-invalid', '')}"
                       name="username"
                       value="<#if user??>${user.username}</#if>"
                       placeholder="User name"/>
                <#if usernameError??>
                    <div class="invalid-feedback">
                        ${usernameError}
                    </div>
                </#if>
            </div>
        </div>
        <#--password field-->
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> Password: </label>
            <div class="col-sm-6">
                <input type="password"
                       class="form-control ${(passwordError??)?string('is-invalid', '')}"
                       name="password"
                       placeholder="Password"/>
                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>
        </div>



        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <#if !isRegisterForm>
            <a href="/registration">Add new user</a>
        </#if>
        <div>
            <button type="submit" class="btn btn-primary"><#if isRegisterForm>Create<#else>Sign In</#if></button>
        </div>
    </form>
</#macro>
<#macro  logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary"><#if user??>Sign Out<#else>Log In</#if></button>
    </form>
</#macro>