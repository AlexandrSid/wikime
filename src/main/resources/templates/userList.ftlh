<#import "parts/common.ftlh" as c>

<@c.page>
    <#if deletedUserName??>
        <div class="alert alert-dark alert-dismissible" role="alert">
            <span type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></span>
            <h5 class="alert-heading">User ${deletedUserName} successfully deleted</h5>
        </div>
    </#if>
    <#if activatedUserName??>
        <div class="alert alert-info alert-dismissible" role="alert">
            <span type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></span>
            <h5 class="alert-heading">User ${activatedUserName} successfully activated</h5>
        </div>
    </#if>
    List of Users

    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Roles</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <th>${user.getUsername()}</th>
                <th><#list user.roles as role>${role}<#sep>, </#list></th>
                <th><a href="user/${user.id}">edit</a></th>
            </tr>
        </#list>
        </tbody>
    </table>

</@c.page>