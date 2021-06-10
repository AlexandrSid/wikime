<#include  "security.ftlh">
<#import "login.ftlh" as l>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary text-white">
    <a class="navbar-brand" href="/wikime">Wikime</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/wikime">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/articles">Articles</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="javascript:history.back()">Go Back</a>
            </li>
            <#if isAdmin>
                <li class="nav-item">
                    <a class="nav-link" href="/user">User List</a>
                </li>
            </#if>
            <#if user??>
                <li class="nav-item">
                    <a class="nav-link" href="/user/profile">Profile</a>
                </li>
            </#if>
        </ul>

        <div class="navbar-text mr-3"> <#if user??>${name}<#else>Please login</#if></div>
        <div>
            <@l.logout />
        </div>
    </div>
</nav>