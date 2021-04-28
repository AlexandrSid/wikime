<#import "parts/common.ftlh" as c>
<#import "parts/util.ftlh" as u>

<@c.page>
<#--    <@u.util/>-->
    <#if article??>
        <div>
<#--            <form action="/edit" method="get">-->
<#--                <input type="hidden" name="id" value="${article.id}">-->
<#--                <button type="submit">Edit Article</button>-->
<#--            </form>-->
            <a class="btn btn-warning" href="/edit?id=${article.id}" role="button">Edit Article</a>
        </div>
        <div>
            <h3>${article.header}</h3>
        </div>
        <div>
            <#list article.tags as tag>
                <i>${tag.tag}</i>
            </#list>
        </div>
        <div>
            <#list article.paragraphs as paragraph>
                <br>${paragraph}
            </#list>
        </div>
    <#else >
        <div>
            <h3>There is no article with id=${RequestParameters.id}</h3>
        </div>
    </#if>
</@c.page>