package dao.impl;

import dao.SellerDao;
import db.DB;
import db.DbException;
import entities.Department;
import entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection connection;

    public SellerDaoJDBC(Connection connection){
        this.connection = connection;
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        Department department = new Department();
        department.setId(resultSet.getInt("DepartmentId"));
        department.setName(resultSet.getString("depName"));
        return department;
    }

    private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(resultSet.getInt("Id"));
        seller.setName(resultSet.getString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBirthDate((resultSet.getDate("BirthDate")));
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setDepartment(department);
        return seller;
    }

    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement("SELECT seller.*, department.Name as depName " +
                    "FROM seller INNER JOIN department ON seller.departmentId " +
                    "WHERE seller.id=?");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                Department department = instantiateDepartment(resultSet);
                return instantiateSeller(resultSet, department);
            }
            return null;
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(statement);
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Seller> findAll() {
        return List.of();
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement("SELECT seller.*, " +
                    "department.Name as depName FROM seller INNER JOIN " +
                    "department ON seller.DepartmentId = department.id " +
                    "WHERE DepartmentId = ? ORDER BY Name");

            statement.setInt(1, department.getId());

            resultSet = statement.executeQuery();

            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();
            while(resultSet.next()){
                Department dept = map.get(resultSet.getInt("DepartmentId"));
                if(dept==null){
                    dept = instantiateDepartment(resultSet);
                    map.put(resultSet.getInt("DepartmentId"), dept);
                }
                Seller seller = instantiateSeller(resultSet, dept);
                sellers.add(seller);
            }
            return sellers;
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(statement);
            DB.closeResultSet(resultSet);
        }
    }
}
