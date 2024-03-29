package Spark;

import static spark.Spark.get;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

/**
 * VelocityTemplateRoute example.
 */
public final class Controller {
    
    public static void main(final String[] args) {
    	
    	staticFileLocation("/public"); 
    	
        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            System.out.println("funkar detta?");
            return new ModelAndView(model, "templates/index.html");
        }, new VelocityTemplateEngine());
        
        
        
		get("/game/:id", (request, response) -> {
			Map<String, Object> model = new HashMap<>();
			int id = Integer.parseInt(request.params("id"));
			model.put("id", request.params("id"));
			int i = 0;
			String str1 = "";
			String str2 = "";
			String str3 = "";
			String str4 = "";
			String str5 = "";
			String str6 = "";
			String str7 = "";
			String str8 = "";
			String str9 = "";
			String str10 = "";
			String str11 = "";
			String str12 = "";
			String str13 = "";
			String str14 = "";

			try {
				Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres",
						"password");
				PreparedStatement stmt = con.prepareStatement("select * from games where gameID=?");
				stmt.setInt(1, id);
				ResultSet rs = stmt.executeQuery();

				while (rs.next()) {
					i = rs.getInt(1);
					str1 = rs.getString(2);
					str2 = rs.getString(3);
					str3 = rs.getString(4);
					str4 = rs.getString(5);
					str5 = rs.getString(6);
					str6 = rs.getString(7);
					str7 = rs.getString(8);
					str8 = rs.getString(9);
					str9 = rs.getString(10);
					str10 = rs.getString(11);
					str11 = rs.getString(12);
					str12 = rs.getString(13);
					str13 = rs.getString(14);
					str14 = rs.getString(15);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
				model.put("game", new Game(i, str1, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12,
						str13, str14));
			return new ModelAndView(model, "templates/Template.html");
		}, new VelocityTemplateEngine());
        
        get("/news.html", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "templates/news.html");
        }, new VelocityTemplateEngine());

        get("/login.html", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "templates/login.html");
        }, new VelocityTemplateEngine());
        
        get("/top10.html", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "templates/top10.html");
        }, new VelocityTemplateEngine());
        
        get("/index.html", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            
            String user = request.session().attribute("loginName");
            System.out.println(user + "från login till index");
            if (null != user) {
            	model.put("loggedIn", "true");
            } else {
            	model.put("loggedIn", "false");
            }
            
            return new ModelAndView(model, "templates/index.html");
        }, new VelocityTemplateEngine());
       
        get("/RegisterServlet", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String uname = request.queryParams("txtName");
    		String pass1 = request.queryParams("txtPass1");
    		String pass2 = request.queryParams("txtPass2");
 
    		if (pass1.equals(pass2)) {
    			try {
    				Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres",
    						"password");
    				PreparedStatement stmt = con.prepareStatement("select username from users where username=?");
    				stmt.setString(1, uname);
    				ResultSet rs = stmt.executeQuery();
    				if (rs.next()) {
    					if (uname.equals(rs.getString(1))) {
    						System.out.println("username exists!");
    						System.out.println(rs.next());
    						response.redirect("login.html");
    					} 
    				} else {
    					System.out.println("username free like dobby, but not for long...");
    					System.out.println("registering user...");
    					PreparedStatement stmt2 = con.prepareStatement("insert into users values (?,?)");
    					stmt2.setString(1, uname);
    					stmt2.setString(2, pass1);
    					stmt2.executeUpdate();
    					request.session(true);  
    					request.session().attribute("loginName", uname);
    					response.redirect("index.html");
    				}
    			} catch (Exception e) {
    				System.out.println(e);
    			}
    		} else {
    			System.out.println("kom hit");
    			response.redirect("login.html");
    		}
            response.redirect("index.html");
            return "templates/index.html";
        });
        
        get("/LoginServlet1", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String loginName = request.queryParams("loginName");
    		String loginPass = request.queryParams("loginPass");
    		System.out.println("test");
    		try {
    			Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres",
    					"password");
    			PreparedStatement stmt = con.prepareStatement("select password from users where username=?");
    			stmt.setString(1, loginName);
    			ResultSet rs = stmt.executeQuery();
    			if (rs.next()) {
					if (loginPass.equals(rs.getString(1))) {
						request.session(true);
						request.session().attribute("loginName", loginName);
						System.out.println("Lösenord stämmer, loggar in användare");
						request.session().attribute("loginName");      
						response.redirect("index.html");
					}
    				else{
    					 response.redirect("login.html");
    				 }
    			} else {
    				response.redirect("login.html");
    			}
    			 
    		} catch (Exception e) {
    			System.out.println(e);
    		}
            return new ModelAndView(model, "templates/index.html");
        }, new VelocityTemplateEngine());
        
        get("/logout", (request, response) -> {
            
			String userID = request.session().attribute("loginName");
			
            if (null != userID) {
            	request.session().removeAttribute("loginName");
            }
            
            response.redirect("index.html");
            return "templates/index.html";
        });
        
		get("/game/:id/RatingServlet", (request, response) -> {
			Map<String, Object> model = new HashMap<>();

			int rate = Integer.parseInt(request.queryParams("rate"));
			String rating = request.queryParams("rating");
			String userID = request.session().attribute("loginName");
			int gameID = Integer.parseInt(request.params("id"));

			try {
				Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres",
						"password");
				PreparedStatement stmt = con.prepareStatement("select * from ratings where username=? and gameID=?");
				stmt.setString(1, userID);
				stmt.setInt(2, gameID);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					System.out.println("Already voted, updating vote");
					PreparedStatement stmt3 = con.prepareStatement("UPDATE ratings SET vote=? WHERE username=? and gameID=?");				
					stmt3.setInt(1, rate);
					stmt3.setString(2, userID);
					stmt3.setInt(3, gameID);
					stmt3.executeUpdate();
					response.redirect("../Template.html");
				}
					 else {
						 if (userID != null) {
						System.out.println("Insert to db");
						PreparedStatement stmt2 = con.prepareStatement("insert into ratings values (?,?,?)");
						stmt2.setString(1, userID);
						stmt2.setInt(2, rate);
						stmt2.setInt(3, gameID);
						stmt2.executeUpdate();
						response.redirect("../../game/"+gameID);
						 } else {					 
							 System.out.println("Ej inloggad, logga in först innan du röstar");
								response.redirect("../../game/"+gameID);
						 }
					}
				}  catch (Exception e) {
				System.out.println(e);
			}    
			
			
            return new ModelAndView(model, "templates/Template.html");
        }, new VelocityTemplateEngine());
    }
}