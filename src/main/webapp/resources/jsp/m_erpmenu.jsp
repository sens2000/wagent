<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<c:import url="/resources/jsp/includeDefaultForm.jsp" />

<script type="text/javascript">
	$(function(){
		// 대메뉴 이벤트
		$("#MENU_UL>li").on("click",function(e)
		{
			viewSubMenu(this.id);
		});
	
		// 소메뉴 이벤트
		$("#MENU_UL>li>ul>li").on("click",function(e)
		{
			goMenuPage(this.id);
		}).on("mouseover",function(e)
		{
			$(this).addClass("submenuOn");
		}).on("mouseout",function(e)
		{
			$(this).removeClass("submenuOn");
		});
	});
	
	// 하위메뉴 관리
	function viewSubMenu(pMenuId)
	{
		var pMenu = $("#" + pMenuId);
		var cMenu = $("#" + pMenuId + "_UL");

//		pMenu.slideDown();
		pMenu.toggleClass("on");
		$('.submenu').hide();
		if(!pMenu.hasClass('on')){
			pMenu.addClass('on').siblings().removeClass('on');
			cMenu.show();
		} else {
			$('.gnb').find('li').removeClass('on');
		}
	}
	
	// 페이지 이동(나중엔 공통으로 이동)
	function goMenuPage(pageId)
	{
		// 소문자로 변경
		pageId = pageId.toLowerCase();
		
		location.href = "/" + pageId + ".do";
	}
	
	/**
	 *	패치노트 확인하기
	 */
	function goUpdateDetail()
	{
		goPage("sy9006", "SEARCH", "NEW");
	}
</script>


<div class="header" >
<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>

      </button>
      <a class="navbar-brand" href="/m_main.do">SENS4U</a>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul id="MENU_UL" class="nav navbar-nav" >
<!--      
        <li class="active"><a href="#">Home</a></li>
         <li><a href="#">About</a></li>
 -->
<c:forEach var="item" items="${list.data}">
		<li class="dropdown" id="${item.GUBUN}">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">${item.PROGNAME} <span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu" id="${item.GUBUN}_UL">
	<c:forEach var="sub" items="${sublist.data}">
		<c:if test="${item.GUBUN == sub.GUBUN}">
			<li role="presentation" class="dropdown-header" id="${sub.PROGRAM}" >${sub.PROGNAME}</li>
		</c:if>
	</c:forEach> 
          </ul>
        </li>
</c:forEach> 

        
        
      </ul>
      
      <ul class="nav navbar-nav navbar-right">
        <li><a href="/m_logout.do"><span class="glyphicon glyphicon-log-in"></span> LogOut</a></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <li><a href="#"><span class="glyphicon glyphicon-user"></span>${USER.GUBUN} ${USER.USERNAME}</a></li>
      </ul>
    </div>
  </div>
</nav>
</div>