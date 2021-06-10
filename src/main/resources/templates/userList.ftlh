<#import "parts/common.ftlh" as c>

<@c.page>
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