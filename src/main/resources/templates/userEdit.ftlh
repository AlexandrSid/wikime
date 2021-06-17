<#import "parts/common.ftlh" as c>

<@c.page>
    User editor
    <form action="/user" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <input type="hidden" value="${user.id}" name="userId">
        <input type="text" value="${user.username}" name="username">
        <#list roles as role>
            <div>
                <label><input type="checkbox"
                              name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}</label>
            </div>
        </#list>
        <button type="submit">Save</button>
    </form>
    <form action="/user/delete" method="post">
        <input type="hidden" value="${user.username}" name="username">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <input type="hidden" value="${user.id}" name="userId">
        <button type="submit">Delete User</button>
    </form>
    <#if !user.isActive() >
        <form action="/user/activate" method="post">
            <input type="hidden" value="${user.username}" name="username">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <input type="hidden" value="${user.id}" name="userId">
            <button type="submit">Activate User</button>
        </form>
    </#if>
</@c.page>