<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="sun.misc.BASE64Encoder" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="org.jasig.cas.services.ume.CasServerCfgPub" %>
<%--

    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License.  You may obtain a
    copy of the License at the following location:

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

--%>
<jsp:directive.include file="includes/top.jsp" />
<%
  String msg = "";
  if(null!=request.getParameter("em") && !"".equals(request.getParameter("em").trim()))
  {
//    msg = new String(em.getBytes("iso-8859-1"), "utf-8");
    msg = URLDecoder.decode(request.getParameter("em"), "utf-8");
  }

  if("true".equals((String)request.getSession().getAttribute("umeNewFlag")))
  {
    request.getSession().removeAttribute("umeNewFlag");
    msg = "";
  }
  String ip = request.getRemoteAddr().toString();
  String bSpecialValue = new BASE64Encoder().encode(ip.getBytes("utf-8"));
  boolean bUserName = false;   //new CasLoginTypeTreat().getLoginType(ip);
//  bUserName = false;
  System.out.println("________bUserName: "+bUserName+"____service: "+request.getParameter("service")+"____renew: "+request.getParameter("renew")+"____msg: "+request.getParameter("umeMsg"));
%>
<script type="text/javascript" language="JavaScript">
    var g_sRootPath = "<%=request.getContextPath()%>";
    var bFirefox = Prototype.Browser.FireFox;
    if(bFirefox)
        document.write("<embed id='ff_plugin' type='application/mozilla-jn-ca-plugin' width='1' height = '1'>");
</script>
<script type="text/javascript" language="JavaScript">
    function doInit01() {
        // 调用打开硬件设备接口
        var ocx = null;
        if (bFirefox){
             ocx =  document.getElementById("ff_plugin");
        }else
        {
            ocx = document.getElementById("ocx") ;
        }

        var pw = document.getElementById("passwordCa");
        var checkDev = "";
        try
        {
            checkDev = ocx.OpenDevice("");
        }
        catch(e)
        {
            alert("请安装证书驱动！");
            checkDev = "";
            return false;
        }

        if(checkDev!=0 && checkDev.indexOf("0")!=0)
        {
            ocx.CloseDevice();
            alert("打开证书失败，请与系统管理员联系！");
            return false;
        }

        // 验证硬件保护口令（56所初始化默认为1qaz2wsx3edc）
        // 如果密码口令修改，不能用默认密码验证，弹出输入新口令的页面CALogin.jsp 提示录入新的口令
        <%--var url = "<%=request.getContextPath()%>/CALogin.jsp";--%>
//        var key = window.showModalDialog(url, '系统提示信息', "toolbar=no;location=no;directories=no;status=no;menubar=no;resizable=0;scrollbars=no;dialogWidth=450px;dialogHeight=200px;");

        var checkPin = ocx.VerifyPin(pw.value);
        try{
            checkPin = ocx.VerifyPin(pw.value);
        }catch(e){
            alert("证书密码验证失败！");
            ocx.CloseDevice();
            return false;
        }

//        alert(" checkPin : "+checkPin);
        if (checkPin != 0 && checkPin.indexOf("0")!=0) {
            alert("证书密码验证失败！");
            ocx.CloseDevice();
            return false;
        }
        // 生成客户端握手请求串，并发送到应用服务器端开始首次通信，服务器端为后台类CALoginAction
        var checkHello = ocx.ClientHello('0');
//        alert(" checkHello : "+checkHello);
        if(checkHello.indexOf("#") == 0)
        {
            alert("证书验证失败！");
            ocx.CloseDevice();
            return false;
        }
        <%--var url = "<%=request.getContextPath()%>/casca/loginServHello";--%>
        <%--var res = doPost(url, Str_ClientHello);--%>
        <%--if (res == "err" || res == "") {--%>
            <%--alert("证书验证失败");--%>
            <%--ocx.CloseDevice();--%>
            <%--return false;--%>
        <%--}--%>
        // 客户端获取应用服务器端应答口令后，在客户端获取证书信息提交给应用服务器CALoginAction
        var Str_ClientAuth=ocx.ClientAuth(checkHello,"0");
//        alert(" Str_ClientAuth : "+Str_ClientAuth);
//        var data="<RLT><ClientAuth>"+Str_ClientAuth+"</ClientAuth><ServerHello>"+res+"</ServerHello></RLT>";
//        alert(" data : "+data);

        var un = document.getElementById("usernameCa");
        un.value = "meiyoushijiyiyideshuju";
        document.getElementById("strCA").value = Str_ClientAuth;
        doLogin();
        <%--url = "<%=request.getContextPath()%>/casca/loginServAuth";--%>
        <%--var res1 = doPost(url, data);--%>
        <%--if (res1=="err"){--%>
            <%--alert("证书验证失败!");--%>
            <%--return false;--%>
        <%--}--%>
        <%--else--%>
            <%--return res1;--%>
    }
    function changeLogin(flag)
    {
        var divID = document.getElementById("id-login");
        var divCA = document.getElementById("ca-login");
        var divHideID = document.getElementById("id-login-hide");
        var divHideCA = document.getElementById("ca-login-hide");
        var divLoginBox = document.getElementById("login-box");
        if("1"==flag)
        {
            loginType = "1";
            divHideID.innerHTML = divID.outerHTML;
            divCA.style.display = "";
            divLoginBox.innerHTML = divCA.outerHTML;
            divHideCA.innerHTML = "";
            var objPasswordCa = document.getElementById("passwordCa");
            objPasswordCa.focus();
        }
        else if("2"==flag)
        {
            loginType = "2";
            divHideCA.innerHTML = divCA.outerHTML;
            divID.style.display = "";
            divLoginBox.innerHTML = divID.outerHTML;
            divHideID.innerHTML = "";
            var objUsername = document.getElementById("usernameId");
            objUsername.focus();
        }
    }
    function doHideIdLoginDiv()
    {
        changeLogin("1");

        var objForm = document.getElementById("fm1");
        objForm.onsubmit = function(){return false;};

    }
    function doLonginByKeydown(event)
    {
//        if(event.keyCode==49)
//            return;
//
//        alert("event.keyCode : "+event.keyCode);
        if(event.keyCode===13)
        {
            if(loginType=="1")
            {
                doInit01();
            }
            else
            {
                doLogin();
            }

        }
    }
    function doLogin()
    {
        var objForm = document.getElementById("fm1");
        objForm.submit();
    }
    function gotoPassword(event)
    {
        if(event.keyCode==13)
        {
            var objPassword = document.getElementById("password");
            objPassword.focus();
        }
    }
    function forget(){
        window.open("<%=CasServerCfgPub.getPortalPage()%>/jsp/modules/wjmm/forget.jsp" , "忘记密码" , "width=1300,height=800");
    }
</script>
<div style="display:none;">
  <div id="id-login-hide"></div>
  <div id="ca-login-hide"></div>
</div>
  <form:form method="post" id="fm1" commandName="${commandName}" htmlEscape="true">
      <div id="login-box" style="background-color:#fff;">
        <div id="id-login" class="content user-login active" style="display:none;">
          <div class="input-i clearfix user">
            <c:choose>
              <c:when test="${not empty sessionScope.openIdLocalId}">
                <input type="text" id="username" name="username" onkeyup="gotoPassword(event);" value="<c:out value="${sessionScope.openIdLocalId}" />" class="pull-left" placeholder="用户名"/>
              </c:when>
              <c:otherwise>
                <input type="text" id="usernameId" name="username" class="pull-left" onkeyup="gotoPassword(event);" placeholder="用户名"/>
              </c:otherwise>
            </c:choose>
            <span class="pull-right user-icon"></span>
          </div>
          <div class="input-i clearfix psw">
            <input type="password" tabindex="" id="password" name="password" placeholder="密码" class="pull-left" onkeydown="doLonginByKeydown(event);">
            <span class="pull-right psw-icon"></span>
          </div>
          <input id="normalSubmitBtn" type="button" class="login-btn" onclick="doLogin();" value="登录" />
          <a href="javascript:void(0);" class="forget1-type" id="forget" onclick="forget()"></a>
          <a class="forget2-type" href="javascript:changeLogin('1');">CA证书登录 &gt;&gt;</a>
        </div>
        <div id="ca-login" class="content user-login active">
          <div class="input-i clearfix user" style="display:none;">
            <c:choose>
              <c:when test="${not empty sessionScope.openIdLocalId}">
                <input type="hidden" id="usernameCa" name="username" value="<c:out value="${sessionScope.openIdLocalId}" />" class="pull-left" placeholder="用户名"/>
              </c:when>
              <c:otherwise>
                <input type="hidden" id="usernameCa" name="username" class="pull-left" placeholder="用户名"/>
              </c:otherwise>
            </c:choose>
          </div>
          <div class="input-i clearfix psw">
            <input type="password" name="password" id="passwordCa" placeholder="密码" class="pull-left" onkeydown="doLonginByKeydown(event);">
            <span class="pull-right psw-icon"></span>
          </div>
          <input id="caSubmitBtn" type="button" class="login-btn" value="CA证书登录" onclick="doInit01();" style="border: none;margin-bottom:25px;" />
          <a class="user-other-type" href="javascript:changeLogin('2');">帐号密码登录 &gt;&gt;</a>
        </div>
      </div>
    <section class="row btn-row" style="display:none;">
      <input type="hidden" name="lt" value="${loginTicket}" />
      <input type="hidden" name="execution" value="${flowExecutionKey}" />
      <input type="hidden" name="_eventId" value="submit" />
      <input type="hidden" name="bSpecial" value="<%=bSpecialValue%>" />
      <input type="hidden" id="strCA" name="strCA" value="" />
    </section>
    <%
      if(null!=msg && !"".equals(msg)){
    %>
    <div class="prompt-text">
        <%=msg%>
    </div>
    <%
    }
    else{
    %>
    <div class="prompt-text"><form:errors path="*" id="msg" cssClass="prompt-text" element="div" htmlEscape="false" /></div>
    <%
      }
    %>
  </form:form>
<div style="display:none;">
  <OBJECT style="VISIBILITY: hidden; TOP: 0px; LEFT: 0px" id="ocx" name="ocx"
          codeBase="<%=request.getContextPath()%>/cab/CryptOCX.ocx" classid=clsid:18ED5D4D-446E-436A-A839-F21382DD1F77
          width=0 height=0></OBJECT>
<div id="sidebar">
  <div class="sidebar-content">
    <p><spring:message code="screen.welcome.security" /></p>

    <div id="list-languages">
      <%final String queryString = request.getQueryString() == null ? "" : request.getQueryString().replaceAll("&locale=([A-Za-z][A-Za-z]_)?[A-Za-z][A-Za-z]|^locale=([A-Za-z][A-Za-z]_)?[A-Za-z][A-Za-z]", "");%>
      <c:set var='query' value='<%=queryString%>' />
      <c:set var="xquery" value="${fn:escapeXml(query)}" />

      <h3>Languages:</h3>
      <c:choose>
        <c:when test="${not empty requestScope['isMobile'] and not empty mobileCss}">
          <form method="get" action="login?${xquery}">
            <select name="locale">
              <option value="en">English</option>
              <option value="es">Spanish</option>
              <option value="fr">French</option>
              <option value="ru">Russian</option>
              <option value="nl">Nederlands</option>
              <option value="sv">Svenska</option>
              <option value="it">Italiano</option>
              <option value="ur">Urdu</option>
              <option value="zh_CN">Chinese (Simplified)</option>
              <option value="zh_TW">Chinese (Traditional)</option>
              <option value="de">Deutsch</option>
              <option value="ja">Japanese</option>
              <option value="hr">Croatian</option>
              <option value="cs">Czech</option>
              <option value="sl">Slovenian</option>
              <option value="pl">Polish</option>
              <option value="ca">Catalan</option>
              <option value="mk">Macedonian</option>
              <option value="fa">Farsi</option>
              <option value="ar">Arabic</option>
              <option value="pt_PT">Portuguese</option>
              <option value="pt_BR">Portuguese (Brazil)</option>
            </select>
          </form>
        </c:when>
        <c:otherwise>
          <c:set var="loginUrl" value="login?${xquery}${not empty xquery ? '&' : ''}locale=" />
          <ul>
            <li class="first"><a href="${loginUrl}en">English</a></li>
            <li><a href="${loginUrl}es">Spanish</a></li>
            <li><a href="${loginUrl}fr">French</a></li>
            <li><a href="${loginUrl}ru">Russian</a></li>
            <li><a href="${loginUrl}nl">Nederlands</a></li>
            <li><a href="${loginUrl}sv">Svenska</a></li>
            <li><a href="${loginUrl}it">Italiano</a></li>
            <li><a href="${loginUrl}ur">Urdu</a></li>
            <li><a href="${loginUrl}zh_CN">Chinese (Simplified)</a></li>
            <li><a href="${loginUrl}zh_TW">Chinese (Traditional)</a></li>
            <li><a href="${loginUrl}de">Deutsch</a></li>
            <li><a href="${loginUrl}ja">Japanese</a></li>
            <li><a href="${loginUrl}hr">Croatian</a></li>
            <li><a href="${loginUrl}cs">Czech</a></li>
            <li><a href="${loginUrl}sl">Slovenian</a></li>
            <li><a href="${loginUrl}ca">Catalan</a></li>
            <li><a href="${loginUrl}mk">Macedonian</a></li>
            <li><a href="${loginUrl}fa">Farsi</a></li>
            <li><a href="${loginUrl}ar">Arabic</a></li>
            <li><a href="${loginUrl}pt_PT">Portuguese</a></li>
            <li><a href="${loginUrl}pt_BR">Portuguese (Brazil)</a></li>
            <li class="last"><a href="${loginUrl}pl">Polish</a></li>
          </ul>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
  </div>
  <script type="text/javascript" language="JavaScript">
    doHideIdLoginDiv();
  </script>
</div>

<jsp:directive.include file="includes/bottom.jsp" />
