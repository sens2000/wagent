<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  %>
<%@ page import="com.sens.*"  %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
  <title>ERP ${USER.COMNAME}</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
<!--
	<link rel="stylesheet" type="text/css" href="/resources/vendor/bootstrap/css/bootstrap.css">
-->	
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
	<script src="/resources/vendor/jquery/jquery-3.2.1.min.js"></script>
	<script src="/resources/vendor/bootstrap/js/popper.js"></script>
	<script src="/resources/vendor/bootstrap/js/bootstrap.min.js"></script>  
	
  <style>
    /* Remove the navbar's default margin-bottom and rounded borders */ 
	 {
	    margin: 0px;
	    padding: 0px;
	}
	html, body {
	    height: 100%;
/*	    
	    background-color: yellow;
*/	    
	}
	.back {
	    height: 98%; 
	    border: 3px solid blue;}
	}
    
    .navbar {
      margin-bottom: 0;
      border-radius: 0;
    }
    
    /* Set height of the grid so .sidenav can be 100% (adjust as needed) */
    .row.content {
    height: 90vh ;
    }
    
    /* Set gray background color and 100% height */
    .sidenav {
      padding-top: 20px;
      background-color: #f1f1f1;
      height: 100%;
    }
    
    /* Set black background color, white text and some padding */
    footer {
		background-color: #555;
		color: white;
		padding: 7px;
      
		position: absolute;
		left: 0;
		bottom: 0;
		width: 100%;
          
    }
    
    /* On small screens, set height to 'auto' for sidenav and grid */
    @media screen and (max-width: 767px) {
      .sidenav {
        height: auto;
        padding: 15px;
      }
      .row.content {height:auto;} 
    }
  </style>	
</head>
