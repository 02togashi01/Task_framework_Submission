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
import jp.co.axiz.web.form.DeleteForm;
import jp.co.axiz.web.service.impl.UserInfoService;

@Controller
public class DeleteController {

	//依存性の注入(使用するファイルと変数名の宣言)
	@Autowired
	private SessionInfo sessionInfo;

	@Autowired
    MessageSource messageSource;

	@Autowired
	private UserInfoService userInfoService;

	@RequestMapping("/delete")
	public String delete(@ModelAttribute("deleteForm") DeleteForm form, Model model) {
		return "delete";
	}

	//URLとメソッドの紐づけ
	@RequestMapping(value = "/deleteConfirm", method = RequestMethod.POST)
	public String deleteConfirm(@Validated @ModelAttribute("deleteForm") DeleteForm form, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			String errorMsg = messageSource.getMessage("required.error", null, Locale.getDefault());
			model.addAttribute("errmsg", errorMsg);	//エラーメッセージ
			return "delete";	//delete.jspへ遷移
		}

		UserInfo user = userInfoService.findById(form.getUserId());

		if(user == null) {
			String errorMsg = messageSource.getMessage("id.not.found.error", null, Locale.getDefault());
			model.addAttribute("errmsg", errorMsg);	//エラーメッセージ
			return "delete";	//delete.jspへ遷移
		}

		form.setName(user.getUserName());
		form.setTel(user.getTelephone());

		return "deleteConfirm";	//deleteConfirm.jspへ遷移
	}

	//URLとメソッドの紐づけ
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deleteExecute(@Validated @ModelAttribute("deleteForm") DeleteForm form, BindingResult bindingResult,
			Model model) {

		int id = form.getUserId();

		userInfoService.delete(id);

		model.addAttribute("user", sessionInfo.getLoginUser());

		return "deleteResult";	//deleteResult.jspへ遷移
	}
}
