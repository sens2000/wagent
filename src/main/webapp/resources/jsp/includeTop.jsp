<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  %>
<%@ page import="com.sens.*"  %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
  <title>ERP ${USER.COMNAME}</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>

	<script src="/resources/vendor/bootstrap/js/popper.js"></script>
	<script src="/resources/vendor/bootstrap/js/bootstrap.min.js"></script>  

    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>
	<script src="/resources/pqgrid/pqgrid.min.js"></script>  
    <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css" />
   	<link rel="stylesheet" href="/resources/pqgrid/pqgrid.min.css" />
   	<link rel="stylesheet" href="/resources/pqgrid/pqgrid.ui.min.css" />
    <link rel="stylesheet" href="/resources/pqgrid/themes/bootstrap/pqgrid.css" />
	<link rel="stylesheet" href="/resources/pqgrid/themes/office/pqgrid.css" />

   	<link rel="stylesheet" href="/resources/pqgrid/ocalize/pq-localize-en.js" />

    <!--for Excel export-->
    <script src="https://paramquery.com/jszip-2.5.0/jszip.min.js"></script>
    <script src="https://paramquery.com/jszip-2.5.0/filesaver.js"></script>

    <script src="https://paramquery.com/pqselect/trial/pqselect.min.js"></script>
    <link href="https://paramquery.com/pqselect/trial/pqselect.min.css" type="text/css" rel="stylesheet">  
 
 	
</head>

 <style>
	html,body{
	  margin:0;
	  height:97%;
	}
	#app {
	  height: 96%;
	  display: flex;
	  flex-direction: column;
	}
	#title{
		padding : 1px 5px 1px 5px;
		margin :  1px 5px 3px 5px;
	}
	#panel_head{
		padding : 2px 5px 2px 5px;
		margin :  2px 5px 3px 5px;
	}	
	#panel_head1{
		padding : 2px 5px 2px 5px;
		margin :  2px 5px 3px 5px;
	}	
	
	 div.pq-grid *
	{
	    font-size:12px;    
	}
	
	.ui-tooltip {
		top : 1107px;
		right : 300px;
		diplay : block;
	    text-align: center;
	    padding: 3px 10px;
	    background-color: #b0cfaa;
	    color : red;
	}

    .ui-autocomplete {
        max-height: 200px;        
        overflow-y: auto;
        /* prevent horizontal scrollbar */
        overflow-x: hidden;
        border : 1px solid blue;
    }    
    .pq-side-icon.pq-grid-cell > div:before{
	    content:"▼";		
	    width: 20px;
	    height:20px;
        color: #ccc;
	    float: right;	    	           
    }
    
    .pq-grid-cell.pq-calendar > div:before{	    
	    content: "\01F4C5";
    }    
    	
    footer {
      padding: 3px;
      margin: 3px;
    }	
 </style>
 
 <!--
선택자{ 속성:값;}
p{color:black;} 형태로 사용합니다. 선택자는 한번에 여러개도 적용 가능합니다. p,h1{color:black;}
모든 선택자에 적용하고 싶을경우 *{속성:값;} 을 사용하시면 됩니다.
특정 태그가 아닌 특정 클래스이름에 스타일을 적용하고 싶을 경우 .클래스이름{속성:값};
특정 id 이름에 스타일을 적용하고 싶을 경우  #아이디이름{속성:값}; 
	#container{
	  height: 20%;
	}
	
    @media screen and (max-width: 767px) {
      #container {
        height: 50%;
      }
    }
	
padding-top: 5px;
padding-right: 5px;
padding-bottom: 5px;
padding-left: 5px;

padding: 5px 7px 3px 0px;    (위, 오른쪽, 아래, 왼쪽 순임)
[4면이 모두 같을 때 padding 지정하기]
padding: 5px;
[위, 오른쪽 왼쪽, 아래 padding 지정하기]
padding: 5px 10px 0px:
[위 아래, 오른쪽 왼쪽 padding 지정하기]
padding: 5px 10px;

 color : 글자색
 backbround-color : 배경색
 width : 폭
 height : 높이
 font-align : 글자정렬. 좌측, 우측, 가운데
 font-size : 글자 크기
 font-weight : 글자 굵기
 font-family:courier;
 border : 외곽선
 border-top:1px solid;
 border-bottom:1px solid;
 text-align:center;
 align:right;
 -->