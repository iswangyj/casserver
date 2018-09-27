<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.ume.CasLoginTypeTreat" %>
<%@ page import="sun.misc.BASE64Encoder" %>
<%@ page import="java.net.URLDecoder" %>
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
  if(null!=request.getParameter("em"))
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
  boolean bUserName = new CasLoginTypeTreat().getLoginType(ip);
//  bUserName = false;
  System.out.println("________bUserName: "+bUserName+"____service: "+request.getParameter("service")+"____renew: "+request.getParameter("renew")+"____msg: "+request.getParameter("umeMsg"));
%>
<script type="text/javascript" language="JavaScript">
    var g_sRootPath = "<%=request.getContextPath()%>";
</script>
<script type="text/javascript" language="JavaScript">
    function doInit() {
        // 调用打开硬件设备接口
        var ocx = document.getElementById("ocx");
        var checkDev = ocx.OpenDevice("");
        if (checkDev != 0) {
            alert("打开证书失败，请与系统管理员联系！");
            ocx.CloseDevice();
            return false;
        }
        // 验证硬件保护口令（56所初始化默认为1qaz2wsx3edc）
        // 如果密码口令修改，不能用默认密码验证，弹出输入新口令的页面CALogin.jsp 提示录入新的口令
        <%--var url = "<%=request.getContextPath()%>/CALogin.jsp";--%>
//        var key = window.showModalDialog(url, '系统提示信息', "toolbar=no;location=no;directories=no;status=no;menubar=no;resizable=0;scrollbars=no;dialogWidth=450px;dialogHeight=200px;");

        var pw = document.getElementById("password");
        var check1 = ocx.VerifyPin(pw.value);
        if (check1 != 0) {
            alert("证书密码验证失败");
            ocx.CloseDevice();
            return false;
        }
        // 生成客户端握手请求串，并发送到应用服务器端开始首次通信，服务器端为后台类CALoginAction
        var Str_ClientHello = ocx.ClientHello('0');
        var url = "<%=request.getContextPath()%>/casca/loginServHello";
        var res = doPost(url, Str_ClientHello);
        if (res == "err" || res == "") {
            alert("证书验证失败");
            ocx.CloseDevice();
            return false;
        }
        // 客户端获取应用服务器端应答口令后，在客户端获取证书信息提交给应用服务器CALoginAction
        var Str_ClientAuth=ocx.ClientAuth(res,"0");
        var data="<RLT><ClientAuth>"+Str_ClientAuth+"</ClientAuth><ServerHello>"+res+"</ServerHello></RLT>";
        return data;
        <%--url = "<%=request.getContextPath()%>/casca/loginServAuth";--%>
        <%--var res1 = doPost(url, data);--%>
        <%--if (res1=="err"){--%>
            <%--alert("证书验证失败!");--%>
            <%--return false;--%>
        <%--}--%>
        <%--else--%>
            <%--return res1;--%>
    }
    function auth()
    {
        var frm1 = document.getElementById("fm1");
        var res = doInit();
//        var res = "<RLT><ClientAuth>123</ClientAuth><ServerHello>345</ServerHello></RLT>";
        if(res)
        {
            var un = document.getElementById("username");
            un.value = "meiyoushijiyiyideshuju";
            document.getElementById("strCA").value = res;
            frm1.submit();
        }
    }
</script>
<c:if test="${not pageContext.request.secure}">
  <div id="msg" class="errors" style="display:none;">
    <h2>Non-secure Connection</h2>
    <p>You are currently accessing CAS over a non-secure connection.  Single Sign On WILL NOT WORK.  In order to have single sign on work, you MUST log in over HTTPS.</p>
  </div>
</c:if>

<div>
  <form:form method="post" id="fm1" commandName="${commandName}" htmlEscape="true">
    <%
      if(null!=msg && !"".equals(msg)){
    %>
    <div class="wrap">
      <div class="prompt-bar">
        <%=msg%>
      </div>
    </div>
    <%
        }
        else{
    %>
    <div class="wrap">
      <form:errors path="*" id="msg" cssClass="prompt-bar" element="div" htmlEscape="false" />
    </div>
    <%
        }
    %>

    <%--<h2><spring:message code="screen.welcome.instructions" /></h2>--%>
    <!-- 代码 开始 -->
    <div id="full-screen-slider">
      <ul id="slides">
        <%--<li style="background:url('<%=request.getContextPath()%>/images/login/banner1.jpg') no-repeat center top"><a href="#"></a></li>--%>
        <li style="background:url('<%=request.getContextPath()%>/images/login/banner2.jpg') no-repeat center top"><a href="#"></a></li>
      </ul>
    </div>
    <!-- 代码 结束 -->
    <div class="wrap">
      <div class="login-box">
        <%
          if(bUserName)
          {
        %>
        <div id="id-login" class="form-box">
          <div class="input-bg">
            <label>账号：</label>
            <i class="user"></i>
            <section class="row">
              <c:choose>
                <c:when test="${not empty sessionScope.openIdLocalId}">
                  <strong><c:out value="${sessionScope.openIdLocalId}" /></strong>
                  <input type="hidden" id="username" name="username" value="<c:out value="${sessionScope.openIdLocalId}" />" />
                </c:when>
                <c:otherwise>
                  <spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" />
                  <form:input cssClass="required" cssErrorClass="errors" id="username" size="25" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="off" htmlEscape="true" />
                </c:otherwise>
              </c:choose>
            </section>
          </div>
          <div class="input-bg" style="margin-top:40px;">
            <label>密码：</label>
            <i class="lock"></i>
            <section class="row">
                <%--
                NOTE: Certain browsers will offer the option of caching passwords for a user.  There is a non-standard attribute,
                "autocomplete" that when set to "off" will tell certain browsers not to prompt to cache credentials.  For more
                information, see the following web page:
                http://www.technofundo.com/tech/web/ie_autocomplete.html
                --%>
              <spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" />
              <form:password cssClass="required" cssErrorClass="error" id="password" size="25" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" />
            </section>
          </div>
          <input class="btn" type="submit" onclick="login();" accesskey="l" value="<spring:message code="screen.welcome.button.login" />" tabindex="4" style="border: none;" />
          <a class="change" href="#" style="display:none;">CA证书登录 &gt;&gt;</a>
        </div>
        <%
          }
          else
          {
        %>
        <div id="ca-login" class="form-box">

          <div class="input-bg" style="display:none;">
            <label>账号：</label>
            <i class="user"></i>
            <section class="row">
              <c:choose>
                <c:when test="${not empty sessionScope.openIdLocalId}">
                  <strong><c:out value="${sessionScope.openIdLocalId}" /></strong>
                  <input type="hidden" id="username" name="username" value="<c:out value="${sessionScope.openIdLocalId}" />" />
                </c:when>
                <c:otherwise>
                  <spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" />
                  <form:input cssClass="required" cssErrorClass="errors" id="username" size="25" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="off" htmlEscape="true" />
                </c:otherwise>
              </c:choose>
            </section>

          </div>
          <div class="input-bg" style="margin:20px 0 50px;">
            <label>密码：</label>
            <i class="lock"></i>
            <section class="row">
                <%--
                NOTE: Certain browsers will offer the option of caching passwords for a user.  There is a non-standard attribute,
                "autocomplete" that when set to "off" will tell certain browsers not to prompt to cache credentials.  For more
                information, see the following web page:
                http://www.technofundo.com/tech/web/ie_autocomplete.html
                --%>
              <spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" />
              <form:password cssClass="required" cssErrorClass="error" id="password" size="25" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" />
            </section>
          </div>
          <input class="btn" accesskey="l" value="CA证书登录" tabindex="4" type="button" onclick="auth();" style="border: none;" />
          <a class="change" href="#" style="display:none;">帐号密码登录 &gt;&gt;</a>
        </div>
        <%
          }
        %>
      </div>

      <script>
//          $("#id-login .change,#ca-login .change").on("click", function(){
//              $("#ca-login").toggle();
//              $("#id-login").toggle();
//          });
      </script>
    </div>
    <section class="row check" style="display:none;">
      <input id="warn" name="warn" value="true" tabindex="3" accesskey="<spring:message code="screen.welcome.label.warn.accesskey" />" type="checkbox" />
      <label for="warn"><spring:message code="screen.welcome.label.warn" /></label>
    </section>

    <section class="row btn-row" style="display:none;">
      <input type="hidden" name="lt" value="${loginTicket}" />
      <input type="hidden" name="execution" value="${flowExecutionKey}" />
      <input type="hidden" name="_eventId" value="submit" />
      <%
        if(bUserName){
      %>
      <input type="hidden" name="bSpecial" value="<%=bSpecialValue%>" />
      <%
        }
        else
        {
      %>
      <input type="hidden" id="strCA" name="strCA" value="" />
      <%
        }
      %>
    </section>
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
            <input type="submit" value="Switch">
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
</div>

<jsp:directive.include file="includes/bottom.jsp" />
