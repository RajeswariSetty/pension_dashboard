Employee Dashboard
<br/>

<%@include file="/libs/foundation/global.jsp"%>

<cq:include path="par" resourceType="foundation/components/parsys"/>
Name:<%= resourceResolver.adaptTo(Session.class).getUserID() %><br/>

Pensions found <label id="potCount"></label> <br/>
Pensions income <label id="pensionIncomeMonthly"></label> monthly at age <label id="currentAge"></label><br/>
Pensions income <label id="pensionIncomeAnnual"></label> monthly at age <label id="retirementAge"></label><br/>

My retirement plan<br/>
current plan<br/>
<br/>
Age  <label id="currentAge2"></label> Monthly salary  <label id="monthlySalCurrentAge"></label><br/>
Pension Age  <label id="retirementAge2"></label> Target Pension Pot  <label id="targetPensionPot"></label><br/>
Estimated monthly income including State Pension <label id="est_mnthly_income"></label> monthly<br/>

My Actions <br/>
<label id="actions"></label>

<cq:includeClientLib js="employeedashboard.main"/>
