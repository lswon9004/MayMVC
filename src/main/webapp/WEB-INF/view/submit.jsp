<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
</head>
<body>
<c:if test="${SUCCESS !=null}">
	로그인 성공
	<button onclick="location.href='logout'">로그아웃</button>
</c:if>
<c:if test="${SUCCESS ==null}">
	로그인 실패
	<script type="text/javascript">
		alert("로그인 폼으로 돌아갑니다.");
		history.go(-1);
	</script>
</c:if>
</body>
</html>