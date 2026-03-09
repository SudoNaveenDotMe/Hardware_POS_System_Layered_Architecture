package lk.ijse.athukorala_hardware.bo;

import lk.ijse.athukorala_hardware.bo.custom.impl.CustomerBOImpl;

public class BOFactory {
    public static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getInstance() {
        return boFactory == null ? new BOFactory() : boFactory;
    }

    public enum BOType {
        CUSTOMER,
        ITEM,
        ORDER,
        DRIVER,
        SUPPLIER,
        USER
    }

    public SuperBO getBO(BOFactory.BOType type) {
        switch (type) {
            case CUSTOMER:
                return new CustomerBOImpl();
            default:
                return null;
        }
    }
}
