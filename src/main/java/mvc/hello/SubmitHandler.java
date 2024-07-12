package mvc.hello;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mvc.command.CommandHandler;

public class SubmitHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) {
		String id = req.getParameter("id");
		String pwd =req.getParameter("pwd");
		if(id.equals(pwd)) {
			req.getSession().setAttribute("SUCCESS", id);
			return "/WEB-INF/view/submit.jsp";
		}else {
			req.getSession().setAttribute("FAIL", id);
		}
		
		return "/WEB-INF/view/submit.jsp";
	}

}