package ca.joshtaylor.CADO;
import java.util.List;
import java.util.LinkedList;

/**
 * Created by josh.taylor on 11/17/2017.
 */
public class TestCompanyDetailScraping {

    public static void main(String[] args) {

        //testShortFormCompanyQuery();
        testDetailOnSingleCompany();

        int testNum = 64104;    //Test on Connadh

//        System.out.println("\nTesting single company in list");
//        CADOCompanyBuilder.getCompanyList("Connadh", "");
//          printCompanies(CADOCompanyBuilder.getCompanyList("Key", ""));

//        System.out.println("\nTesting company number that returns company details");

        //TODO fix this later
//        LinkedList<CompanyDetail> companyList = new LinkedList<>();
//        for(int i=8000; i<8025; i++){
//            companyList.addAll(CADOCompanyBuilder.getCompanyList(i));
//        }
//        printCompanies(companyList);
//
//        try {
//            CompanyWriter.createCVSFile(companyList);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }


        System.out.println("Over!");
    }

    public static void testDetailOnSingleCompany(){

        System.out.println("\nTesting company number that returns a single companies");
        printCompanies(CADOCompanyBuilder.getCompanyList("64104"));

        //TODO Finish Address Print


    }

    public static void testShortFormCompanyQuery(){

        System.out.println("Testing Short Form CompanyDetail Query");

        LinkedList<Company> companyList = new LinkedList<>();
        CADOCompanyBuilder.getShortFormCompanyList("Key", "");
        for(Company company : companyList){
            CompanyWriter.printCompany(company, System.out);
        }

    }


    public static void printCompanies(List<Company> companyList) {
        if(companyList.isEmpty()){
            System.out.println("List is Empty, can't print yet...");
            return;
        }

        for (Company company : companyList) {
            //TODO print out company details.
            System.out.println("\n");
            CompanyWriter.printCompany(company, System.out);
        }

    }


}
