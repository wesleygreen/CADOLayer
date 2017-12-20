package ca.joshtaylor.CADO;
/**
 * A class to represent the line items data on companies returned after a search result.
 *
 * Created by josh.taylor on 11/20/2017.
 */
public class Company {

    protected String name;
    protected String number;
    protected String status;
    protected String corporationType;
    protected String incorporationDate;

    protected Company(CompanyBuilder builder){
        this.name = builder.name;
        this.number = builder.number;
        this.status = builder.status;
        this.corporationType = builder.corporationType;
        this.incorporationDate = builder.incorporationDate;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getStatus() {
        return status;
    }

    public String getCorporationType() {
        return corporationType;
    }

    public String getIncorporationDate() {
        return incorporationDate;
    }

    public static class CompanyBuilder {

        protected String name;
        protected String number;
        protected String status;
        protected String corporationType;
        protected String incorporationDate;

        public CompanyBuilder(String name, String number){
            this.name = name;
            this.number = number;
        }

        public CompanyBuilder status(String status){
            this.status = status;
            return this;
        }

        public CompanyBuilder corporationType(String corporationType){
            this.corporationType = corporationType;
            return this;
        }

        public CompanyBuilder incorporationDate(String incorporationDate){
            this.incorporationDate = incorporationDate;
            return this;
        }

        public Company build(){
            return new Company(this);
        }

    }

}
