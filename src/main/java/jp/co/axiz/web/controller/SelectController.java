package jp.co.axiz.web.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.axiz.web.entity.UserInfo;
import jp.co.axiz.web.form.SelectForm;
import jp.co.axiz.web.service.impl.UserInfoService;

@Controller
public class SelectController {

	//依存性の注入(使用するファイルと変数名の宣言)
	@Autowired
    MessageSource messageSource;

	@Autowired
	private UserInfoService userInfoService;

	@RequestMapping("/select")
	public String login(@ModelAttribute("selectForm") SelectForm form, Model model) {
		return "select";
	}

	//URLとメソッドの紐づけ
	@RequestMapping(value = "/list")
	public String list(@Validated @ModelAttribute("selectForm") SelectForm form, BindingResult bindingResult,
			Model model) {

		String errorMsg = messageSource.getMessage("select.error", null, Locale.getDefault());

		if (bindingResult.hasErrors()) {
			model.addAttribute("errmsg", errorMsg);	//エラーメッセージ
			return "select";	//select.jspへ遷移
		}

		UserInfo condition = new UserInfo();
		condition.setUserId(form.getUserId());
		condition.setUserName(form.getName());
		condition.setTelephone(form.getTel());

		List<UserInfo> resultList = userInfoService.find(condition);

		if(resultList.isEmpty()) {
			model.addAttribute("errmsg", errorMsg);	//エラーメッセージ
			return "select";	//select.jspへ遷移
		}else {
			model.addAttribute("userlist", resultList);
			return "selectResult";	//selectResult.jspへ遷移
		}
	}
}
