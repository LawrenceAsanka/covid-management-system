package util;

public class QuarantineCenterTM {
    private String quarantine_id;
    private String quarantine_name;
    private String quarantine_city;
    private String quarantine_district;
    private String quarantine_head_name;
    private String quarantine_head_contact;
    private String quarantine_contact_01;
    private String quarantine_contact_02;
    private String quarantine_capacity;

    public QuarantineCenterTM() {
    }

    public QuarantineCenterTM(String quarantine_id, String quarantine_name, String quarantine_city, String quarantine_district, String quarantine_head_name, String quarantine_head_contact, String quarantine_contact_01, String quarantine_contact_02, String quarantine_capacity) {
        this.quarantine_id = quarantine_id;
        this.quarantine_name = quarantine_name;
        this.quarantine_city = quarantine_city;
        this.quarantine_district = quarantine_district;
        this.quarantine_head_name = quarantine_head_name;
        this.quarantine_head_contact = quarantine_head_contact;
        this.quarantine_contact_01 = quarantine_contact_01;
        this.quarantine_contact_02 = quarantine_contact_02;
        this.quarantine_capacity = quarantine_capacity;
    }

    public String getQuarantine_id() {
        return quarantine_id;
    }

    public void setQuarantine_id(String quarantine_id) {
        this.quarantine_id = quarantine_id;
    }

    public String getQuarantine_name() {
        return quarantine_name;
    }

    public void setQuarantine_name(String quarantine_name) {
        this.quarantine_name = quarantine_name;
    }

    public String getQuarantine_city() {
        return quarantine_city;
    }

    public void setQuarantine_city(String quarantine_city) {
        this.quarantine_city = quarantine_city;
    }

    public String getQuarantine_district() {
        return quarantine_district;
    }

    public void setQuarantine_district(String quarantine_district) {
        this.quarantine_district = quarantine_district;
    }

    public String getQuarantine_head_name() {
        return quarantine_head_name;
    }

    public void setQuarantine_head_name(String quarantine_head_name) {
        this.quarantine_head_name = quarantine_head_name;
    }

    public String getQuarantine_head_contact() {
        return quarantine_head_contact;
    }

    public void setQuarantine_head_contact(String quarantine_head_contact) {
        this.quarantine_head_contact = quarantine_head_contact;
    }

    public String getQuarantine_contact_01() {
        return quarantine_contact_01;
    }

    public void setQuarantine_contact_01(String quarantine_contact_01) {
        this.quarantine_contact_01 = quarantine_contact_01;
    }

    public String getQuarantine_contact_02() {
        return quarantine_contact_02;
    }

    public void setQuarantine_contact_02(String quarantine_contact_02) {
        this.quarantine_contact_02 = quarantine_contact_02;
    }

    public String getQuarantine_capacity() {
        return quarantine_capacity;
    }

    public void setQuarantine_capacity(String quarantine_capacity) {
        this.quarantine_capacity = quarantine_capacity;
    }

    @Override
    public String toString() {
        return quarantine_name;
    }
}
