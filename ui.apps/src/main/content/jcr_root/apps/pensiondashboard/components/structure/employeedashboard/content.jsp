Employee Dashboard
<br/>

<%@include file="/libs/foundation/global.jsp"%>

<cq:include path="par" resourceType="foundation/components/parsys"/>
Name:<%= resourceResolver.adaptTo(Session.class).getUserID() %>
<form id="loginModalForm1">
<input type="text" name="amount"/>
</form>

<cq:includeClientLib js="employeedashboard.main"/>