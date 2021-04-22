<#import "parts/common.ftlh" as c>

<@c.page>
    <div>
        <h1>
            Welcome to wikime, your personal databased wiki
        </h1>
    </div>
    <form method="get" action="/articles">
        <input type="text" name="filter" placeholder="Enter tags separated by ', ': (Tag1, Tag_2, ... , #Cat)">
        <button type="submit">Search</button>
    </form>
    <form action="/articles" method="get">
        <button type="submit">All articles</button>
    </form>
</@c.page>