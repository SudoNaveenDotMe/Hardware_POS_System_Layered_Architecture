package lk.ijse.athukorala_hardware.dao;

import lk.ijse.athukorala_hardware.dao.custom.impl.*;

public class DAOFactory {
    public static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        return daoFactory == null ? new DAOFactory() : daoFactory;
    }

    public SuperDAO getDAO(DAOType type) {
        switch (type) {
            case CUSTOMER:
                return new CustomerDAOImpl();
            case ITEM:
                return new ItemDAOImpl();
            case DRIVER:
                return new DriverDAOImpl();
            case SUPPLIER:
                return new SupplierDAOImpl();
            case ORDER:
                return new OrderDAOImpl();
            case ORDER_DETAIL:
                return new OrderDetailsDAOImpl();
            case QUERY:
                return new QueryDAOImpl();
            case USER:
                return new UserDAOImpl();
            default:
                return null;
        }
    }

    public enum DAOType {
        CUSTOMER,
        ITEM,
        DRIVER,
        SUPPLIER,
        ORDER,
        ORDER_DETAIL,
        QUERY,
        USER
    }
}
