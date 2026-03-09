package lk.ijse.athukorala_hardware.dao;

import lk.ijse.athukorala_hardware.dao.custom.impl.CustomerDAOImpl;
import lk.ijse.athukorala_hardware.dao.custom.impl.DriverDAOImpl;
import lk.ijse.athukorala_hardware.dao.custom.impl.ItemDAOImpl;
import lk.ijse.athukorala_hardware.dao.custom.impl.SupplierDAOImpl;

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
        DRIVER,
        SUPPLIER,
        ORDER,
        ORDER_DETAIL,
        QUERY
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
            default:
                return null;
        }
    }
}
