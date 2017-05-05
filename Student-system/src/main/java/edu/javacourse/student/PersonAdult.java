package edu.javacourse.student;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement(name = "Person")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonAdult extends Person implements Serializable
{
    @XmlElement(name = "seria", required = true)
    private String passportSeria;
    @XmlElement(name = "number", required = true)
    private String passportNumber;
    @XmlElement(name = "dateOfIssue", required = false)
    @XmlJavaTypeAdapter(StudentDateAdapter.class)
    private Date passportDateIssue;
    @XmlElement(name = "dateExpire", required = false)
    @XmlJavaTypeAdapter(StudentDateAdapter.class)
    private Date passportDateExpire;

    public String getPassportSeria() {
        return passportSeria;
    }

    public void setPassportSeria(String passportSeria) {
        this.passportSeria = passportSeria;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public Date getPassportDateIssue() {
        return passportDateIssue;
    }

    public void setPassportDateIssue(Date passportDateIssue) {
        this.passportDateIssue = passportDateIssue;
    }

    public Date getPassportDateExpire() {
        return passportDateExpire;
    }

    public void setPassportDateExpire(Date passportDateExpire) {
        this.passportDateExpire = passportDateExpire;
    }


}
