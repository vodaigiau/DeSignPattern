package version2.presentation.comand;

import version2.presentation.InvoiceController;

public class RemoveInvoiceCommand implements Command {
    private final InvoiceController controller;
    private final int invoiceId;

    public RemoveInvoiceCommand(InvoiceController controller, int invoiceId) {
        this.controller = controller;
        this.invoiceId = invoiceId;
    }

    @Override
    public void execute() {
        controller.removeInvoice(invoiceId);
    }
}



