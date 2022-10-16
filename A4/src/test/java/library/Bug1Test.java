package library;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import library.borrowitem.BorrowItemUI;
import library.entities.Calendar;
import library.entities.Item;
import library.entities.ItemType;
import library.entities.Library;
import library.entities.Loan;
import library.entities.Patron;
import library.payfine.PayFineControl;
import library.payfine.PayFineUI;
import library.returnItem.ReturnItemControl;
import library.returnItem.ReturnItemUI;

public class Bug1Test {
    
    Library library;
    
    Loan loan;
    Patron patron;
    Calendar calendar;
    Item item;
    ReturnItemControl returnItem;
    PayFineControl payFine;
    
    @Mock BorrowItemUI itemUI;
    @Mock ReturnItemUI returnItemUI;
    @Mock PayFineUI payFineUI;
    
    public Bug1Test() {
    }
    
   
    @BeforeEach
    public void setUp() {
    
        library = Library.getInstance();
    
        patron = library.addPatron("John", "Smith", "dotcom", 1234);
        item = library.addItem("No", "Yes", "1", ItemType.BOOK);
        calendar = Calendar.getInstance();
        loan = library.issueLoan(item, patron);
        returnItem = new ReturnItemControl();
        returnItemUI = spy(new ReturnItemUI(returnItem));

    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Reproducing bug 1 - Incorrect late fee.
     */
    @Test
    public void testItem() {
        
        Double quotedFee;
        Double actualFee;
        
        calendar.incrementDate(3);
        library.updateCurrentLoanStatus();        

        Mockito.doNothing().when(returnItemUI).setReady();
        
        returnItem.itemScanned(item.getId());
        
        quotedFee = library.calculateOverDueFine(loan);
        
        returnItem.dischargeLoan(false);
        
        actualFee = patron.finesOwed();
        
        System.out.println("fines, quoted: " + quotedFee + 
                " actual: " + patron.finesOwed());

        assertEquals(quotedFee, actualFee);
    } 
}
