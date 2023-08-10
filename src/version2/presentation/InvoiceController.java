package version2.presentation;
import version2.domain.model.Invoice;
import version2.presentation.comand.InvoiceService;

import java.time.LocalDate;
import java.util.List;

public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public void addInvoice(int invoiceId, LocalDate invoiceDate, String customerName, String roomCode, double unitPrice, int hoursOrDays) {
        Invoice invoice = new Invoice(invoiceId, invoiceDate, customerName, roomCode, unitPrice, hoursOrDays);
        invoiceService.addInvoice(invoice);
    }

    public void removeInvoice(int invoiceId) {
        invoiceService.removeInvoice(invoiceId);
    }

    public void editInvoice(int invoiceId, LocalDate invoiceDate, String customerName, String roomCode, double unitPrice, int hoursOrDays) {
        Invoice updatedInvoice = new Invoice(invoiceId, invoiceDate, customerName, roomCode, unitPrice, hoursOrDays);
        invoiceService.editInvoice(updatedInvoice);
    }

    public Invoice findInvoice(int invoiceId) {
        return invoiceService.findInvoice(invoiceId);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }
}
