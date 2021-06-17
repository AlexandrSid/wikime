<#import "parts/common.ftlh" as c>
<@c.page>
    <h5>${username}</h5>
    ${message?if_exists}
    <#if password_changed??>
        <div class="alert alert-success alert-dismissible" role="alert">
            <span type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></span>
            <h5 class="alert-heading">Password successfully changed!</h5>
        </div>
    </#if>

    <form method="post">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> Password: </label>
            <div class="col-sm-6">
                <input type="password" class="form-control" name="password" placeholder="Enter new password"/>
            </div>
        </div>
<#--        email в логике этого приложения лишний-->
<#--        <div class="form-group row">-->
<#--            <label class="col-sm-2 col-form-label"> Email: </label>-->
<#--            <div class="col-sm-6">-->
<#--                <input type="email" class="form-control" name="email" placeholder="${email!''}"/>-->
<#--            </div>-->
<#--        </div>-->
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <div>
            <button type="submit" class="btn btn-primary">Save</button>
        </div>
    </form>
</@c.page>