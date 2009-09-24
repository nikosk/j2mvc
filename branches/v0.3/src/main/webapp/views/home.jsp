<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <title>${template.title}</title>
        <link rel="stylesheet" href="/static/css/reset-fonts-grids.css" type="text/css"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <link rel="stylesheet" href="/static/js/mootree.css" type="text/css" media="screen" charset="utf-8"/>
        <link rel="stylesheet" href="/static/css/forms.css" type="text/css" media="screen" charset="utf-8"/>
        <link rel="stylesheet" href="/static/css/typography.css" type="text/css" media="screen" charset="utf-8"/>
        <link rel="stylesheet" href="/static/css/styles.css" type="text/css" media="screen" charset="utf-8"/>
        <script type="text/javascript" src="/static/js/mootools-1.2.1-core.js"></script>
        <script type="text/javascript" src="/static/js/mootree.js"></script>
        <script type="text/javascript" src="/static/js/mootree_init.js"></script>
        <style>
            ul ul{padding-left:15px;}
        </style>
    </head>
    <body>
        <div id="doc3" class="yui-t2">
            <div id="hd"><h1>Header</h1></div>
            <div id="bd">
                <div id="yui-main">
                    <div class="yui-b">
                        <div class="yui-ge">
                            <div class="yui-u first">
                                <div id="message"></div>
                                <div>${template.content}</div>
                            </div>
                            <div class="yui-u">
                                <div class="sidebar">

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="yui-b">
                    <div class="sidebar">
                        ${template.sidebar}
                    </div>
                </div>
            </div>
            <div id="ft"><p>Footer</p></div>
        </div>
    </body>
</html>