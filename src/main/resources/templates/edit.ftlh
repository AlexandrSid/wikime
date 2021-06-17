<#import "parts/common.ftlh" as c>
<#import "parts/util.ftlh" as u>
<#assign isNew = article.id==0>

<@c.page>
<#--<@u.util/>-->
    <#if !isNew>
        <div>
            <form method="post" action="/delete">
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <input type="hidden" name="id" value="${article.id}">
                <button class="btn btn-danger" type="submit">Delete Article</button>
            </form>
        </div>
    </#if>

    <#if isNew>
        <div>
            <h3>
                Create new Article
            </h3>
        </div>
    </#if>

    <#--gt means greater than -->
    <#if article.id gt 0>
        <div class="my-2">
            <h6>The article created by <span class="badge badge-secondary"><#if article.author??>${article.author.getUsername()}<#else>none</#if></span></h6>
        </div>
    </#if>
    <div>
        <form method="post" action="/edit">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <div class="form-group">
                <input type="hidden" name="id" value="${article.id}">
            </div>
            <div class="form-group">
                <input type="hidden" name="author" value="${article.getAuthorName()}">
            </div>
            <div class="form-group">
                <input class="form-control col-sm-6" type="text" name="header" placeholder="Enter Article Header"
                       <#if !isNew>value="${article.header}"</#if>/>
            </div>
            <div class="form-group">
                <input class="form-control col-sm-6" type="text" name="tags"
                       placeholder="enter tags separtated by ', ': [Tag1, Tag2, a cat, ..., Last Tag]"
                       <#if !isNew>value="[<#list article.tags as tag>${tag}<#sep>, </#list>]"</#if>
                />
            </div>
            <div class="form-group">
                <input class="form-control col-sm-6" type="text" name="text"
                       placeholder="[Paragraph1, Paragraph2, ..., ParagraphN]"
                       <#if !isNew>value="[<#list article.paragraphs as paragraph>${paragraph}<#sep>, </#list>]"</#if>
                />
            </div>
            <button class="btn btn-primary" type="submit"><#if isNew>Add Article<#else>Save</#if></button>
        </form>
    </div>

</@c.page>