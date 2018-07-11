package org.byters.dataplaybilling.controller.data.device;

public interface IRepositoryClientBilling {

    void request();

    void setListener(IRepositoryClientBillingCallback listener);
}
