<#import "parts/common.ftlh" as c>
<#import "parts/util.ftlh" as u>
<@c.page>
<#--    <@u.util/>-->
    <div><h4>Articles: </h4></div>
    <#list articles as article>
        <div>
            <a href="article?id=${article.id}">
            <b>${article.header}</b>
            <#list article.tags as tag>
                <span>${tag.tag}</span>
            </#list>
                </a>
        </div>
    <#else>
        No articles
    </#list>
    <div>
        <a class="btn btn-primary my-4" href="/new" role="button">Create new article</a>
    </div>
</@c.page>