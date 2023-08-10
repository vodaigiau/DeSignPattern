package version2.domain.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;

public class Invoice implements Serializable {
    private int invoiceId;
    private LocalDate invoiceDate;
    private String customerName;
    private String roomCode;
    private double unitPrice;
    private int hoursOrDays;

    public Invoice(int invoiceId, LocalDate invoiceDate, String customerName, String roomCode, double unitPrice, int hoursOrDays) {
        this.invoiceId = invoiceId;
        this.invoiceDate = invoiceDate;
        this.customerName = customerName;
        this.roomCode = roomCode;
        this.unitPrice = unitPrice;
        this.hoursOrDays = hoursOrDays;
    }

    // Getters and setters

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getHoursOrDays() {
        return hoursOrDays;
    }

    public void setHoursOrDays(int hoursOrDays) {
        this.hoursOrDays = hoursOrDays;
    }

    // Business logic to compute total price
    public double computeTotalPrice() {
        if (hoursOrDays > 30) {
            return -1; // Indicate invalid
        } else if (hoursOrDays > 24) {
            hoursOrDays = 24;
        }
        return hoursOrDays * unitPrice;
    }

    // Business logic to compute average price
    public double computeAveragePrice() {
        double totalPrice = computeTotalPrice();
        if (totalPrice >0) {
            return 0; // Indicate invalid
        }

        if (hoursOrDays > 7) {
           double discountedPrice = totalPrice * 0.8;
        int daysInMonth = YearMonth.of(invoiceDate.getYear(), invoiceDate.getMonthValue()).lengthOfMonth();
        int remainingDays = daysInMonth - 7;
        return (discountedPrice + (totalPrice - discountedPrice)) / remainingDays;
        }
        return totalPrice / hoursOrDays;
    }

    // Display invoice information
    public void displayInvoice() {
        System.out.println("Invoice ID: " + invoiceId);
        System.out.println("Invoice Date: " + invoiceDate);
        System.out.println("Customer Name: " + customerName);
        System.out.println("Room Code: " + roomCode);
        System.out.println("Unit Price: " + unitPrice);
        System.out.println("Hours or Days: " + hoursOrDays);
        double totalPrice = computeTotalPrice();
        if (totalPrice == -1) {
            System.out.println("Invalid hours/days for calculation.");
        } else {
            System.out.println("Total Price: " + totalPrice);
            System.out.println("Average Price: " + computeAveragePrice());
        }
    }
}
