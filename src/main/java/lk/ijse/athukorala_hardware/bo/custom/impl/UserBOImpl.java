package lk.ijse.athukorala_hardware.bo.custom.impl;

import lk.ijse.athukorala_hardware.bo.custom.UserBO;
import lk.ijse.athukorala_hardware.dao.DAOFactory;
import lk.ijse.athukorala_hardware.dao.custom.SupplierDAO;
import lk.ijse.athukorala_hardware.dao.custom.UserDAO;
import lk.ijse.athukorala_hardware.dto.UserDTO;
import lk.ijse.athukorala_hardware.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserBOImpl implements UserBO {
    UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.USER);


    @Override
    public boolean saveUser(UserDTO dto) throws SQLException, ClassNotFoundException {
        return userDAO.save(new User(dto.getName(), dto.getEmail(), dto.getPassword(), dto.getRole()));
    }

    @Override
    public boolean updateUser(UserDTO dto) throws SQLException, ClassNotFoundException {
        return userDAO.update(new User(dto.getUserId(), dto.getName(), dto.getEmail(), dto.getPassword(), dto.getRole()));
    }

    @Override
    public boolean deleteUser(String id) throws SQLException, ClassNotFoundException {
        return userDAO.delete(id);
    }

    @Override
    public UserDTO searchUserById(String id) throws SQLException, ClassNotFoundException {
        User user = userDAO.search(id);
        if (user != null) {
            return new UserDTO(user.getUserId(), user.getName(), user.getEmail(), user.getPassword(), user.getRole());
        }
        return null;
    }

    @Override
    public UserDTO searchUserByEmail(String email) throws SQLException, ClassNotFoundException {
        User user = userDAO.searchByEmail(email);
        if (user != null) {
            return new UserDTO(user.getUserId(), user.getName(), user.getEmail(), user.getPassword(), user.getRole());
        }
        return null;
    }

    @Override
    public List<UserDTO> getAllUsers() throws SQLException, ClassNotFoundException {
        List<User> users = userDAO.getAll();
        List<UserDTO> userDTOs = new ArrayList<>();

        for (User user : users) {
            userDTOs.add(new UserDTO(user.getUserId(), user.getName(), user.getEmail(), user.getPassword(), user.getRole()));
        }
        return userDTOs;
    }
}
