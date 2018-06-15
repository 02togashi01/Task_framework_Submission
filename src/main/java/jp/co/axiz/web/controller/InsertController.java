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

import jp.co.axiz.web.entity.SessionInfo;
import jp.co.axiz.web.entity.UserInfo;
import jp.co.axiz.web.form.InsertForm;
import jp.co.axiz.web.service.impl.UserInfoService;

@Controller
public class InsertController {

	//依存性の注入(使用するファイルと変数名の宣言)
	@Autowired
	private SessionInfo sessionInfo;

	@Autowired
    MessageSource messageSource;

	@Autowired
	private UserInfoService userInfoService;

	//URLとメソッドの紐づけ
	@RequestMapping("/insert")
	public String insert(@ModelAttribute("insertForm") InsertForm form, Model model) {
		return "insert";
	}

	//URLとメソッドの紐づけ
	@RequestMapping(value = "/insertConfirm", method = RequestMethod.POST)
	public String insertConfirm(@Validated @ModelAttribute("insertForm") InsertForm form, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			String errorMsg = messageSource.getMessage("required.error", null, Locale.getDefault());
			model.addAttribute("errmsg", errorMsg);	//エラーメッセージ
			return "insert";	//insert.jspへ遷移
		}

		UserInfo user = new UserInfo();
		user.setUserName(form.getName());
		user.setTelephone(form.getTel());
		user.setPassword(form.getPassword());

		sessionInfo.setNewUser(user);

		return "insertConfirm";	//insertConfirm.jspへ遷移
	}

	//URLとメソッドの紐づけ
	@RequestMapping(value = "/insertBack")
	public String insertBack(@ModelAttribute("insertForm") InsertForm form, Model model) {

		UserInfo user = sessionInfo.getNewUser();

		form.setName(user.getUserName());
		form.setTel(user.getTelephone());
		form.setPassword(user.getPassword());

		return "insert";	//insert.jspへ遷移
	}

	//URLとメソッドの紐づけ
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String insertExecute(@Validated @ModelAttribute("insertForm") InsertForm form, BindingResult bindingResult,
			Model model) {

		UserInfo user = sessionInfo.getNewUser();

		if(!user.getPassword().equals(form.getConfirmPassword())) {
			String errorMsg = messageSource.getMessage("password.not.match.error", null, Locale.getDefault());
			model.addAttribute("errmsg", errorMsg);	//エラーメッセージ

			form.setConfirmPassword("");

			return "insertConfirm";	//insertConfirm.jspへ遷移
		}

		int id = userInfoService.insert(user);

		sessionInfo.setNewUser(null);

		form.setUserId(id);

		model.addAttribute("user", sessionInfo.getLoginUser());

		return "insertResult";	//insertResult.jspへ遷移
	}
}
