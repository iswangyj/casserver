<%@ page import="java.net.URLEncoder" %><%--

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
<%@ page contentType="text/html; charset=UTF-8" %>
<%
  StringBuilder sb = new StringBuilder("");
  String service = request.getParameter("service");
  if(null!=service && !"".equals(service))
  {
      sb.append("service="+ URLEncoder.encode(service, "utf-8"));
  }

  String msg = request.getParameter("em");
  if(null!=msg && !"".equals(msg))
  {
      if(sb.length()>0)
          sb.append("&");
    sb.append("em="+ URLEncoder.encode(msg, "utf-8"));
  }
//  String renew = request.getParameter("renew");
//  if(null!=renew && !"".equals(renew))
//  {
//    if(sb.length()>0)
//      sb.append("&");
//    sb.append("renew="+ URLEncoder.encode(renew, "utf-8"));
//  }
  System.out.println("______url____"+sb.toString());
  session.invalidate();
  response.sendRedirect(request.getContextPath()+"/login?"+sb.toString());
%>
<jsp:directive.include file="includes/top.jsp" />
<script type="text/javascript" language="JavaScript">
  window.location = "<%=service%>";
</script>
<div class="error-box">
  <img src="images/login/success.png" width="280" height="230">
    <h2><spring:message code="screen.logout.header" /></h2>
    <p><spring:message code="screen.logout.success" /></p>
    <p><spring:message code="screen.logout.security" /></p>
  <a class="btn" href="#">返回登录</a>
  </div>
<jsp:directive.include file="includes/bottom.jsp" />