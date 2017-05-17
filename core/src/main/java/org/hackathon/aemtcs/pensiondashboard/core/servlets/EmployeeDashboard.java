package org.hackathon.aemtcs.pensiondashboard.core.servlets;

import java.io.IOException;
import java.rmi.ServerException;
//import MySQL APIs
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.jcr.Session;
import javax.sql.DataSource;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//Import CQ DataSOurcePool
import com.day.commons.datasource.poolservice.DataSourcePool;

@SlingServlet(paths = "/bin/employeedashboardServlet", methods = "POST", metatype = true)
public class EmployeeDashboard extends
		org.apache.sling.api.servlets.SlingAllMethodsServlet {
	private static final long serialVersionUID = 2598426539166789515L;
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDashboard.class);
	  

	@Reference
	private DataSourcePool source;

	@Override
	protected void doPost(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServerException,
			IOException {

		try {
			// Get the submitted form data that is sent from the
			// CQ web page
			Session session = request.getResourceResolver().adaptTo(Session.class);
			String userId = session.getUserID();

			// Persist the Data into MySQL by using connection build with the
			// DataSourcePool
			String jsonData = getDashboardData(userId);

			// Return the JSON formatted data
			response.getWriter().write(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Returns a connection using the configured DataSourcePool
	private Connection getConnection() {
		DataSource dataSource = null;
		Connection con = null;
		try {
			// Inject the DataSourcePool right here!
			dataSource = (DataSource) source.getDataSource("Customer");
			con = dataSource.getConnection();
			return con;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Adds a new customer record in the Customer table
	public String getDashboardData(String userId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String dashboardData = "";
		JSONObject obj = new JSONObject();;
		try {


		      //STEP 3: Open a connection
		      LOGGER.info("Pension Dashboard Connecting to a selected database...");
		      conn = getConnection();
		      LOGGER.info("Connected database successfully...");
		      
		      //STEP 4: Execute a query
		      LOGGER.info("Creating statement...");
		      String sql = "select scheme_id,"+
		    		  "sp_policy_number,"+
		    		  "sp_pension_fund,"+
		    		  "sp_monthly_contribution,"+
		    		  "fc_Policy_number,"+
		    		  "fc_pension_fund,"+
		    		  "fc_monthly_contribution,"+
		    		  "fb_Policy_number,"+
		    		  "fb_pension_fund,"+
		    		  "fb_monthly_contribution,"+
		    		  "created_by,"+
		    		  "created_date,"+
		    		  "first_name,"+
		    		  "last_name,"+
		    		  "dob,"+
		    		  "ni_number,"+
		    		  "annual_income from tbl_member_details where member_id="+userId;
		      pstmt = conn.prepareStatement(sql);
		      //pstmt.setString(1, userId);
		      ResultSet rs = pstmt.executeQuery(sql);
		      //STEP 5: Extract data from result set
		      while(rs.next()){
		         //Retrieve by column name
		         int   schemeId = rs.getInt("scheme_id");
		         String   spPolicyNumber = rs.getString("sp_policy_number");
		         double   spPensionFund = rs.getDouble("sp_pension_fund");
		         double   spMonthlyContr = rs.getDouble("sp_monthly_contribution");
		         String   fcPolicyNumber = rs.getString("fc_Policy_number");
		         double   fcPensionFund = rs.getDouble("fc_pension_fund");
		         double   fcMonthlyContr = rs.getDouble("fc_monthly_contribution");
		         String   fbPolicyNumber = rs.getString("fb_Policy_number");
		         double   fbPensionFund = rs.getDouble("fb_pension_fund");
		         double   fbMonthlyContr = rs.getDouble("fb_monthly_contribution");
		         String   firstName = rs.getString("first_name");
		         String   lastName = rs.getString("last_name");
		         Date   dob = rs.getDate("dob");
		         String   NI = rs.getString("ni_number");
		         double   annualIncome = rs.getDouble("annual_income");

		         
		         obj.put("schemeId",schemeId);
		         obj.put("spPolicyNumber",spPolicyNumber);
		         obj.put("spPensionFund",spPensionFund);
		         obj.put("spMonthlyContr",spMonthlyContr);
		         obj.put("fcPolicyNumber",fcPolicyNumber);
		         obj.put("fcPensionFund",fcPensionFund);
		         obj.put("fcMonthlyContr",fcMonthlyContr);
		         obj.put("fbPolicyNumber",fbPolicyNumber);
		         obj.put("fbPensionFund",fbPensionFund);
		         obj.put("fbMonthlyContr",fbMonthlyContr);
		         obj.put("firstName",firstName);
		         obj.put("lastName",lastName);
		         obj.put("dob",dob);
		         obj.put("NI",NI);
		         obj.put("annualIncome",annualIncome);
		      }
		      rs.close();
		      dashboardData = obj.toString();
		      }catch(SQLException se){
		          //Handle errors for JDBC
		          se.printStackTrace();
		       }catch(Exception e){
		          //Handle errors for Class.forName
		          e.printStackTrace();
		       }finally{
		          //finally block used to close resources
		          try{
		             if(pstmt!=null)
		            	 pstmt.close();
		          }catch(SQLException se){
		          }// do nothing
		          try{
		             if(conn!=null)
		                conn.close();
		          }catch(SQLException se){
		             se.printStackTrace();
		          }//end finally try
		       }//end try
		return dashboardData;
	}

}