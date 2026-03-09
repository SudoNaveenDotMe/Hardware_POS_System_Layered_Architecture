package lk.ijse.athukorala_hardware.bo;

import lk.ijse.athukorala_hardware.bo.custom.impl.*;

public class BOFactory {
    public static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getInstance() {
        return boFactory == null ? new BOFactory() : boFactory;
    }

    public SuperBO getBO(BOFactory.BOType type) {
        switch (type) {
            case CUSTOMER:
                return new CustomerBOImpl();
            case ITEM:
                return new ItemBOImpl();
            case DRIVER:
                return new DriverBOImpl();
            case SUPPLIER:
                return new SupplierBOImpl();
            case ORDER:
                return new PlaceOrderBOImpl();
            case USER:
                return new UserBOImpl();
            default:
                return null;
        }
    }

    public enum BOType {
        CUSTOMER,
        ITEM,
        ORDER,
        DRIVER,
        SUPPLIER,
        USER
    }
}
