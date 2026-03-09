package lk.ijse.athukorala_hardware.dao;

import lk.ijse.athukorala_hardware.dao.custom.impl.CustomerDAOImpl;

public class DAOFactory {
    public static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        return daoFactory == null ? new DAOFactory() : daoFactory;
    }

    public enum DAOType {
        CUSTOMER,
        ITEM,
        ORDER,
        ORDER_DETAIL,
        QUERY
    }

    public SuperDAO getDAO(DAOType type) {
        switch (type) {
            case CUSTOMER:
                return new CustomerDAOImpl();
            default:
                return null;
        }
    }
}
