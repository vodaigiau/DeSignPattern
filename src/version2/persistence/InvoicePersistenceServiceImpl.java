package version2.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import version2.domain.model.Invoice;

public class InvoicePersistenceServiceImpl implements InvoicePersistenceService {
    private final String filePath;

    public InvoicePersistenceServiceImpl(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void saveInvoice(Invoice invoice) {
        List<Invoice> invoices = getAllInvoices();
        invoices.add(invoice);
        saveInvoicesToFile(invoices);
    }

    @Override
    public void deleteInvoice(int id) {
        List<Invoice> invoices = getAllInvoices();
        invoices.removeIf(invoice -> invoice.getInvoiceId() == id);
        saveInvoicesToFile(invoices);
    }

    @Override
    public void updateInvoice(Invoice invoice) {
        List<Invoice> invoices = getAllInvoices();
        for (int i = 0; i < invoices.size(); i++) {
            if (invoices.get(i).getInvoiceId() == invoice.getInvoiceId()) {
                invoices.set(i, invoice);
                break;
            }
        }
        saveInvoicesToFile(invoices);
    }

    @Override
    public Invoice getInvoiceById(int id) {
        List<Invoice> invoices = getAllInvoices();
        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceId() == id) {
                return invoice;
            }
        }
        return null;
    }

    @Override
    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            while (true) {
                Invoice invoice = (Invoice) inputStream.readObject();
                invoices.add(invoice);
            }
        } catch (EOFException e) {
            // End of file, do nothing
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    private void saveInvoicesToFile(List<Invoice> invoices) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            for (Invoice invoice : invoices) {
                outputStream.writeObject(invoice);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
