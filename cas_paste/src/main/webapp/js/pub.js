/**
 * Created by slirn on 2017/4/8.
 */

/*********通用提交函数*********/
doPost = function(sActionURL,postData,method)
{
    if(!postData || typeof(postData)=='undefined')
        postData = "";
    if(sActionURL.substring(0,1)!="/" && sActionURL.substring(0,4).toLowerCase()!='http')
        sActionURL = "/" + sActionURL ;
    if(sActionURL.indexOf(g_sRootPath)<0)
        sActionURL = g_sRootPath + sActionURL;
    if(method)
    {
        if(sActionURL.indexOf("?")<0)
            sActionURL += "?method="+method ;
        else
            sActionURL += "&method="+method ;
    }
    try
    {
            var syncResult = null;
        new Ajax.Request(sActionURL,{
            method: 'post',
            postBody:postData,
            asynchronous:false,
            contentType:'text/xml',
            onSuccess: function(transport) {
                syncResult = transport.responseText;
            },
            onError: function(transport) {
                syncResult = transport.responseText;
            },
            onComplete: function(transport)
            {
                syncResult = transport.responseText;
                if (200 != transport.status)
                    alert(syncResult);
            }
        });
        return syncResult;
    }
    catch(e)
    {
        alert('#2289: '+e.message);
        return null;
    }
};
