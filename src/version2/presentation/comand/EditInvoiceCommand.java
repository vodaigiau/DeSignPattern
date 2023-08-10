package version2.presentation.comand;
import version2.presentation.InvoiceController;
import java.time.LocalDate;

public class EditInvoiceCommand implements Command {
    private final InvoiceController controller;
    private final int invoiceId;
    private final LocalDate invoiceDate;
    private final String customerName;
    private final String roomCode;
    private final double unitPrice;
    private final int hoursOrDays;

    public EditInvoiceCommand(InvoiceController controller, int invoiceId, LocalDate invoiceDate, String customerName, String roomCode, double unitPrice, int hoursOrDays) {
        this.controller = controller;
        this.invoiceId = invoiceId;
        this.invoiceDate = invoiceDate;
        this.customerName = customerName;
        this.roomCode = roomCode;
        this.unitPrice = unitPrice;
        this.hoursOrDays = hoursOrDays;
    }

    @Override
    public void execute() {
        controller.editInvoice(invoiceId, invoiceDate, customerName, roomCode, unitPrice, hoursOrDays);
    }
}

