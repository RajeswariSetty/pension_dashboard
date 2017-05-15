<%@include file="/libs/foundation/global.jsp" %>
<cq:include path="clientcontext" resourceType="cq/personalization/components/clientcontext"/>
<cq:include path="header" resourceType="pensiondashboard/components/structure/header"/>
<body>
    <cq:include path="topnav" resourceType="pensiondashboard/components/structure/topnav"/>

<div class="container">
<cq:include script="content.jsp"/>
</div>
<cq:includeClientLib js="pension_dashboard.main"/>
<cq:include path="cloudservices" resourceType="cq/cloudserviceconfigs/components/servicecomponents"/>
</body>
<cq:include path="footer" resourceType="pensiondashboard/components/structure/footer"/>