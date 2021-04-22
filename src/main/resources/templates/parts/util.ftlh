<#macro  util>
    <div>
        <table>
            <tr>
                    <td><button onclick="window.history.back()">Back</button></td>
                    <td>
                        <form action="/wikime" method="get">
                            <button type="submit">Home</button>
                        </form>
                    </td>
                    <td>
                        <form action="/articles" method="get">
                            <button type="submit">Main</button>
                        </form>
                    </td>
            </tr>
        </table>
    </div>
</#macro>