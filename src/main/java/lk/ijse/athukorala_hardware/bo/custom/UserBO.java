package lk.ijse.athukorala_hardware.bo.custom;

import lk.ijse.athukorala_hardware.bo.SuperBO;
import lk.ijse.athukorala_hardware.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

public interface UserBO extends SuperBO {
    boolean saveUser(UserDTO userDTO) throws SQLException, ClassNotFoundException;
    boolean updateUser(UserDTO userDTO) throws SQLException, ClassNotFoundException;
    boolean deleteUser(String id) throws SQLException, ClassNotFoundException;
    UserDTO searchUserById(String id) throws SQLException, ClassNotFoundException;
    UserDTO searchUserByEmail(String email) throws SQLException, ClassNotFoundException;
    List<UserDTO> getAllUsers() throws SQLException, ClassNotFoundException;
}
