package org.hackathon.aemtcs.pensiondashboard.core.servlets;

import java.io.IOException;
import java.rmi.ServerException;
//import MySQL APIs
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Session;
import javax.sql.DataSource;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;






//Import CQ DataSOurcePool
import com.day.commons.datasource.poolservice.DataSourcePool;

@SlingServlet(paths = "/bin/employeedashboardServlet", methods = "POST", metatype = false)
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
            LOGGER.info("jsonData  ----->  "+jsonData);
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
			LOGGER.info("data source-->"+source);
			Collection<String> names = source.getAllJndiDataSourceNames();
			LOGGER.info("data source-->"+names.toString()+"  size "+names.size());
			Iterator<String> it = names.iterator();
			while(it.hasNext()){
				LOGGER.info("Datasource name:"+it.next());
			}
			dataSource = (DataSource) source.getDataSource("pensionsdsaws");
			con = dataSource.getConnection();
			LOGGER.info("conn->" + con);
			return con;

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("connection",e);
		}
		return null;
	}

	// Adds a new customer record in the Customer table
	public String getDashboardData(String userId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtActions = null;
		PreparedStatement pstmtTimeline = null;
		String dashboardData = "";
		JSONObject obj = new JSONObject();;
		try {


		      //STEP 3: Open a connection
		      LOGGER.info("Pension Dashboard Connecting to a selected database...");
		      conn = getConnection();
		      LOGGER.info("Connected database successfully...");
		      
		      //STEP 4: Execute a query
		      LOGGER.info("Creating statement...");
		      String sql = " select "+
		    		  "  pot_count,"+
		    		  "  pension_income_monthly,"+
		    		  "  pension_income_annual,"+
		    		  "  current_age,"+
		    		  "  retirement_age,"+
		    		  "  monthly_sal_current_age,"+
		    		  "  target_pension_pot,"+
		    		  "  est_mnthly_income"+
		    		  "  from tbl_emp_dashboard_summary t where user_name='"+userId+"'";
		      LOGGER.info("sql query is"+sql);
		      pstmt = conn.prepareStatement(sql);
		      //pstmt.setString(1, userId);
		      ResultSet rs = pstmt.executeQuery(sql);
		      //STEP 5: Extract data from result set
		      while(rs.next()){
		         //Retrieve by column name
		         int   potCount = rs.getInt("pot_count");
		         int   pensionIncomeMonthly = rs.getInt("pension_income_monthly");
		         int   pensionIncomeAnnual = rs.getInt("pension_income_annual");
		         int   currentAge = rs.getInt("current_age");
		         int   retirementAge = rs.getInt("retirement_age");
		         int   monthlySalCurrentAge = rs.getInt("monthly_sal_current_age");
		         int   targetPensionPot = rs.getInt("target_pension_pot");
		         int   est_mnthly_income = rs.getInt("est_mnthly_income");
		         
		         obj.put("potCount",potCount);
		         obj.put("pensionIncomeMonthly",pensionIncomeMonthly);
		         obj.put("pensionIncomeAnnual",pensionIncomeAnnual);
		         obj.put("currentAge",currentAge);
		         obj.put("retirementAge",retirementAge);
		         obj.put("monthlySalCurrentAge",monthlySalCurrentAge);
		         obj.put("targetPensionPot",targetPensionPot);
		         obj.put("est_mnthly_income",est_mnthly_income);
		      }
		      rs.close();
		      
		      String myactionsSql = "SELECT request_date,description,status FROM pensions.tbl_member_request t "+
		    		  				"where t.member_id=(select t1.member_id from tbl_member_details t1 where t1.user_name='"+userId+"')";
		      pstmtActions = conn.prepareStatement(myactionsSql);
		      ResultSet actionsrs = pstmtActions.executeQuery();
		      //STEP 5: Extract data from result set
		      JSONArray actions = new JSONArray();
		       
		      while(actionsrs.next()){
		         //Retrieve by column name
		    	 JSONObject action = new JSONObject(); 
		         String   potCount = actionsrs.getDate("request_date")+"";
		         String   description = actionsrs.getString("description");
		         String   status = actionsrs.getString("status");
		         
		         action.put("potCount", potCount);
		         action.put("description", description);
		         action.put("status", status);
		         actions.put(action);
		      }
		      actionsrs.close();
		      
		      obj.put("actions", actions);
		      
		      dashboardData = obj.toString();
		      }catch(SQLException se){
		          //Handle errors for JDBC
		          se.printStackTrace();
		          LOGGER.error("SQL fetch", se);
		       }catch(Exception e){
		          //Handle errors for Class.forName
		          e.printStackTrace();
		          LOGGER.error("SQL fetch", e);
		       }finally{
		          //finally block used to close resources
		          try{
		             if(pstmt!=null)
		            	 pstmt.close();
		             
		             if(pstmtActions!=null)
		            	 pstmtActions.close();
		          }catch(SQLException se){
		          }// do nothing
		          try{
		             if(conn!=null)
		                conn.close();
		          }catch(SQLException se){
		             se.printStackTrace();
		             LOGGER.error("SQL fetch", se);
		          }//end finally try
		       }//end try
		return dashboardData;
	}

}