package jp.co.axiz.web.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.axiz.web.entity.Admin;
import jp.co.axiz.web.entity.SessionInfo;
import jp.co.axiz.web.form.LoginForm;
import jp.co.axiz.web.service.impl.AdminService;

@Controller
public class AuthController {

//依存性の注入(使用するファイルと変数名の宣言)
	@Autowired
    MessageSource messageSource;

	@Autowired
	private AdminService adminService;

	@Autowired
	private SessionInfo sessionInfo;

	@RequestMapping("/login")
	public String login(@ModelAttribute("loginForm") LoginForm form, Model model) {
		return "login";	//login.jspへ遷移
	}

//URLとメソッドの紐づけ
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@Validated @ModelAttribute("loginForm") LoginForm form, BindingResult bindingResult,
			Model model) {

		String errorMsg = messageSource.getMessage("login.error", null, Locale.getDefault());

		if (bindingResult.hasErrors()) {
			model.addAttribute("errmsg", errorMsg);	//エラーメッセージ
			return "login";	//login.jspへ遷移
		}

		Admin admin = adminService.authentication(form.getLoginId(), form.getPassword());

		if (admin == null) {
			model.addAttribute("errmsg", errorMsg);	//エラーメッセージ
			return "login";	//login.jspへ遷移
		} else {
			sessionInfo.setLoginUser(admin);
			model.addAttribute("user", sessionInfo.getLoginUser());
			return "menu";	//menu.jspへ遷移
		}
	}

//URLとメソッドの紐づけ
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public String logout(Model model) {
		sessionInfo.invalidate();
		return "logout";	//logout.jspへ遷移
	}
}
