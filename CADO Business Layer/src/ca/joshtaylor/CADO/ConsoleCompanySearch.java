package ca.joshtaylor.CADO;
import java.util.Scanner;

/**
 * Created by josh.taylor on 11/30/2017.
 */
public class ConsoleCompanySearch {

    public static void main(String[] args){
        System.out.println("CADO CompanyDetail Search...");
        Scanner input = new Scanner(System.in);

        String choice = "";
        do {
            System.out.println("\n\nSearch by [K]eyword or [B]usiness number or [D]etailed Business Info or [Q]uit:");
            choice = input.next();

            //Keyword Searching
            if(choice.equalsIgnoreCase("K")){
                System.out.println("Keyword 1: ");
                String keyword1 = input.next();
//                System.out.println("Keyword 2: ");
//                String keyword2 = input.next();
                //TODO Handle second keyword
                CompanyWriter.printCompanyList(CADOCompanyBuilder.getCompanyList(keyword1,""),System.out);

            //Business Number Searching
            } else if(choice.equalsIgnoreCase("B")) {
                System.out.println("Enter Business Number:");
                String businessNumber = input.next();
                CompanyWriter.printCompanyList(CADOCompanyBuilder.getCompanyList(businessNumber), System.out);


            //Not valid input
            } else if(choice.equalsIgnoreCase("D")) {
                System.out.println("Enter Business Number:");
                String businessNumber = input.next();
                try{
                    CompanyWriter.printCompanyDetail(CADOCompanyBuilder.getCompanyDetail(businessNumber), System.out);

                } catch (CADOSearchError e){
                    System.out.print("CADO Search error message: " + e.getMessage());
                }


                //Not valid input
            } else {
                System.out.println("Not a valid choice\n");
            }

        } while(!choice.equalsIgnoreCase("q"));

    }


}
