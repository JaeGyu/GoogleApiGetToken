package me.jaegyu.token.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import me.jaegyu.token.service.GoogleApiService;

@Controller
public class CalenderController {

	@Value("${repository.google.clientid}")
	private String clientId;

	@Value("${repository.google.clientsecret}")
	private String clientSecret;

	@Value("${repository.google.callback.url}")
	private String redirectUrl;

	@Value("${repository.google.approveurl_head}")
	private String approveUrlHead;

	@Value("${repository.google.url_for_token}")
	private String urlForToken;

	private String approveUrl;

	@Autowired
	private GoogleApiService service;

	@PostConstruct
	public void createApproveUrl() {
		approveUrl = approveUrlHead + clientId + "&response_type=code&redirect_uri=" + redirectUrl
				+ "&scope=https://www.googleapis.com/auth/calendar&access_type=offline";
	}

	/*
	 * 우리가 개발하고 있는 서비스의 URL이다.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "index";
	}

	/*
	 * 구글에 세팅해 놓은 사용자 인증허용 앱을 띄운다. 이것의 의미는 사용자가 허가를 해주면 엑세스토큰 발급을 위한 "code"값을
	 * 넘겨달라는 것이다.
	 */
	@RequestMapping(value = "/get/code", method = RequestMethod.GET)
	public String getCode() {
		return "redirect:" + approveUrl;
	}

	/*
	 * 사용자가 허용을 눌러주면 여기로 리다이렉트 되어서 같이 넘어온 "code"를 가지고 httpClient를 이용해서 구글 서버에
	 * "code"에 해당하는 access_token을 발급해 달라고 요청한다. httpClient의 응답으로 access_token이
	 * 오면 엑세스 토큰 발급 받는 과정은 끝이 난다.
	 */
	@RequestMapping(value = "/cal/callback", method = RequestMethod.GET, produces = "text/plain; charset=UTF-8")
	public ModelAndView a(@RequestParam("code") String code, ModelAndView mav) throws Exception {
		String accessToken = service.getAccessToken(code, urlForToken, redirectUrl, clientId, clientSecret);

		mav.setViewName("result");
		mav.addObject("accessToken", accessToken);
		return mav;
	}

}
