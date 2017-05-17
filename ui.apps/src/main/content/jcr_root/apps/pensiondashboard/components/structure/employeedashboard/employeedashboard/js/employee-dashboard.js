$(document).ready(function(){
//alert('Employee Dashboard');
     $("#loginModalForm1  [name='amount']").val(1212);


    $.ajax({
         type: 'POST',    
         url:'/bin/employeedashboardServlet',
         success: function(msg){
           alert(msg); //display the data returned by the servlet
         }
     });

});