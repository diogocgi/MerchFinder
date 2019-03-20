/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ua.merchfinder.controllers;

import com.ua.merchfinder.entities.Productssearches;
import com.ua.merchfinder.session.ProductssearchesFacade;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author diogo
 */
public class GetEbaySearchResultsCtrl extends HttpServlet {

    @EJB
    private ProductssearchesFacade customerFacade;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GetEbaySearchResultsCtrl</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GetEbaySearchResultsCtrl at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // get value of the form with name "search"
        String searchText = request.getParameter("search");
        
        boolean contentFound = false;
        
        String dbData;
        
        // try two times to fetch information from the database. If none are successfull, no results were found
        for (int i = 0; i < 2; i++)
        {
            dbData = searchTextInDB(searchText);

            if (!dbData.equals("")) // means that the searched product is in the database (in cache)
            {
                request.setAttribute("dataInDB", "true");
                passDataToJSP(request, response, "index.jsp", dbData);
                contentFound = true;
            }
            else   // the product is not in the database, so we need to use the REST API
            {
                populateDBwithDataFromRESTapi(searchText);
            }
            
            if(contentFound)
                break;
        }
        
        // no results found
        request.setAttribute("dataInDB", "false");
        passDataToJSP(request, response, "index.jsp", "No results were found...");
    }
    
    /**
     * Stores in the database the search results obtained from the ebay REST API
     * @param searchName
     * @throws ProtocolException
     * @throws MalformedURLException
     * @throws IOException 
     */
    public void populateDBwithDataFromRESTapi(String searchName) throws ProtocolException, MalformedURLException, IOException 
    {
        /* ---------- ADD HERE THE EBAY PRODUCTION ENVIRONMENT appID/clientID/appName ----------- */
        String appName = "ADD HERE!";  
        String responseDataFormat = "JSON";
        String searchWord = "";

        String checkForOnlySpaces = "";
              
        searchWord = "" + searchName;

        checkForOnlySpaces = searchWord.replace(" ", "");

        // If the search word doesn't have characters (only consists of spaces),don't print anything
        if (!checkForOnlySpaces.equals(""))
        {
            // suport for search words with spaces
            searchWord = searchWord.replace(" ", "%20");

            // https://developer.ebay.com/api-docs/static/make-a-call.html
            String url = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsByKeywords&SERVICE-VERSION=1.13.0&SECURITY-APPNAME=" + appName + "&RESPONSE-DATA-FORMAT=" + responseDataFormat + "&REST-PAYLOAD&keywords=" + searchWord;

            URL obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // o default request type is GET
            con.setRequestMethod("GET");
            // add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = con.getResponseCode();
            
            BufferedReader in = new BufferedReader (new InputStreamReader (con.getInputStream()));
            
            String inputLine;
            StringBuffer response2 = new StringBuffer();
            
            while((inputLine = in.readLine()) != null)
            {
                response2.append(inputLine);
            }
            
            in.close();

            // print all received JSON in the page
            // out.println(response2.toString());

            // reading JSON response
            JSONObject myResponse = new JSONObject(response2.toString());
            
            // findItemsByKeywordsResponse é um array/ lista JSON

            /* number of products received in the JSON string (sent by Ebay API)
             * int numberOfItems = myResponse.getJSONArray("findItemsByKeywordsResponse")
             *                               .getJSONObject(0)
             *                               .getJSONArray("searchResult")
             *                               .getJSONObject(0)
             *                               .getJSONArray("item").length();
            */
            
            JSONArray jsonItemArray = myResponse.getJSONArray("findItemsByKeywordsResponse")
                                                .getJSONObject(0)
                                                .getJSONArray("searchResult")
                                                .getJSONObject(0)
                                                .getJSONArray("item");
            int numberOfItems = jsonItemArray.length();

            String tmpImgStr = "";
            String tmpProductName = "";
            String tmpPrice = "";
            String tmpCurrency = "";
            String tmpItemurl = "";
            String imgSrc = "";

            // 1 div per product
            for (int i = 0; i < numberOfItems; i++) {

                // get product url
                tmpItemurl = "";
                tmpItemurl += jsonItemArray.getJSONObject(i)
                                           .getJSONArray("viewItemURL")
                                           .getString(0);
 
                // get product image
                imgSrc = "";
                imgSrc = jsonItemArray.getJSONObject(i)
                                      .getJSONArray("galleryURL")
                                      .getString(0);

                // get product price
                tmpPrice = "";
                tmpPrice += jsonItemArray.getJSONObject(i)
                                         .getJSONArray("sellingStatus")
                                         .getJSONObject(0)
                                         .getJSONArray("convertedCurrentPrice")
                                         .getJSONObject(0)
                                         .getString("__value__");

                // currency
                tmpCurrency = "";
                tmpCurrency += jsonItemArray.getJSONObject(i)
                                            .getJSONArray("sellingStatus")
                                            .getJSONObject(0)
                                            .getJSONArray("convertedCurrentPrice")
                                            .getJSONObject(0)
                                            .getString("@currencyId");    

                // product name
                tmpProductName = "";
                tmpProductName += jsonItemArray.getJSONObject(i)
                                               .getJSONArray("title")
                                               .getString(0);      // to remove the characters [ ] that delimite the json array 
                
                storeSearchResultInDB(searchName, tmpProductName,imgSrc, tmpItemurl, tmpCurrency, Double.parseDouble(tmpPrice));
            }
        }           
    }
    
    /**
     * 
     * @param searchName
     * @param productName
     * @param pimage_link
     * @param plink
     * @param currency
     * @param price 
     */
    private void storeSearchResultInDB(String searchName, String productName, String pimage_link, String plink, String currency, double price)
    {
        Productssearches search = new Productssearches();
        //search.setId(1);      // auto increment
        search.setPname(productName);
        search.setSearchText(searchName);
        search.setCurrency(currency);
        search.setPimageLink(pimage_link);
        search.setPrice(BigDecimal.valueOf(price));
        search.setPlink(plink);
        
        // store in the database
        customerFacade.Save(search);     
    }
    
    /**
     * Search in the database for products with the specified search word
     * @param searchText
     * @return A group of structured <div>'s with each one having information about 
     * a product. If there are no products in the db for the specified search word, 
     * this method returns an empty string
     */
    private String searchTextInDB(String searchText)
    {
        // read data from the database
        List<Object[]> rows = customerFacade.getSearchResultsFromDB(searchText);

        // pname1.*.plink1.*.price.*.pimage_link1.*.currency
        
        String htmlStr = "";
        String tmp = "";
        for (Object[] row : rows) {
            tmp = "";
            tmp = "<a class=\"product-a-ref\" style=\"display:block;\" href=\"" + row[1] + "\">";
            tmp += "<div class=\"product-div\" style=\"height:300px; width:300px; float: left; background: #f9f9f9; box-shadow: 0px 2px 8px 0px rgba(0,0,0,0.2); z-index: 1; opacity: 0;\">";
            tmp += "<img class=\"product-img\" src=\"" + row[3] + "\" style=\"border:none;\">";
            tmp += "<span><p>" + row[2] + " " + row[4] + "</p>";
            tmp += "<p>" + row[0] + "</p><span>";
            tmp += "</div>"  + "</a>";
            //System.out.println(tmp);
            htmlStr += tmp;
        }
        
        return htmlStr;
    }
    
    /**
     * Send the data to the specified jsp file (this data are the <div>'s for the products to
     * be displayed in a grid in the browser)
     * 
     * @param request
     * @param response
     * @param jspFileName
     * @param data
     * @throws ServletException
     * @throws IOException 
     */
    private void passDataToJSP(HttpServletRequest request, HttpServletResponse response, String jspFileName, String data) throws ServletException, IOException
    {
        request.setAttribute("searchResult", data);
        
        RequestDispatcher rd = request.getRequestDispatcher(jspFileName);
        rd.forward(request, response);
    }
    
    /**
     * Only for testing purposes: store a record in the database.
     * 
     * @param searchName
     * @return String 
     */
    private String storeSearchResultInDB(String searchName)
    {
        
        Productssearches search = new Productssearches();
        //search.setId(1);      // auto increment
        search.setPname("hello name");
        search.setSearchText(searchName);
        search.setCurrency("EUR");
        search.setPimageLink("hello image link");
        search.setPrice(BigDecimal.valueOf(123).movePointLeft(2));
        search.setPlink("product mylink");
        
        // guardar na base de dados
        customerFacade.Save(search);
        
        // ler da base de dados
        List<Object[]> rows = customerFacade.getSearchResultsFromDB(searchName);
        
        String tmp = "";
        for (Object[] row : rows) {
            tmp += "pname: " + row[0] + ", plink: " + row[1] + ", pimage_link: " + row[2] + 
                   ", price: " + row[3] + ", currency: " + row[4];
            
        }
        
        // pname,plink,pimage_link,price,currency
        
        return tmp;
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet controller for the ebay search results";
    }

}
