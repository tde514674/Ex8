/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitymanagerdemo;


import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import model.Address;
import model.Customer;

/**
 *
 * @author sarun
 */
public class EntityManagerDemo {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        createData();
        //printAllCustomer();
        printCustomerByCity("Bangkok");
    }

    public static void createData(){
        Customer customer1 = new Customer(1L, "John", "Henry", "jh@mail.com"); 
        Address address1 = new Address(1L, "123/4 Viphavadee Rd", "Bangkok", "10900", "TH"); 
        address1.setCustomerFk(customer1);
        customer1.setAddressId(address1);
        
        Customer customer2 = new Customer(2L, "Marry", "Jane", "mj@mail.com"); 
        Address address2 = new Address(2L, "123/5 Viphavadee Rd", "Bangkok", "10900", "TH"); 
        address2.setCustomerFk(customer2);
        customer2.setAddressId(address2); 
        
        Customer customer3 = new Customer(3L, "Peter", "Parker", "pp@mail.com"); 
        Address address3 = new Address(3L, "543/21 Fake Rd", "Nonthaburi", "20900", "TH"); 
        address3.setCustomerFk(customer3);
        customer3.setAddressId(address3); 
        
        Customer customer4 = new Customer(4L, "Bruce", "Wayn", "bw@mail.com"); 
        Address address4 = new Address(4L, "678/90 Unreal Rd", "Pathumthani", "30500", "TH"); 
        address4.setCustomerFk(customer4);
        customer4.setAddressId(address4); 
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EntityManagerDemoPU");
        EntityManager em = emf.createEntityManager();
        
        em.getTransaction().begin();
        try {
            em.persist(address1);
            em.persist(customer1);
            em.flush();
            em.persist(address2);
            em.persist(customer2);
            em.flush();
            em.persist(address3);
            em.persist(customer3);
            em.flush();
            em.persist(address4);
            em.persist(customer4);
            
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
    
    
    public static void printAllCustomer(){
        List<Customer> customerList = queryAllCustomer();
        List<Address> addressList = queryAllAddress();
        if(customerList.size()!=0&&addressList.size()!=0&&customerList.size()==addressList.size()){
            showAllCustomer(customerList, addressList);
        }
    }
    
    public static void printCustomerByCity(String city){
        List<Customer> customerList = queryAllCustomer();
        List<Address> addressList = findAddressByCity(city);
        if(customerList.size()!=0&&addressList.size()!=0){
            showAllCustomer(customerList, addressList);
        }
    }
    
    public static void showAllCustomer(List<Customer> customerList,List<Address> addressList ){
        int size = 0;
        if(customerList.size()>=addressList.size()) size = customerList.size();
        else size = addressList.size();
        for(int i=0;i<size;i++){
            for(int j=0;j<addressList.size();j++){
                if(customerList.get(i).getId()==addressList.get(j).getId()){
                    System.out.println("First Name: "+customerList.get(i).getFirstname());
                    System.out.println("Last Name: "+customerList.get(i).getLastname());
                    System.out.println("Email: "+customerList.get(i).getEmail());
                    System.out.println("Street: "+addressList.get(j).getStreet());
                    System.out.println("City: "+addressList.get(j).getCity());
                    System.out.println("Country: "+addressList.get(j).getCountry());
                    System.out.println("Zip Code: "+addressList.get(j).getZipcode());
                    System.out.println("------------------------------------------");
                    System.out.println("------------------------------------------");
                    break;
                }
            }
        }
           
    }
    
    public static List<Customer> queryAllCustomer() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EntityManagerDemoPU");
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("Customer.findAll");
        List<Customer> customerList = (List<Customer>) query.getResultList();
        return customerList;
    }
    
    public static List<Address> queryAllAddress() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EntityManagerDemoPU");
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("Address.findAll");
        List<Address> addressList = (List<Address>) query.getResultList();
        return addressList;
    }
    
    public static List<Address> findAddressByCity(String city) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EntityManagerDemoPU");
        EntityManager em = emf.createEntityManager();
        String sql = "SELECT * FROM ADDRESS WHERE city = ?city";
        Query query = em.createNativeQuery(sql, Address.class);
        query.setParameter("city", city);
        List<Address> addressList = (List<Address>) query.getResultList();
        return addressList;
    }
    
    public void persist(Object object) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EntityManagerDemoPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
