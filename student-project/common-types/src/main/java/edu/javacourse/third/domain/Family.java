package edu.javacourse.third.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by maria on 11/04/17.
 */
//@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "family")
public class Family {

    @XmlElement(name = "husband", required = true)
    private PersonAdult husband;
    @XmlElement(name = "wife", required = true)
    private PersonAdult wife;
    @XmlElement(name = "child", required = true)
    private PersonChild child;
    @XmlElement(name = "type", required = true)
    private String s="family";

    public void setParameters (PersonAdult h, PersonAdult w, PersonChild ch) {
        this.husband=h;
        this.wife=w;
        this.child=ch;
    }

    public PersonAdult getHusband () {return husband;}
    public PersonAdult getWife () {return wife;}
    public PersonChild getChild () {return child;}
}
