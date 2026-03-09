package lk.ijse.athukorala_hardware.model;

import lk.ijse.athukorala_hardware.db.DBConnection;
import lk.ijse.athukorala_hardware.dto.UserDTO;
import lk.ijse.athukorala_hardware.dao.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class UserModel {
    public UserDTO searchUser(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email LIKE ?";
        ResultSet resultSet = CrudUtil.execute(sql, email);

        if (resultSet.next()){
            return new UserDTO(
                    resultSet.getInt("u_id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role")
            );
        }
        return null;
    }

    public UserDTO searchUserById(String id) throws SQLException {
        String sql = "SELECT * FROM users WHERE u_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, id);

        if (resultSet.next()){
            return new UserDTO(
                    resultSet.getInt("u_id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role")
            );
        }
        return null;
    }

    public boolean saveUser(UserDTO userDTO) throws SQLException {
        String sql = "INSERT INTO users (name, email, password, role) VALUES(?, ?, ?, ?)";
        return CrudUtil.execute(sql,
                userDTO.getName(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getRole()
        );
    }

    public boolean updateUser(UserDTO userDTO) throws SQLException {
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, role = ? WHERE u_id = ?";
        return CrudUtil.execute(sql,
                userDTO.getName(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getRole(),
                userDTO.getUserId()
        );
    }

    public boolean deleteUser(String id) throws SQLException {
        String sql = "DELETE FROM users WHERE u_id = ?";
        return CrudUtil.execute(sql, id);
    }

    public List<UserDTO> getAllUsers() throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM users";
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<UserDTO> userList = new ArrayList<>();

        while (resultSet.next()) {
            userList.add(new UserDTO(
                    resultSet.getInt("u_id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role")
            ));
        }
        return userList;
    }
}
