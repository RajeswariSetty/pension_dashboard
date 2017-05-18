$(document).ready(function(){
   $.ajax({
         type: 'POST',    
         url:'/bin/employeedashboardServlet',
         success: function(msg){
           //alert(msg); //display the data returned by the servlet
              var json = jQuery.parseJSON(msg);
              $('#potCount').val(json.potCount); 
              $('#pensionIncomeMonthly').text(json.pensionIncomeMonthly);
              $('#currentAge').text(json.currentAge);
              $('#currentAge2').text(json.currentAge);
              $('#retirementAge').text(json.retirementAge);
              $('#retirementAge2').text(json.retirementAge);
              $('#pensionIncomeAnnual').text(json.pensionIncomeAnnual);
              $('#monthlySalCurrentAge').text(json.monthlySalCurrentAge);
              $('#targetPensionPot').text(json.targetPensionPot);
              $('#est_mnthly_income').text(json.est_mnthly_income);

             var actions = "";

             for(var i=0;i<json.actions.length;i++){
				actions = actions+","+JSON.stringify(json.actions);
             }
             $('#actions').text(actions);
         }
     });

});