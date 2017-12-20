package ca.joshtaylor.CADO;
/**
 * Created by josh.taylor on 11/17/2017.
 */
public class CompanyAddress {

    private String contact;
    private String streetLine1;
    private String streetLine2;
    private String streetLine3;
    private String city;
    private String province;
    private String country;
    private String postalCode;

    public CompanyAddress(CompanyAddressBuilder builder){
        this.contact = builder.contact;
        this.streetLine1 = builder.streetLine1;
        this.streetLine2 = builder.streetLine2;
        this.streetLine3 = builder.streetLine3;
        this.city = builder.city;
        this.province = builder.province;
        this.country = builder.country;
        this.postalCode = builder.postalCode;
    }

    public String getContact() {
        return contact;
    }

    public String getStreetLine1() {
        return streetLine1;
    }

    public String getStreetLine2() {
        return streetLine2;
    }

    public String getStreetLine3() {
        return streetLine3;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public static class CompanyAddressBuilder{

        private String contact;
        private String streetLine1;
        private String streetLine2;
        private String streetLine3;
        private String city;
        private String province;
        private String country;
        private String postalCode;

        public CompanyAddressBuilder contact(String contact){
            this.contact = contact;
            return this;
        }

        public CompanyAddressBuilder streetLine1(String streetLine1){
            this.streetLine1 = streetLine1;
            return this;
        }

        public CompanyAddressBuilder streetLine2(String streetLine2){
            this.streetLine2 = streetLine2;
            return this;
        }

        public CompanyAddressBuilder streetLine3(String streetLine3){
            this.streetLine3 = streetLine3;
            return this;
        }

        public CompanyAddressBuilder city(String city){
            this.city = city;
            return this;
        }

        public CompanyAddressBuilder province(String province){
            this.province = province;
            return this;
        }

        public CompanyAddressBuilder country(String country){
            this.country = country;
            return this;
        }

        public CompanyAddressBuilder postalCode(String postalCode){
            this.postalCode = postalCode;
            return this;
        }

        public CompanyAddress build(){
            return new CompanyAddress(this);
        }

    }


}
