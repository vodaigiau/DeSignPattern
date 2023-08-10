package version2.domain.model;

import java.util.List;
//observer
public interface InvoiceObserver {
    void updateInvoices(List<Invoice> updatedInvoices);
}
