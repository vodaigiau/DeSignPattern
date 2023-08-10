package version2.presentation.comand;

import java.util.ArrayList;
import java.util.List;

import version2.domain.model.Invoice;
import version2.domain.model.InvoiceObserver;
import version2.persistence.InvoicePersistenceService;

public class InvoiceServiceImpl implements InvoiceService {
    private final InvoicePersistenceService persistenceService;
    //observer
    private List<InvoiceObserver> observers = new ArrayList<>();
    public InvoiceServiceImpl(InvoicePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void addInvoice(Invoice invoice) {
        persistenceService.saveInvoice(invoice);
        notifyObservers(getAllInvoices());
    }

    @Override
    public void removeInvoice(int id) {
        persistenceService.deleteInvoice(id);
        notifyObservers(getAllInvoices());
    }

    @Override
    public void editInvoice(Invoice invoice) {
        persistenceService.updateInvoice(invoice);
        notifyObservers(getAllInvoices());
    }

    @Override
    public Invoice findInvoice(int id) {
        return persistenceService.getInvoiceById(id);
    }
  
    @Override
    public List<Invoice> getAllInvoices() {
        return persistenceService.getAllInvoices();
    }
    //obsever
    public void addObserver(InvoiceObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(InvoiceObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(List<Invoice> updatedInvoices) {
        for (InvoiceObserver observer : observers) {
            observer.updateInvoices(updatedInvoices);
        }
    }
    
}
