package version2.presentation;

import version2.domain.InvoiceService;
import version2.domain.InvoiceServiceImpl;
import version2.persistence.InvoicePersistenceService;
import version2.persistence.InvoicePersistenceServiceImpl;



public class Main {
    
    public static void main(String[] args) {
        InvoicePersistenceService invoicePersistenceService = new InvoicePersistenceServiceImpl("dataInvoice.db");
        InvoiceService invoiceService = new InvoiceServiceImpl(invoicePersistenceService);
        HotelManagementUI ui = new HotelManagementUI(invoiceService);
    }
}
