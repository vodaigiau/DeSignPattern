package version2.persistence;

import java.util.List;

import version2.domain.model.Invoice;

public interface InvoicePersistenceService {
    void saveInvoice(Invoice invoice);
    void deleteInvoice(int id);
    void updateInvoice(Invoice invoice);
    Invoice getInvoiceById(int id);
    List<Invoice> getAllInvoices();
}
