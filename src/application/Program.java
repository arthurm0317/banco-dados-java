package application;

import dao.DaoFactory;
import dao.SellerDao;
import entities.Department;
import entities.Seller;

import java.util.List;

public class Program {
    public static void main(String[] args) {
        SellerDao sellerDao = DaoFactory.createSellerDao();
        System.out.println("------TESTE 1------");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);



        System.out.println("\n------TESTE 2------");
        Department department = new Department(2, null);
        List<Seller> sellers = sellerDao.findByDepartment(department);
        sellers.forEach(System.out::println);
    }
}
