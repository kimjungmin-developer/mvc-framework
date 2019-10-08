package slipp.controller;

import nextstep.mvc.asis.Controller;
import slipp.annotationcontroller.UserSessionUtils;
import slipp.support.db.DataBase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ListUserController implements Controller {

    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return "redirect:/users/loginForm";
        }

        req.setAttribute("users", DataBase.findAll());
        return "/user/list.jsp";
    }
}
