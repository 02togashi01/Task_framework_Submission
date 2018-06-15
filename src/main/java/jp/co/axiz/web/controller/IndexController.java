package jp.co.axiz.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.axiz.web.entity.SessionInfo;

@Controller
public class IndexController {

//依存性の注入(使用するファイルと変数名の宣言)
	@Autowired
	private SessionInfo sessionInfo;

//URLとメソッドの紐づけ
	@RequestMapping("/index")
	public String index(Model model) {
		return "index";	//index.jspへ遷移
	}

//URLとメソッドの紐づけ
	@RequestMapping("/menu")
	public String menu(Model model) {
		model.addAttribute("user", sessionInfo.getLoginUser());
		return "menu";	//menu.jspへ遷移
	}

}
