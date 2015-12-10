package com.volkswagen.tel.billing.billcall.jpa.domain;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TBS_TELEPHONE_BILL_SUM")
public class TelephoneBillSumEntity {

    @Id
    @Column(name = "sum_id")
    @SequenceGenerator(name = "TELEPHONE_BILL_SUM_GENERATOR", sequenceName = "SEQ_TBS_TELEPHONE_BILL_SUM", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TELEPHONE_BILL_SUM_GENERATOR")
    private Long billSumId;

    @Column(name = "telephone_number", length = 20)
    private String telephoneNumber;

    @Column(name = "year")
    private int year;

    @Column(name = "month")
    private int month;

    @Column(name = "monthPkg")
    private float monthPkg;

    @Column(name = "dataBoPkg")
    private float dataBoPkg;

    @Column(name = "smsBoPkg")
    private float smsBoPkg;

    @Column(name = "roamingBoPkg")
    private float roamingBoPkg;

    @Column(name = "vendor_name", length = 100)
    private String vendorName;

    @Column(name = "status", length = 20)
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update_time")
    private Date lastUpdateTime;

    public Long getBillSumId() {

        return billSumId;
    }

    public void setBillSumId(Long billSumId) {

        this.billSumId = billSumId;
    }

    public String getTelephoneNumber() {

        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {

        this.telephoneNumber = telephoneNumber;
    }

    public int getYear() {

        return year;
    }

    public void setYear(int year) {

        this.year = year;
    }

    public int getMonth() {

        return month;
    }

    public void setMonth(int month) {

        this.month = month;
    }

    public float getMonthPkg() {

        return monthPkg;
    }

    public void setMonthPkg(float monthPkg) {

        this.monthPkg = monthPkg;
    }

    public float getDataBoPkg() {

        return dataBoPkg;
    }

    public void setDataBoPkg(float dataBoPkg) {

        this.dataBoPkg = dataBoPkg;
    }

    public float getSmsBoPkg() {

        return smsBoPkg;
    }

    public void setSmsBoPkg(float smsBoPkg) {

        this.smsBoPkg = smsBoPkg;
    }

    public float getRoamingBoPkg() {

        return roamingBoPkg;
    }

    public void setRoamingBoPkg(float roamingBoPkg) {

        this.roamingBoPkg = roamingBoPkg;
    }

    public String getVendorName() {

        return vendorName;
    }

    public void setVendorName(String vendorName) {

        this.vendorName = vendorName;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public Date getLastUpdateTime() {

        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {

        this.lastUpdateTime = lastUpdateTime;
    }


    public static TelephoneBillSumEntity composeBillSumEntity(List<String> columns, int year, int month, String venderName) {

        TelephoneBillSumEntity entity = new TelephoneBillSumEntity();
        String spcialChar = Character.toString((char) 160);
        entity.setTelephoneNumber(columns.get(0).replaceAll(spcialChar, ""));
        entity.setMonthPkg(Float.parseFloat(columns.get(2).replaceAll(spcialChar, "")));
        entity.setRoamingBoPkg(Float.parseFloat(columns.get(3).replaceAll(spcialChar, "")));
        entity.setDataBoPkg(Float.parseFloat(columns.get(4).replaceAll(spcialChar, "")));
        entity.setSmsBoPkg(Float.parseFloat(columns.get(5).replaceAll(spcialChar, "")));
        entity.setYear(year);
        entity.setMonth(month);
        entity.setVendorName(venderName);
        entity.setStatus("ACTIVE");
        entity.setLastUpdateTime(Calendar.getInstance().getTime());
        return entity;
    }

}
