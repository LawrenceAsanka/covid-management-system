package util;

public class HospitalTM {
    private String hospital_id;
    private String hospital_name;
    private String hospital_city;
    private String hospital_district;
    private String hospital_capacity;
    private String hospital_director_name;
    private String hospital_director_contact;
    private String hospital_contact_no1;
    private String hospital_contact_no2;
    private String hospital_fax_number;
    private String hospital_email;

    public HospitalTM() {
    }

    public HospitalTM(String hospital_id, String hospital_name, String hospital_city, String hospital_district, String hospital_capacity, String hospital_director_name, String hospital_director_contact, String hospital_contact_no1, String hospital_contact_no2, String hospital_fax_number, String hospital_email) {
        this.hospital_id = hospital_id;
        this.hospital_name = hospital_name;
        this.hospital_city = hospital_city;
        this.hospital_district = hospital_district;
        this.hospital_capacity = hospital_capacity;
        this.hospital_director_name = hospital_director_name;
        this.hospital_director_contact = hospital_director_contact;
        this.hospital_contact_no1 = hospital_contact_no1;
        this.hospital_contact_no2 = hospital_contact_no2;
        this.hospital_fax_number = hospital_fax_number;
        this.hospital_email = hospital_email;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getHospital_city() {
        return hospital_city;
    }

    public void setHospital_city(String hospital_city) {
        this.hospital_city = hospital_city;
    }

    public String getHospital_district() {
        return hospital_district;
    }

    public void setHospital_district(String hospital_district) {
        this.hospital_district = hospital_district;
    }

    public String getHospital_capacity() {
        return hospital_capacity;
    }

    public void setHospital_capacity(String hospital_capacity) {
        this.hospital_capacity = hospital_capacity;
    }

    public String getHospital_director_name() {
        return hospital_director_name;
    }

    public void setHospital_director_name(String hospital_director_name) {
        this.hospital_director_name = hospital_director_name;
    }

    public String getHospital_director_contact() {
        return hospital_director_contact;
    }

    public void setHospital_director_contact(String hospital_director_contact) {
        this.hospital_director_contact = hospital_director_contact;
    }

    public String getHospital_contact_no1() {
        return hospital_contact_no1;
    }

    public void setHospital_contact_no1(String hospital_contact_no1) {
        this.hospital_contact_no1 = hospital_contact_no1;
    }

    public String getHospital_contact_no2() {
        return hospital_contact_no2;
    }

    public void setHospital_contact_no2(String hospital_contact_no2) {
        this.hospital_contact_no2 = hospital_contact_no2;
    }

    public String getHospital_fax_number() {
        return hospital_fax_number;
    }

    public void setHospital_fax_number(String hospital_fax_number) {
        this.hospital_fax_number = hospital_fax_number;
    }

    public String getHospital_email() {
        return hospital_email;
    }

    public void setHospital_email(String hospital_email) {
        this.hospital_email = hospital_email;
    }

    @Override
    public String toString() {
        return hospital_name;
    }
}
