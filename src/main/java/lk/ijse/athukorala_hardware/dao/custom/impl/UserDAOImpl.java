package lk.ijse.athukorala_hardware.dao.custom.impl;

import lk.ijse.athukorala_hardware.dao.custom.UserDAO;
import lk.ijse.athukorala_hardware.dao.util.CrudUtil;
import lk.ijse.athukorala_hardware.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    @Override
    public User searchByEmail(String email) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM users WHERE email LIKE ?", email);

        if (resultSet.next()) {
            return new User(
                    resultSet.getInt("u_id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role")
            );
        }
        return null;
    }

    @Override
    public List<User> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM users");
        List<User> userList = new ArrayList<>();

        while (resultSet.next()) {
            userList.add(new User(
                    resultSet.getInt("u_id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role")
            ));
        }
        return userList;
    }

    @Override
    public boolean save(User entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO users (name, email, password, role) VALUES(?, ?, ?, ?)",
                entity.getName(), entity.getEmail(), entity.getPassword(), entity.getRole()
        );
    }

    @Override
    public boolean update(User entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE users SET name = ?, email = ?, password = ?, role = ? WHERE u_id = ?",
                entity.getName(), entity.getEmail(), entity.getPassword(), entity.getRole(), entity.getUserId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM users WHERE u_id = ?", id);    }

    @Override
    public User search(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM users WHERE u_id = ?", id);

        if (resultSet.next()) {
            return new User(
                    resultSet.getInt("u_id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role")
            );
        }
        return null;
    }
}
