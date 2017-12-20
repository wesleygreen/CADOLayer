package ca.joshtaylor.CADO;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This class builds up company business objects from the CADO database. The CADO Database returns company information
 * (which is captured in the Company object) and more detailed information on companies, such as Addresses and Directors
 * (which is captured in the CompanyDetail object)
 *
 * This class is completely dependent on the website code.
 * This class is designed to be the only change necessary if style or data display on the CADO site changes.
 * (If there is a data structure change the whole package will have to be updated.)
 *
 * Created by Josh Taylor on 11/17/2017.
 */
public class CADOCompanyBuilder {

    public static final String SEARCH_PAGE_URL = "https://cado.eservices.gov.nl.ca/CadoInternet/Company/CompanyNameNumberSearch.aspx";
    public static final String COMPANY_DETAILS_PAGE_URL = "https://cado.eservices.gov.nl.ca/CadoInternet/Company/CompanyDetails.aspx";

    /**
     * Return a list of companies that are returned from a two keyword query on the CADO website.
     * This method will retrieve all of the company details - therefore, there will be a webclick / HTTP request
     * for each company returned in the query.
     *
     * If only a single keyword search is performed pass in a blank or null String.)
     *
     * @param nameKeyword1 First keyword
     * @param nameKeyword2 Second keyword (optional)
     * @return
     */
    public static List<Company> getCompanyList(String nameKeyword1, String nameKeyword2) {
        //TODO handle exceptions
        return processSearchResults(nameKeyword1, nameKeyword2, "");
    }

    /**
     * Get a list of Companies objects from a business number. Usually the list will only contain the most current version
     * of the company object for the business number. However, in some cases, previous versions of the companies are also listed,
     * notably earlier versions of the company with different names
     *
     * @param businessNumber Corporation Number used to index CADO database
     * @return Companies with that business number.
     */
    public static List<Company> getCompanyList(String businessNumber) {
        //TODO handle exceptions
        return processSearchResults(null, null, businessNumber);
    }

    public static CompanyDetail getCompanyDetail(String businessNumber) throws CADOSearchError {

        //Retrieve the search results page
        HtmlPage resultsPage = getSearchResultsPage("", "", businessNumber);

        if (resultsPage.getBaseURL().toString().equals(COMPANY_DETAILS_PAGE_URL)) {
            return processCompanyDetail(resultsPage);
        } else {
            throw new CADOSearchError("Company Business Number not valid or not specific enough");
        }

    }

    /**
     * Return a list of short form companies that are returned from a query on the CADO website.
     * This method will NOT retrieve all of the company details - therefore, it's much more light weight and much
     * less detectable on the server
     *
     * @param nameKeyword1
     * @param nameKeyword2
     * @deprecated
     * @return
     */
    public static List<Company> getShortFormCompanyList(String nameKeyword1, String nameKeyword2) {
        //Retrieve the search results page
        HtmlPage resultsPage = getSearchResultsPage(nameKeyword1, nameKeyword2, "");

        if (resultsPage.getBaseURL().toString().equals(SEARCH_PAGE_URL)) {
            return processCompaniesFromSearchPage(resultsPage);
        } else {
            System.out.println("\n**UH OH - URL comparison failed");
            //TODO Deal with these
        }
        return null;
    }

    /**
     * Return company objects based on a CADO query. This method calls the getSearchResults page method to get the
     * HTML page returned and then checks to see if the page navigated to is a company details (ie. the search returned
     * a single company) or if a list of companies is appended to the current search page.
     *
     * The method will also deal with exceptions including: no results found and too many results found to display.
     *
     * @param nameKeyword1 Input into first keyword text field on CADO site
     * @param nameKeyword2 Input into second keyword text field space 2 on CADO site
     * @param businessNumber Input into Business Number text field on CADO site
     * @return
     */
    private static List<Company> processSearchResults(String nameKeyword1, String nameKeyword2, String businessNumber) {

        //Retrieve the search results page
        HtmlPage resultsPage = getSearchResultsPage(nameKeyword1, nameKeyword2, businessNumber);

        if (resultsPage.getBaseURL().toString().equals(SEARCH_PAGE_URL)) {
            return processCompaniesFromSearchPage(resultsPage);

        } else if (resultsPage.getBaseURL().toString().equals(COMPANY_DETAILS_PAGE_URL)) {
            //There is only a single company shown on a detailed page. Build a list with just one company
            //TODO why process details and chuck them? Fix this - perhaps save the details somewhere
            LinkedList<Company> companyList = new LinkedList<>();
            companyList.addFirst(processCompanyDetail(resultsPage).getCompany());
            return companyList;

        } else {
            System.out.println("\n**UH OH - URL comparison failed");
            //TODO Deal with these
        }
        return null;
    }

    /**
     * Execute a search on the CADO main page and return the resulting response. Page may vary between a company list
     * or a detail company view. This method contains all the information about the structure of the search form.
     *
     * @return HtmlPage containing search results
     */
    private static HtmlPage getSearchResultsPage(String nameKeyword1, String nameKeyword2, String businessNumber) {

        //TODO handle exception
        HtmlPage resultsPage = null;

        try {
            WebClient webClient = new WebClient();
            webClient.getCookieManager().clearCookies();

            HtmlPage page = webClient.getPage(SEARCH_PAGE_URL);

            //TODO Watch out for this
            //https://cado.eservices.gov.nl.ca/CADOInternet/ErrorPage.aspx?aspxerrorpath=/CADOInternet/CompanyDetail/CompanyNameNumberSearch.aspx
            //System.out.println("First HTML Grab>\n\n" + page.asXml());

            //Grab the form
            HtmlForm form = page.getForms().get(0);     //Grab the form. It's the only form.

            //Populate the form
            if (nameKeyword1 != null && !nameKeyword1.isEmpty()) {
                form.getInputByName("txtNameKeywords1").setValueAttribute(nameKeyword1);
            }
            if (nameKeyword2 != null && !nameKeyword2.isEmpty()) {
                form.getInputByName("txtNameKeywords2").setValueAttribute(nameKeyword2);
            }
            if (businessNumber != null && !businessNumber.isEmpty()) {
                form.getInputByName("txtCompanyNumber").setValueAttribute(businessNumber); //TODO Check that it doesn't exceed 8 char
            }

            HtmlImageInput searchButton = (HtmlImageInput) form.getInputsByName("btnSearch").get(0);

            //TODO look into removing this casting.
            resultsPage = (HtmlPage) searchButton.click();

        }
        catch (ElementNotFoundException e){
            System.out.println("Element not found exception");
            System.err.println(e);

        } catch (Exception e) {         //if an HTTP/connection error occurs, handle JauntException.
            System.err.println(e);
        }

        return resultsPage;
    }

    //TODO This needs to be fixed

    /**
     * Build up a list of companies from a search page that has multiple company results. This involves clicking through
     * a link for each company which involves another HTTP request - a "Deep" search.
     * @param resultsPage a results page from CADO search
     * @return List of company objects build from search results page
     * @deprecated FIX or Delete!
     */
    private static List<CompanyDetail> processCompanyDetailFromSearchPage(HtmlPage resultsPage) {

        LinkedList<CompanyDetail> companyDetailList = new LinkedList<>();

        System.out.println("Results page dump>\n\n" + resultsPage.asXml());

        //Grab the Table Search Results
        HtmlTable resultsTable = (HtmlTable) resultsPage.getElementById("tableSearchResults");

        //TODO handle more than one table
        try {
            HtmlTableRow row = resultsTable.getRows().get(5);
            HtmlTable smallestResultsTable = (HtmlTable) (row.getElementsByTagName("table").get(0)).getElementsByTagName("table").get(0);

            //Note: THIS IS INCREDIBLY UNSTABLE
            //TODO Check for site updates
            DomNodeList<HtmlElement> tdElementList = smallestResultsTable.getElementsByTagName("td");

        /*
            This code is messy because the website is not developed well.
         */
            for (int i = 0; i < tdElementList.size(); i++) {
                System.out.printf("\n%d> %s ", i, tdElementList.get(i).getChildNodes().get(0).asText());
                if (i % 5 == 0 && i >= 5 & i < tdElementList.size() - 4) {
                    System.out.println(tdElementList.get(i).getFirstChild().asXml());
                    HtmlAnchor companyLink = (HtmlAnchor) tdElementList.get(i).getFirstChild();

                    //TODO look into removing this casting.
                    //resultsPage = (HtmlPage) searchButton.click();

                        //System.out.println(">" + companyLink.click().getUrl());
                    try {
                        HtmlPage companyDetailsPage = companyLink.click();
                        System.out.println(companyDetailsPage.getUrl());
                        //System.out.println(companyDetailsPage.asXml());

                        //TODO Fix this mess

//                        System.out.println("Building company> " + tdElementList.get(i+2).getFirstChild().asText());
//                        CompanyDetail company = processCompanyDetail(((HtmlAnchor) tdElementList.get(i).getFirstChild()).click());
//                        System.out.println("Result>" + company.getName());
//                        companyDetailList.addAll(processSearchResults("", "", tdElementList.get(i+2).getFirstChild().asText()));
//                        companyDetailList.add(processCompanyDetail(((HtmlAnchor) tdElementList.get(i).getFirstChild()).click()));


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        } catch (NullPointerException e) {
            e.printStackTrace();

            //TODO what to do with the alert
            /*
            com.gargoylesoftware.htmlunit.javascript.host.Window alert
            WARNING: window.alert("Search returned more than 300 matching results,
            please refine your search criteria and try again.") no alert handler installed
             */
        }
        return companyDetailList;
    }

    /**
     * Build up a list of companies from a search page that has multiple company results. This involves clicking through
     * a link for each company which involves another HTTP request
     * @param resultsPage a results page from CADO search
     * @return List of company objects build from search results page
     */
    private static List<Company> processCompaniesFromSearchPage(HtmlPage resultsPage) {

        LinkedList<Company> companyList = new LinkedList<>();

        /*
                1) The multiple companies return in a table that's tagged with 'tableSearchResults'.
                    Grab that in resultsTable

                2) Within that table is a table within another table (seriously!) but it's nested inside a row.  Grab the row and then grab
                    a reference to the table within it.
                3) Within the inner table there are there are 5 rows (even though it's all one row) and each row has a single 'td' tag.
                    Note: td tags are used here because there are no span tags

                    These td tags contain the following information:
                    > CompanyDetail Name
                    > Status
                    > CompanyDetail Number
                    > Corporation Type
                    > Incorporation Date

                ++ This code all needs to update if the website changes. ++
         */
        try {
            HtmlTable resultsTable = (HtmlTable) resultsPage.getElementById("tableSearchResults");
            HtmlTableRow row = resultsTable.getRows().get(5);
            HtmlTable smallestResultsTable = (HtmlTable) (row.getElementsByTagName("table").get(0)).getElementsByTagName("table").get(0);
            DomNodeList<HtmlElement> tdElementList = smallestResultsTable.getElementsByTagName("td");

            for (int i = 0; i < tdElementList.size(); i++) {
                if (i % 5 == 0 && i >= 5 & i < tdElementList.size() - 4) {
                    companyList.add(new Company.CompanyBuilder(
                            tdElementList.get(i).getChildNodes().get(0).asText(),
                            tdElementList.get(i+2).getChildNodes().get(0).asText()
                            )
                            .status(tdElementList.get(i+1).getChildNodes().get(0).asText())
                            .corporationType(tdElementList.get(i+3).getChildNodes().get(0).asText())
                            .incorporationDate(tdElementList.get(i+4).getChildNodes().get(0).asText())
                            .build());
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();

            //TODO what to do with the alert
            /*
            com.gargoylesoftware.htmlunit.javascript.host.Window alert
            WARNING: window.alert("Search returned more than 300 matching results,
            please refine your search criteria and try again.") no alert handler installed
             */
        }

        return companyList;
    }

    /**
     * Build up a CompanyDetail from a details company page generated by a Business Number search or link click
     * on CAD0
     *
     * @param companyDetailsPage
     * @return CompanyDetail
     * @warning Very dependent on the CADO CompanyDetail.aspx page
     */
    private static CompanyDetail processCompanyDetail(HtmlPage companyDetailsPage) {
        //Grab the Main Table
        HtmlTable resultsTable = (HtmlTable) companyDetailsPage.getElementById("mainTable");

        //System.out.println(resultsTable.asXml());

        /*
        All of the data on the website are returned in Span tags with an id.
        1) Grab span tags
        2) Populate a Hash Map with span tags with "lbl" in them using the label as the key
            (We will use this to populate the CompanyDetail using the Builder object
        Note: all data has an ID containing "lbl".  This could break
         */
        DomNodeList<HtmlElement> spanList = resultsTable.getElementsByTagName("span");  //Grab all the span tag
        HashMap<String, HtmlElement> lblHash = new HashMap<>();
        for (HtmlElement element : spanList) {
            if (element.getId().contains("lbl")) {
                //TODO remove these debug statements
                //System.out.printf("\n> %s, %s", element.getId(), element.asText());
                lblHash.put(element.getId(), element);
            }
        }

    /*
        +++ NOTE this is completely dependent on the website. Prone to change. +++

        DETAILS                                     SPAN TAG ID
        CompanyDetail Name: 	Connadh Enterprises Inc.    lblCompanyName
        Not in Good Standing                        GoodStandingUC_lblGoodStanding
        CompanyDetail Number:	64104	                    lblCompanyNumber
        Corporation Type:	CompanyDetail                 lblCorporationType
        Status:	Active	                            lblStatus
        Category:	Local                           lblCategory
        Last Annual Return:	2015-12-31              lblLastAnnualReturn
        Business Type:	With Share Capital          lblBusinessType
        Incorporation Jurisdiction:	NL	            lblIncorporationJurisdiction
        Filing Type:	Incorporation With Share Capital        lblFilingType
        Incorporation Date:	2011-01-14	            lblIncorporationDate
        Min/Max Directors:	1 / 10                  lblMinMaxDirectors
        Additional Information:                     lblAddInfo

        (Latest addresses on file)
        Registered Office:                          lblROContact        //This label is often absent
        132 Bond Street                             lblROAddress1
                                                    lblROAddress2
                                                    lblROAddress3
        St. John's                                  lblROCity
        NL                                          lblROProvinceState
        Canada                                      lblROCountry
        A1C 1T9                                     lblROPostalZipCode

        Mailing Address:
        Corporate Services Department               lblMAContact
        Stewart McKelvey                            lblMAAddress1
        PO Box 5038                                 lblMAAddress2
        St. John's                                  lblMACity
        NL                                          lblMAProvinceState
        Canada                                      lblMACountry
        A1C 5V3                                     lblMAPostalZipCode
    */

        //TODO We May be to error handle this

        return new CompanyDetail.CompanyDetailBuilder(  //Constructor
                //Build the Company
                new Company.CompanyBuilder(
                        lblHash.get("lblCompanyName").asText(),
                        lblHash.get("lblCompanyNumber").asText()
                        )
                        .status(lblHash.containsKey("lblStatus") ? lblHash.get("lblStatus").asText() : "")
                        .corporationType(lblHash.containsKey("lblCorporationType") ? lblHash.get("lblCorporationType").asText() : "")
                        .incorporationDate(lblHash.containsKey("lblIncorporationDate") ? lblHash.get("lblIncorporationDate").asText() : "")
                        .build()
                    )
                //Set the Company Detail Parameters
                .category(lblHash.containsKey("lblCategory") ? lblHash.get("lblCategory").asText() : "")
                .lastAnnualReturn(lblHash.containsKey("lblLastAnnualReturn") ? lblHash.get("lblLastAnnualReturn").asText() : "")
                .businessType(lblHash.containsKey("lblBusinessType") ? lblHash.get("lblBusinessType").asText() : "")
                .incorporationJurisdiction(lblHash.containsKey("lblIncorporationJurisdiction") ? lblHash.get("lblIncorporationJurisdiction").asText() : "")
                .filingType(lblHash.containsKey("lblFilingType") ? lblHash.get("lblFilingType").asText() : "")
                .minMaxDirectors(lblHash.containsKey("lblMinMaxDirectors") ? lblHash.get("lblMinMaxDirectors").asText() : "")
                .additionalInfo(lblHash.containsKey("lblAddInfo") ? lblHash.get("lblAddInfo").asText() : "")
                .inGoodStanding(lblHash.containsKey("GoodStandingUC_lblGoodStanding") ? lblHash.get("GoodStandingUC_lblGoodStanding").asText().trim().equals("") : false)
                .registeredAddress( //Build up the Registered Address
                        new CompanyAddress.CompanyAddressBuilder()
                            .contact(lblHash.containsKey("lblROContact") ? lblHash.get("lblROContact").asText() : "")
                            .streetLine1(lblHash.containsKey("lblROAddress1") ? lblHash.get("lblROAddress1").asText() : "")
                            .streetLine2(lblHash.containsKey("lblROAddress2") ? lblHash.get("lblROAddress2").asText() : "")
                            .streetLine3(lblHash.containsKey("lblROAddress3") ? lblHash.get("lblROAddress3").asText() : "")
                            .city(lblHash.containsKey("lblROCity") ? lblHash.get("lblROCity").asText() : "")
                            .province(lblHash.containsKey("lblROProvinceState") ? lblHash.get("lblROProvinceState").asText() : "")
                            .country(lblHash.containsKey("lblROCountry") ? lblHash.get("lblROCountry").asText() : "")
                            .postalCode(lblHash.containsKey("lblROPostalZipCode") ? lblHash.get("lblROPostalZipCode").asText() : "")
                            .build())
                .mailingAddress(    //Build up the Company Address
                        new CompanyAddress.CompanyAddressBuilder()
                            .contact(lblHash.containsKey("lblMAContact") ? lblHash.get("lblMAContact").asText() : "")
                            .streetLine1(lblHash.containsKey("lblMAAddress1") ? lblHash.get("lblMAAddress1").asText() : "")
                            .streetLine2(lblHash.containsKey("lblMAAddress2") ? lblHash.get("lblMAAddress2").asText() : "")
                            .streetLine3("")        //There isn't one in the CADO results so set it blank to avoid NULL
                            .city(lblHash.containsKey("lblMACity") ? lblHash.get("lblMACity").asText() : "")
                            .province(lblHash.containsKey("lblMAProvinceState") ? lblHash.get("lblMAProvinceState").asText() : "")
                            .country(lblHash.containsKey("lblMACountry") ? lblHash.get("lblMACountry").asText() : "")
                            .postalCode(lblHash.containsKey("lblMAPostalZipCode") ? lblHash.get("lblMAPostalZipCode").asText() : "")
                            .build())
                .build();
    }
}


