package version2.presentation;

import version2.persistence.InvoicePersistenceService;
import version2.persistence.InvoicePersistenceServiceImpl;
import version2.presentation.comand.InvoiceService;
import version2.presentation.comand.InvoiceServiceImpl;



public class Main {
    
    public static void main(String[] args) {
        InvoicePersistenceService invoicePersistenceService = new InvoicePersistenceServiceImpl("dataInvoice.db");
        InvoiceService invoiceService = new InvoiceServiceImpl(invoicePersistenceService);
        HotelManagementUI ui = new HotelManagementUI(invoiceService);
    }
}
