package com.kh.tsp.member.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.tsp.member.model.service.MemberService;
import com.kh.tsp.member.model.service.MemberServiceImpl;
import com.kh.tsp.member.model.vo.Member;

@Controller
@SessionAttributes("loginUser")
public class MemberController {
	@Autowired
	private MemberService ms;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@RequestMapping(value = "login.me", method = RequestMethod.POST) // 의존성 주입 (DI)
	/*public String loginCheck(@RequestParam String userId
							, @RequestParam String userPwd){
		
		System.out.println("controller userId : " + userId);
		System.out.println("controller userPwd : " + userPwd);
		
		Member m = new Member();
		
		m.setUserId(userId);
		m.setUserPwd(userPwd);
		
		System.out.println("controller member : " + m);*/
	public ModelAndView/*String*/ loginCheck(Member m, /*HttpSession session,*/ /*Model*/ModelAndView mv, SessionStatus status){
		
		//비밀번호 암호화
		//나중에 회원 가입할 때 쓸거임
		//m.setUserPwd(passwordEncoder.encode(m.getUserPwd()));
		
		System.out.println("controller member : " + m);
		
		//System.out.println("암호 일치 여부 확인 : " + passwordEncoder.matches("pass01", "$2a$10$d6vu5pNLsgDv.XPdXmlk/utER.f5icPlXxkDXcbZdcH/35BFlBrUe"));
		
		//MemberService ms = new MemberServiceImpl(); //@Autowired
		
		try {
			Member loginUser = ms.loginMember(m);
			
			mv.addObject("loginUser",loginUser);
			//session.setAttribute("loginUser", loginUser);
			status.setComplete();
			mv.setViewName("main/main");
			//return "main/main";
			
		} catch (LoginException e) {
			mv.addObject("message",e.getMessage());
			mv.setViewName("common/errorPage");
			//model.addAttribute(e.getMessage());
			//return "common/errorPage";
		}
		return mv;
		
	}
	@RequestMapping(value="logout.me", method=RequestMethod.GET)
	public String logout(/*HttpSession session*/ SessionStatus status){
		//session.invalidate();
		
		status.setComplete();
		
		return "main/main";
	}
	@RequestMapping(value="memberJoinView.me")
	public String showMemberJoinView(){
		
		return "member/memberJoin";
	}
	@RequestMapping("insert.me")
	public String insertMember(Member m, Model model
			, @RequestParam(name="photo", required=false) MultipartFile photo
			, HttpServletRequest request){
		
		String root = request.getSession().getServletContext().getRealPath("resources");
		String filePath = root + "\\uploadFiles";
		
		System.out.println(filePath);
		
		try {
			System.out.println("photo : " + photo);
			photo.transferTo(new File(filePath + "\\" + photo.getOriginalFilename()));
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// 비밀번호 암호화 처리 
		m.setUserPwd(passwordEncoder.encode(m.getUserPwd()));
		
		if(m.getGender().equals("1") || m.getGender().equals("3") ){
			m.setGender("M");
		} else {
			m.setGender("F");
		}
		
		System.out.println("controller : " + m);
		
		
		try {
			ms.insertMember(m);
			return "main/main";
		} catch (RuntimeException e) {
			model.addAttribute("message","로그인실패");
			return "common/errorPage";
		}
	}
	//1.  스트림을 이용한 ajax 처리를 위한 메소드
	
	/*@RequestMapping("duplicationCheck.me")
	public void duplicationCheck(@RequestParam("userId") String userId
								, HttpServletResponse response){
		
		System.out.println("userId : " + userId);
		
		try {
			response.getWriter().print(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}*/
	
	//2. ObjectMapper를 이용한 ajax 처리를 위한 메소드
	//pox.xml에 jackson-databind dependency 추가해야 함
	/*@RequestMapping("duplicationCheck.me")
	public void duplicationCheck(String userId
								, HttpServletResponse response){
		
		ObjectMapper mapper = new ObjectMapper();
		
		Member m = new Member();
		
		m.setUserId(userId);
		
		try {
			response.getWriter().println(mapper.writeValueAsString(m));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	//3. @ResponseBody를 이용한 ajax 처리를 위한 메소드
	//pom.xml에 jackson-mapper-asl dependency 추가해야함
	//스프링 4.1 이후 버전 사용 불가
	/*@RequestMapping("duplicationCheck.me")
	public @ResponseBody HashMap<String, Object> duplicationCheck(String userId){
		HashMap<String, Object> hmap = new HashMap<String, Object>();
		
		hmap.put("userId", userId);
		
		return hmap;
	}*/
	//4. jsonView를 사용한 방식
	//pom.xml에 json-lib-ext-spring dependecy 추가해야함.
	@RequestMapping("duplicationCheck.me")
	public ModelAndView duplicationCheck(ModelAndView mv, String userId){
		Member m = new Member();
		m.setUserId(userId);
		
		mv.addObject(m);
		
		mv.setViewName("jsonView");
		
		return mv;
	}
	
}
