package ca.joshtaylor.CADO;

/**
 * CompanyDetail object for the CADO CompanyDetail Database
 *
 * Created by josh.taylor on 11/16/2017.
 */
public class CompanyDetail {

    //Private Members
    private Company company;
    private boolean inGoodStanding;
    private String category;
    private String lastAnnualReturn;
    private String businessType;
    private String incorporationJurisdiction;
    private String filingType;
    private String minMaxDirectors;
    private String additionalInfo;
    private CompanyAddress registeredAddress;
    private CompanyAddress mailingAddress;

    /**
     * Build a CompanyDetail with a builder
     * @param builder
     */
    private CompanyDetail(CompanyDetailBuilder builder){
        this.company = builder.company;
        this.inGoodStanding = builder.inGoodStanding;
        this.category = builder.category;
        this.lastAnnualReturn = builder.lastAnnualReturn;
        this.businessType = builder.businessType;
        this.incorporationJurisdiction = builder.incorporationJurisdiction;
        this.filingType = builder.filingType;
        this.minMaxDirectors = builder.minMaxDirectors;
        this.additionalInfo = builder.additionalInfo;
        this.registeredAddress = builder.registeredAddress;
        this.mailingAddress = builder.mailingAddress;
    }

    public Company getCompany() { return company; }

    public boolean isInGoodStanding() {
        return inGoodStanding;
    }

    public String getCategory() {
        return category;
    }

    public String getLastAnnualReturn() {
        return lastAnnualReturn;
    }

    public String getBusinessType() {
        return businessType;
    }

    public String getIncorporationJurisdiction() {
        return incorporationJurisdiction;
    }

    public String getFilingType() {
        return filingType;
    }

    public String getMinMaxDirectors() {
        return minMaxDirectors;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public CompanyAddress getRegisteredAddress() {
        return registeredAddress;
    }

    public CompanyAddress getMailingAddress() {
        return mailingAddress;
    }

    //TODO Consider extending this from Short Form CompanyDetail Builder
    /**
     * Builder class for CompanyDetail
     */
    public static class CompanyDetailBuilder {

        //Private Members
//        private final String name;
//        private final String number;
//        private String status;                  //TODO Enum these
//        private String corporationType;         //TODO Enum these
//        private String incorporationDate;       //TODO build up a date object
        private Company company;
        private boolean inGoodStanding;
        private String category;
        private String lastAnnualReturn;
        private String businessType;
        private String incorporationJurisdiction;
        private String filingType;
        private String minMaxDirectors;
        private String additionalInfo;
        private CompanyAddress registeredAddress;
        private CompanyAddress mailingAddress;


        public CompanyDetailBuilder(Company company){
            this.company = company;
        }

        public CompanyDetailBuilder inGoodStanding(boolean inGoodStanding){
            this.inGoodStanding = inGoodStanding;
            return this;
        }

        public CompanyDetailBuilder category(String category){
            this.category = category;
            return this;
        }

        public CompanyDetailBuilder lastAnnualReturn(String lastAnnualReturn){
            this.lastAnnualReturn = lastAnnualReturn;
            return this;
        }

        public CompanyDetailBuilder businessType(String businessType){
            this.businessType = businessType;
            return this;
        }

        public CompanyDetailBuilder incorporationJurisdiction(String incorporationJurisdiction){
            this.incorporationJurisdiction = incorporationJurisdiction;
            return this;
        }
        public CompanyDetailBuilder filingType(String filingType){
            this.filingType = filingType;
            return this;
        }

        public CompanyDetailBuilder minMaxDirectors(String minMaxDirectors){
            this.minMaxDirectors = minMaxDirectors;
            return this;
        }

        public CompanyDetailBuilder additionalInfo(String additionalInfo){
            this.additionalInfo = additionalInfo;
            return this;
        }

        public CompanyDetailBuilder registeredAddress(CompanyAddress registeredAddress){
            this.registeredAddress = registeredAddress;
            return this;
        }

        public CompanyDetailBuilder mailingAddress(CompanyAddress mailingAddress){
            this.mailingAddress = mailingAddress;
            return this;
        }

        public CompanyDetail build(){
            return new CompanyDetail(this);
        }

    }



}
