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
<!DOCTYPE html>

<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="zh_CN">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <title>CAS &#8211; 高检院统一登录系统</title>
  
  <spring:theme code="standard.custom.css.file" var="customCssFile" />
  <link rel="stylesheet" href="<c:url value="${customCssFile}" />" />
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/login.css" />
  <link rel="icon" href="<c:url value="/src/main/webapp/favicon.ico" />" type="image/x-icon" />
    <script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/js/prototype.js"></script>
    <script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/js/pub.js"></script>
    <%--<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/js/jquery-1.8.0.min.js"></script>--%>
    <%--<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/js/jquery.jslides.js"></script>--%>

    <!--[if lt IE 9]>
    <script src="//cdnjs.cloudflare.com/ajax/libs/html5shiv/3.6.1/html5shiv.js" type="text/javascript"></script>
    <![endif]-->
</head>
<body id="cas">
<div class="header">
    <div class="wrap">
        <span class="logo"></span>
        <div class="nav">
        </div>
    </div>
</div>