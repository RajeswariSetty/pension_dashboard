<%@page import="com.day.cq.wcm.api.PageFilter,
                java.util.Iterator"%>
<%@include file="/libs/foundation/global.jsp"%>
<%@taglib prefix="personalization" uri="http://www.day.com/taglibs/cq/personalization/1.0" %>
<%
// obtain the site's home page, assume that this is the first level page under /content
Page homePage = currentPage.getAbsoluteParent(1);
pageContext.setAttribute("homePage", homePage);
%>
  <div style="display: none;" id="loginModalRequired" class="alert alert-danger" role="alert">Both username and password are required.</div>
                <div style="display: none;" id="loginModalInvalid" class="alert alert-danger" role="alert">Username and password do not match</div>
                <div style="display: none;" id="loginModalUnknown" class="alert alert-danger" role="alert">Unknown error</div>

 <form id="loginModalForm"  method="post" action="${homePage.path}/j_security_check">
     <div class="form-group">
         <label for="j_username">Username</label> <input type="text" name="j_username">
     </div>
     <br/>
     <div class="form-group">
         <label for="j_password">Password</label> <input type="password" name="j_password">
     </div>
     <br/>
     <input type="hidden" name="resource" value="/content/PensionDashboard/EmployeeDashboard.html"/>
     <button type="submit">Login</button>
     
</form>

