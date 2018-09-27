<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />

<div class="error-box">
    <img src="images/login/warning.png" width="280" height="230">
  <h2>Authentication Succeeded with Warnings</h2>

<c:forEach items="${messages}" var="message">
  <p class="message">${message.text}</p>
</c:forEach>

    <a class="btn" href="#">返回登录</a>
</div>

<c:url value="login" var="url">
  <c:param name="execution" value="${flowExecutionKey}" />
  <c:param name="_eventId" value="proceed" />
</c:url>

<%--<div id="big-buttons">--%>
 <%--<a class="button" href="${url}">Continue</a>--%>
<%--</div>--%>

<jsp:directive.include file="includes/bottom.jsp" />
