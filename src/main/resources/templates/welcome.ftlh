<#import "parts/common.ftlh" as c>

<@c.page>
    <div>
        <h1>
            Welcome to wikime, your personal databased wiki!
        </h1>
        <h3>
            This is version with very basic functionality and no DB included.
        </h3>
    </div>
    <div class-="m-2">
    <form class="form-inline" method="get" action="/articles">
        <label class="sr-only" for="tagsToSearch">Tags</label>
        <input class="form-control col-sm-8" id="tagsToSearch" type="text" name="filter" placeholder="Enter tags separated by ', ': Tag1, Tag_2, ... , #Cat">
        <button type="submit" class="btn btn-primary mx-2">Search</button>
        <a class="btn btn-primary" href="/articles" role="button">All articles</a>
    </form>
    </div>
</@c.page>