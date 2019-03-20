/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ua.merchfinder.scheduler;

import com.ua.merchfinder.entities.Productssearches;
import com.ua.merchfinder.kafka.EventProducer;
import com.ua.merchfinder.session.ProductssearchesFacade;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author diogo
 */
@Startup
@Singleton
public class TimerSessionBean {

    /**
     * Inject a reference to the ProductssearchesFacade EJB instance into the TimerSessionBean EJB.
     */
    @EJB
    private ProductssearchesFacade psFacade;
    
    /**
     * This interface provides access to the runtime session context that the container 
     * provides for a session bean instance.
     * The resources annotation Specifies a dependency on an external resource.
     */
    @Resource
    private SessionContext context;
    
    /**
     * Counter to iterate through all different searches in the database
     */
    private int counter = 0;

    /**
     * Kafka event producer
     */
    private EventProducer producer;
    
    /**
     * updates database every 30 seconds
     * 
     * @throws ProtocolException
     * @throws MalformedURLException
     * @throws IOException 
     */
    @Schedule(hour = "*", minute = "*", second = "30")
    public void myTimer() throws ProtocolException, MalformedURLException, IOException 
    {
        producer = EventProducer.getInstance();
        
        // get list of all different search names (different searches made by the users)
        List<String> searchNamesList = psFacade.getAllDifferentSearchNames();
        
        if(searchNamesList != null)
        {
            if (this.counter >= searchNamesList.size())
            {
                // reset
                this.counter = 0;
            }

            /* ---------- ADD HERE THE EBAY PRODUCTION ENVIRONMENT appID/clientID/appName ----------- */
            String appName = "ADD HERE!";   
            String responseDataFormat = "JSON";
            String searchWord = "";

            String checkForOnlySpaces = "";

            // this string will not be altered, even if it has spaces
            String searchName = searchNamesList.get(counter);

            searchWord = "" + searchNamesList.get(counter);

            checkForOnlySpaces = searchWord.replace(" ", "");

            // If the search word doesn't have characters (only consists of spaces),don't print anything
            if (!checkForOnlySpaces.equals(""))
            {
                // suporte para pesquisas com espaços
                searchWord = searchWord.replace(" ", "%20");

                // https://developer.ebay.com/api-docs/static/make-a-call.html
                String url = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsByKeywords&SERVICE-VERSION=1.13.0&SECURITY-APPNAME=" + appName + "&RESPONSE-DATA-FORMAT=" + responseDataFormat + "&REST-PAYLOAD&keywords=" + searchWord;

                URL obj = new URL(url);

                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                
                // the default request type is GET
                con.setRequestMethod("GET");
                
                // add request header
                con.setRequestProperty("User-Agent", "Mozilla/5.0");
                int responseCode = con.getResponseCode();
                
                BufferedReader in = new BufferedReader (
                        new InputStreamReader (con.getInputStream()));
                
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

                // number of products received in the JSON string (sent by Ebay API)
                // int numberOfItems = myResponse.getJSONArray("findItemsByKeywordsResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item").length();
                
                JSONArray jsonItemArray = myResponse.getJSONArray("findItemsByKeywordsResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item");
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
                                                .getString(0);    // to remove the characters [ ] that delimite the json array 

                    storeSearchResultInDB(searchName, tmpProductName,imgSrc, tmpItemurl, tmpCurrency, Double.parseDouble(tmpPrice));
                }
            }  
            
            // next execution of this function is going to update entries for another search name
            this.counter++;
            
            // producer event
            producer.storeEvent("merchUpdateTopic", 1, "The search results for \""+ searchName+ "\" were updated!");
        }        
    }
    
    /**
     * Store one search result in the database
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
        psFacade.Save(search);     
    }
}
