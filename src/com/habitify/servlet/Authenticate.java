package com.habitify.servlet;

import com.habitify.Login;
import com.habitify.SignUp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

/**
 * Servlet implementation class Authenticate
 */
@WebServlet("/Authenticate")
public class Authenticate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Authenticate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String uri = request.getRequestURI();
		if (uri.contains("/signedup/")) {

		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonResponse = new JSONObject();
		HttpSession session = request.getSession();

		String uri = request.getRequestURI();
		if (uri.contains("/signupform")) {
			SignUp signUp = new SignUp();
			session.setAttribute("signUpObject",signUp);
			String phoneNumber = request.getParameter("phonenumber");
			phoneNumber = "+91" + phoneNumber;
			if (signUp.canThisNumberSignup(phoneNumber)){
				if(signUp.sendOTP()){
					jsonResponse.put("status", AUTHENTICATE_CONSTANTS.SIGNUP_MESSAGE.SENT.getValue());
				}
				else{
					jsonResponse.put("status", AUTHENTICATE_CONSTANTS.SIGNUP_MESSAGE.PROBLEM_SENDING_MESSAGE.getValue());
				}
			}
			else {
				jsonResponse.put("status", AUTHENTICATE_CONSTANTS.SIGNUP_MESSAGE.USER_ALREADY_EXIST.getValue());
			}
		}
		else if (uri.contains("/signupotp")) {
			SignUp signUp = (SignUp) session.getAttribute("signUpObject");
			String otp = request.getParameter("otp");
			if (signUp != null && signUp.verifyOtp(otp)) {
				if(signUp.createUser()){
					jsonResponse.put("status", AUTHENTICATE_CONSTANTS.SIGNUP_OTP_MESSAGE.MATCH.getValue());
					jsonResponse.put("qrUrl", signUp.getQrUrl());
				}
				else {
					jsonResponse.put("status", AUTHENTICATE_CONSTANTS.SIGNUP_OTP_MESSAGE.MATCH_UNABLE_TO_CREATE_USER.getValue());
				}
			}
			else{
				jsonResponse.put("status", AUTHENTICATE_CONSTANTS.SIGNUP_OTP_MESSAGE.INCORRECT.getValue());
			}
		}
		else if(uri.contains("/loginForm")){
			String phoneNumber = request.getParameter("phonenumber");
			phoneNumber = "+91" + phoneNumber;
			String otp = request.getParameter("otp");
			Login login = new Login();
			HashMap loginResult = login.verifyCredentials(phoneNumber,otp);
			if (loginResult.get(AUTHENTICATE_CONSTANTS.STATUS) == AUTHENTICATE_CONSTANTS.LOGIN_STATUS.VALID){
				session.setAttribute("session", loginResult.get(AUTHENTICATE_CONSTANTS.SESSION));
				jsonResponse.put("status", AUTHENTICATE_CONSTANTS.LOGIN_STATUS.VALID.getValue());
			}
			else if (loginResult.get(AUTHENTICATE_CONSTANTS.STATUS) == AUTHENTICATE_CONSTANTS.LOGIN_STATUS.INVALID){
				jsonResponse.put("status", AUTHENTICATE_CONSTANTS.LOGIN_STATUS.INVALID.getValue());
			}
			else if (loginResult.get(AUTHENTICATE_CONSTANTS.STATUS) == AUTHENTICATE_CONSTANTS.LOGIN_STATUS.USER_NOT_EXIST){
				jsonResponse.put("status", AUTHENTICATE_CONSTANTS.LOGIN_STATUS.USER_NOT_EXIST.getValue());
			}
		}

		PrintWriter printWriter = response.getWriter();
		printWriter.write(jsonResponse.toString());
		printWriter.flush();
		printWriter.close();
	}

}
