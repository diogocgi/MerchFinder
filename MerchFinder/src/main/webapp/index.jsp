<%-- 
    Document   : index
    Created on : Mar 10, 2019, 10:38:36 AM
    Author     : diogo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="main.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        
        <title>MerchFinder</title>
    </head>
    <body>
        <div class="topnav">
            <a href="#home" id="home-btn">MerchFinder</a>
            <!-- <a href="#Alarm" id="alarm-nav-btn">Alarm</a> -->
        </div>

        <div align="center" id = "main-text">
            <p style="font-size: 40px;">SEARCH PRODUCT</p>
            <p>Searching products using the Ebay API...</p>
        </div>
        
        <div id="search-product" align="center">
            <!--action="/action_page.php"-->
            <form class="search-form" method="GET" action="getEbaySearchResults" style="margin:auto; max-width:600px">
                <input type="text" autocomplete="off" placeholder="Search.." name="search">
                <button type="submit"><i class="fa fa-search"></i></button>
            </form>
        </div>
        
        <div id="list-of-products" align="center">
            
             <%
                // print the product divs in the new page that's going to be displayed to the user
                String isdataInBD = (String)request.getAttribute("dataInDB");
                if (isdataInBD != null)
                {
                    if (isdataInBD.equals("true"))
                    {
                        String searchtext = (String) request.getAttribute("searchResult");

                        out.println(searchtext);
                    }
                    else
                    {
                        out.println("No data in cache");
                    }
                }
            %>

        </div>
        
    </body>
</html>
