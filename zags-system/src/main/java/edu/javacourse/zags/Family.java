package edu.javacourse.zags;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by maria on 11/04/17.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "family")
public class Family {

    @XmlElement(name = "husband", required = true)
    private PersonAdult husband;
    @XmlElement(name = "wife", required = true)
    private PersonAdult wife;
    @XmlElement(name = "child", required = true)
    private PersonChild child;
    @XmlElement(name = "type", required = true)
    private String type;

    public void setHusband (PersonAdult h) {this.husband=h;}
    public void setWife (PersonAdult w) {this.wife=w;}
    public void setChild (PersonChild ch) {this.child=ch;}

    public PersonAdult getHusband () {return husband;}
    public PersonAdult getWife () {return wife;}
    public PersonChild getChild () {return child;}
    public String getType () {return type;}
}
