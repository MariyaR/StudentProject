package edu.javacourse.zags;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by antonsaburov on 22.02.17.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Person implements Serializable
{
    @XmlElement(name = "surName", required = true)
    private String surName;
    @XmlElement(name = "givenName", required = true)
    private String givenName;
    @XmlElement(name = "patronymic", required = true)
    private String patronymic;
    @XmlElement(name = "dateOfBirth", required = false)
    @XmlJavaTypeAdapter(StudentDateAdapter.class)
    private Date dateOfBirth;

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
