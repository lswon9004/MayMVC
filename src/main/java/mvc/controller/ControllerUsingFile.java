package mvc.controller;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mvc.command.CommandHandler;
import mvc.hello.NullHandler;
   
@WebServlet(value = "/controllerUsingFile")
public class ControllerUsingFile extends HttpServlet {

   private static final long serialVersionUID = 1L;

   // <커맨드, 핸들러인스턴스> 매핑 정보 저장
   private Map<String, CommandHandler> commandHandlerMap = new HashMap<>();

   public void init() throws ServletException {

      Properties prop = new Properties();
      // 현 어플리케이션 내 /WEB-INF/commandHandler.properties경로 지정
      String configFilePath = getServletContext().getRealPath("/WEB-INF/commandHandler.properties");
      try (FileReader fis = new FileReader(configFilePath)) {
         prop.load(fis);
      } catch (IOException e) {
         throw new ServletException(e);
      }
      Iterator keyIter = prop.keySet().iterator();
      while (keyIter.hasNext()) {
         String command = (String) keyIter.next(); // 요청
         String handlerClassName = prop.getProperty(command);// 클래스명
         try {
            // 해당 이름의 클래스 가져옴
            Class<?> handlerClass = Class.forName(handlerClassName);
            // 객체 생성

            CommandHandler handlerInstance;
            Constructor<?> constructor;

            constructor = handlerClass.getConstructor(null);

            handlerInstance = (CommandHandler) constructor.newInstance();// 모델 객체 생성

            commandHandlerMap.put(command, handlerInstance);

            // 요청 - 객체 매핑

         } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | SecurityException
               | IllegalAccessException | IllegalArgumentException | InstantiationException e) {
            throw new ServletException(e);
         }
      }
   }// init end

   public void doGet(HttpServletRequest request, HttpServletResponse response) 
		   throws ServletException, IOException {
      process(request, response);
   }// doGet 끝 => response 전달

   protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      process(request, response);
   }

   //
   private void process(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      String command = request.getParameter("cmd");//
      CommandHandler handler = commandHandlerMap.get(command);//
      if (handler == null) {
         handler = new NullHandler();
      }
      String viewPage = null;
      try {
         //
         viewPage = handler.process(request, response);
      } catch (Throwable e) {
         throw new ServletException(e);
      }
      if (viewPage != null) {
         RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
         dispatcher.forward(request, response);
      }
   }// process()
}
