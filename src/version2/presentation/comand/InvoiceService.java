package version2.presentation.comand;

import java.util.List;

import version2.domain.model.Invoice;
import version2.domain.model.InvoiceObserver;

public interface InvoiceService {
    void addInvoice(Invoice invoice);
    void removeInvoice(int id);
    void editInvoice(Invoice invoice);
    Invoice findInvoice(int id);
    List<Invoice> getAllInvoices();
    void addObserver(InvoiceObserver observer); // Add this method
    void removeObserver(InvoiceObserver observer); // Add this method
}
