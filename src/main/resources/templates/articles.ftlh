<#import "parts/common.ftlh" as c>
<#import "parts/util.ftlh" as u>
<@c.page>
    <@u.util/>
    <div>Articles: </div>
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
</@c.page>