<#import "parts/common.ftlh" as c>
<#import "parts/util.ftlh" as u>

<@c.page>
<@u.util/>
    <#if article.id != 0>
        <div>
            <form method="post" action="/delete">
                <#--            <input type="hidden" name="_csrf" value="${_csrf.token}"/>-->
                <input type="hidden" name="id" value="${article.id}">
                <button type="submit">Delete</button>
            </form>
        </div>
    </#if>

    <div>
        <form method="post" action="/edit">
            <#--            <input type="hidden" name="_csrf" value="${_csrf.token}"/>-->
            <input type="hidden" name="id" value="${article.id}">
            <input type="text" name="header" placeholder="Enter Article Header" value="${article.header}"/><br>
            <input type="text" name="tags" placeholder="[Tag1, Tag2, a cat, ..., Last Tag]"
                   value="[<#list article.tags as tag>${tag}<#sep>, </#list>]"/><br>
            <input type="text" name="text" placeholder="[Paragraph1, Paragraph2, ..., ParagraphN]"
                   value="[<#list article.paragraphs as paragraph>${paragraph}<#sep>, </#list>]"/><br>
            <button type="submit"><#if article.id == 0>Добавить<#else>Сохранить</#if></button>
        </form>
    </div>

</@c.page>