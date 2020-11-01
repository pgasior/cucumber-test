<#macro restQueryData(restQuery) >
<#--    <span>-->
<#--      <button type="button" class="btn btn-success btn-sm" data-toggle="collapse"-->
<#--              data-target="#restModal-${number}">-->
<#--          REST Query-->
<#--      </button>-->
<#--    </span>-->
    <!-- Modal -->
    <div class="rest-query-details">
        <div>
            <div class="card">
                <div class="card-body">
                    <h4>Response</h4>
                    <#if restQuery.statusCode?has_content>
                        <p>Status code: ${restQuery.statusCode}</p>
                    </#if>
                    <#if restQuery.contentType?has_content>
                        <p>Content Type: ${restQuery.contentType}</p>
                    </#if>
                    <#if restQuery.requestHeaders?has_content>
                        <h4>Request Headers</h4>
                        <pre>${(formatter.renderHeaders(restQuery.requestHeaders))!}</pre>
                    </#if>
                    <#if restQuery.content?has_content>
                        <h4>Content Body</h4>
                        <pre>${(formatter.renderText(restQuery.content))!}</pre>
                    </#if>
                    <#if restQuery.requestCookies?has_content>
                        <h4>Request Cookies</h4>
                        <pre>${(formatter.renderText(restQuery.requestCookies))!}</pre>
                    </#if>
                    <#if restQuery.responseHeaders?has_content>
                        <h4>Response Headers</h4>
                        <pre>${(formatter.renderHeaders(restQuery.responseHeaders))!}</pre>
                    </#if>
                    <h4>Response Body</h4>
                    <#if restQuery.responseHeaders?has_content>
                        <pre>${formatter.renderText(restQuery.responseBody)}</pre>
                    </#if>
                    <#if restQuery.responseCookies?has_content && (!(restQuery.requestCookies?has_content) || restQuery.responseCookies!=restQuery.requestCookies)>
                        <h4>Response Cookies</h4>
                        <pre>${(formatter.renderText(restQuery.responseCookies))!}</pre>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</#macro>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
<#--    <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">-->
<#--    <script src="js/jquery.min.js"></script>-->
<#--    <script src="js/bootstrap.min.js"></script>-->
    <style>
        * {
            margin:0;
            padding:0;
        }

        .rest-query-details {
            width: 99%;
            margin-left: 0px !important;
            background-color: #EEEEE0;
            font-family: monospace;
        }

        pre {
            border: 1px solid;
            border-radius: 4px;

            display: block;
            padding: 9.5px;
            margin: 0 0 10px;
            font-size: 13px;
            color: #333;
            word-break: break-all;
            word-wrap: break-word;
            background-color: #f5f5f5;

            font-family: Menlo,Monaco,Consolas,"Courier New",monospace;
        }
    </style>

</head>

 <@restQueryData restQuery=query />

</html>