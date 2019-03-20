/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ua.merchfinder.session;

import com.ua.merchfinder.entities.Productssearches;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author diogo
 */
@Stateless
public class ProductssearchesFacade extends AbstractFacade<Productssearches> {

    @PersistenceContext(unitName = "com.ua_merchfinder_war_1.0PU")
    private EntityManager em;

    /**
     * Returns the entity manager instance
     * @return 
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Constructor
     */
    public ProductssearchesFacade() {
        super(Productssearches.class);
    }
    
    /**
     * Store one search result / product in the database
     * @param search 
     */
    public void Save(Productssearches search)
    {
        try {
            em.persist(search);
        }
        catch (Exception e){
            throw new EJBException(e.getMessage());
        }
    }
    
    /**
     * Get the list of products according to the searched word 
     * @param searchText
     * @return 
     */
    public List<Object[]> getSearchResultsFromDB(String searchText)
    {
        TypedQuery<Object[]> query = null;
        
        try {
            
            query = em.createQuery(
                "SELECT ps.pname,ps.plink,ps.price,ps.pimageLink,ps.currency FROM com.ua.merchfinder.entities.Productssearches ps WHERE ps.searchText='" + searchText + "'", 
                Object[].class);
            
            /* In JPQL the column name is not used, but the correspondent attribute name
             * For example, pimage_link correspondes to the pimagelink attribute defined in Productssearches.java.
             * 
             * JPQL requires the feinition of an alis for the entity table 
             */

        }
        catch (Exception e){
            throw new EJBException(e.getMessage());
        }
        
        return query.getResultList();
    }
 
    /**
     * Produces a list with all different product searches in the database
     * @return 
     */
    public List<String> getAllDifferentSearchNames()
    {
        TypedQuery<String> query = null;
        
        try {
            
            query = em.createQuery(
                "SELECT DISTINCT ps.searchText FROM com.ua.merchfinder.entities.Productssearches ps", 
                String.class);
        }
        catch (Exception e){
            throw new EJBException(e.getMessage());
        }
        
        return query.getResultList();
    }
    
}
