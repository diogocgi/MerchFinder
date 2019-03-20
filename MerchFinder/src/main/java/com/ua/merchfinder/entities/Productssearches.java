/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ua.merchfinder.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author diogo
 */
@Entity
@Table(name = "Products_searches")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Productssearches.findAll", query = "SELECT p FROM Productssearches p")
    , @NamedQuery(name = "Productssearches.findById", query = "SELECT p FROM Productssearches p WHERE p.id = :id")
    , @NamedQuery(name = "Productssearches.findBySearchText", query = "SELECT p FROM Productssearches p WHERE p.searchText = :searchText")
    , @NamedQuery(name = "Productssearches.findByPname", query = "SELECT p FROM Productssearches p WHERE p.pname = :pname")
    , @NamedQuery(name = "Productssearches.findByPlink", query = "SELECT p FROM Productssearches p WHERE p.plink = :plink")
    , @NamedQuery(name = "Productssearches.findByPimageLink", query = "SELECT p FROM Productssearches p WHERE p.pimageLink = :pimageLink")
    , @NamedQuery(name = "Productssearches.findByPrice", query = "SELECT p FROM Productssearches p WHERE p.price = :price")
    , @NamedQuery(name = "Productssearches.findByCurrency", query = "SELECT p FROM Productssearches p WHERE p.currency = :currency")})
public class Productssearches implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "search_text")
    private String searchText;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "pname")
    private String pname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "plink")
    private String plink;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "pimage_link")
    private String pimageLink;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private BigDecimal price;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "currency")
    private String currency;

    public Productssearches() {
    }

    public Productssearches(Integer id) {
        this.id = id;
    }

    public Productssearches(Integer id, String searchText, String pname, String plink, String pimageLink, BigDecimal price, String currency) {
        this.id = id;
        this.searchText = searchText;
        this.pname = pname;
        this.plink = plink;
        this.pimageLink = pimageLink;
        this.price = price;
        this.currency = currency;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPlink() {
        return plink;
    }

    public void setPlink(String plink) {
        this.plink = plink;
    }

    public String getPimageLink() {
        return pimageLink;
    }

    public void setPimageLink(String pimageLink) {
        this.pimageLink = pimageLink;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Productssearches)) {
            return false;
        }
        Productssearches other = (Productssearches) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ua.merchfinder.entities.Productssearches[ id=" + id + " ]";
    }
    
}
