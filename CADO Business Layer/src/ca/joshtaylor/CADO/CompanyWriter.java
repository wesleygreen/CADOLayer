package ca.joshtaylor.CADO;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.List;


/**
 * A class that writes Company and CompanyDetails out to printstreams and files
 *
 * //TODO Add XML Support. Generalize the CVS printing
 *
 * Created by josh.taylor on 11/17/2017.
 */
public class CompanyWriter {

    /**
     * Print out a companies information to a print stream (ie. System.out)
     * @param company
     */
    public static final void printCompany(Company company, PrintStream printStream){
        printStream.printf(
            "\nName>\t\t\t\t\t %s " +
                "\nNumber>\t\t\t\t\t %s " +
                    "\nStatus>\t\t\t\t\t %s " +
                        "\nType>\t\t\t\t\t %s " +
                            "\nIncorp Date>\t\t\t %s",
            company.getName(),
                company.getNumber(),
                    company.getStatus(),
                        company.getCorporationType(),
                            company.getIncorporationDate()
        );
    }

    //TODO Get rid of this function ?
    /**
     * Print out a companies detailed information to a print stream (ie. System.out)
     * This method prints out Company and Address details as well.
     * @param companyDetail
     */
    public static final void printCompanyDetail(CompanyDetail companyDetail, PrintStream printStream){

        //Print out the company info first
        printCompany(companyDetail.getCompany(), printStream);

        //Print out the details
        printStream.printf(
            "\nGood standing>\t\t\t %b " +
                "\nCategory>\t\t\t\t %s " +
                    "\nLast Annual Return>\t\t %s" +
                        "\nBusiness Type>\t\t\t %s" +
                            "\nIncorp Juristiction>\t %s" +
                                "\nFiling Type>\t\t\t %s" +
                                    "\nMin/Max Directors>\t\t %s" +
                                        "\nAdditional Info>\t\t\t %s",
            companyDetail.isInGoodStanding(),
                companyDetail.getCategory(),
                    companyDetail.getLastAnnualReturn(),
                        companyDetail.getBusinessType(),
                            companyDetail.getIncorporationJurisdiction(),
                                companyDetail.getFilingType(),
                                    companyDetail.getMinMaxDirectors(),
                                        companyDetail.getAdditionalInfo()
        );

        //Print out the company information
        printCompanyAddressDetails(companyDetail.getRegisteredAddress(), printStream);
        printCompanyAddressDetails(companyDetail.getMailingAddress(), printStream);
    }

    /**
     * Print out a companies detailed information to a print stream (ie. System.out)
     * @param companyAddress
     * @param printStream
     */
    private static final void printCompanyAddressDetails(CompanyAddress companyAddress, PrintStream printStream){
        printStream.printf(
            "\nContact>\t\t\t\t %s" +
                "Street addr1>\t\t\t %s" +
                    "Street addr2>\t\t\t %s" +
                        "Street addr3>\t\t\t %s" +
                            "\nCity>\t\t\t\t\t %s" +
                                "\nProvince>\t\t\t\t %s" +
                                    "\nCountry>\t\t\t\t %s" +
                                        "\nPostal Code>\t\t\t %s"
                ,
            companyAddress.getContact(),
                companyAddress.getStreetLine1(),
                    companyAddress.getStreetLine2(),
                        companyAddress.getStreetLine3(),
                            companyAddress.getCity(),
                                companyAddress.getProvince(),
                                    companyAddress.getCountry(),
                                        companyAddress.getPostalCode()
        );

    }

    public static void printCompanyList(List<Company> companyList, PrintStream printStream) {
        try {
            if (companyList.isEmpty()) {
                System.out.println("List is Empty, can't print yet...");
                return;
            }

            for (Company company : companyList) {
                //TODO print out company details.
                printCompany(company, printStream);
                System.out.println("\n");
            }
        } catch (NullPointerException e){
            System.out.println("List not created, can't print yet...");
        }

    }


    public static void createCVSFile(List<CompanyDetail> companyDetailList) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File("companyTest.csv"));
        StringBuilder sb = new StringBuilder();

        sb.append("name");
            sb.append(',');
        sb.append("number");
            sb.append(',');
        sb.append("good standing");
            sb.append(',');
        sb.append("status");
            sb.append(',');
        sb.append("corp type");
            sb.append(',');
        sb.append("incorp date");
            sb.append(',');
        sb.append("category");
            sb.append(',');
        sb.append("last ann returns");
            sb.append(',');
        sb.append("business type");
            sb.append(',');
        sb.append("incorp juristiction");
            sb.append(',');
        sb.append("filing type");
            sb.append(',');
        sb.append("min/max directors");
            sb.append(',');
        sb.append("additional");
            sb.append('\n');

        for(CompanyDetail companyDetail : companyDetailList){
            sb.append(companyDetail.getCompany().getName());
                sb.append(',');
            sb.append(companyDetail.getCompany().getNumber());
                sb.append(',');
            sb.append(companyDetail.isInGoodStanding());
                sb.append(',');
            sb.append(companyDetail.getCompany().getStatus());
                sb.append(',');
            sb.append(companyDetail.getCompany().getCorporationType());
                sb.append(',');
            sb.append(companyDetail.getCompany().getIncorporationDate());
                sb.append(',');
            sb.append(companyDetail.getCategory());
                sb.append(',');
            sb.append(companyDetail.getLastAnnualReturn());
                sb.append(',');
            sb.append(companyDetail.getBusinessType());
                sb.append(',');
            sb.append(companyDetail.getCompany().getIncorporationDate());
                sb.append(',');
            sb.append(companyDetail.getFilingType());
                sb.append(',');
            sb.append(companyDetail.getMinMaxDirectors());
                sb.append(',');
            sb.append(companyDetail.getAdditionalInfo());
                sb.append('\n');
        }

        pw.write(sb.toString());
        pw.close();
    }

}
