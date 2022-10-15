package library;


import library.borrowitem.BorrowItemControl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class Bug2Test {
    
    Library library;
    
    Loan loan;
    Patron patron;
//    Calendar calendar;

    Item item1;
    Item item2;
    Item item3;
    Item item4;

    BorrowItemControl borrowItem;
    
    @Mock BorrowItemUI itemUI;
    
    public Bug2Test() {
    }
    
   
    @BeforeEach
    public void setUp() {
    
        library = Library.getInstance();
    
        patron = library.addPatron("John", "Smith", "dotcom", 1234);
        
        item1 = library.addItem("No", "Yes", "1", ItemType.BOOK);
        item2 = library.addItem("Because", "Why", "2", ItemType.BOOK);
        item3 = library.addItem("Below", "Above", "3", ItemType.BOOK);
        item4 = library.addItem("Inside", "Outside", "4", ItemType.BOOK);
        
//        calendar = Calendar.getInstance();

        borrowItem = new BorrowItemControl();
        itemUI = spy(new BorrowItemUI(borrowItem));

    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Reproducing bug 1 - Incorrect late fee.
     */
    @Test
    public void testItem() {
        
        int expectedLoanTotal;
        int actualLoanTotal;

        Mockito.doNothing().when(itemUI).setReady();
        Mockito.doNothing().when(itemUI).setScanning();
        Mockito.doNothing().when(itemUI).setFinalising();
        Mockito.doNothing().when(itemUI).setCompleted();
        Mockito.doNothing().when(itemUI).display(any(Object.class));
        
        borrowItem.cardSwiped(patron.getId());
        
        borrowItem.itemScanned(1);
        borrowItem.itemScanned(2);
        borrowItem.itemScanned(3);
        
        borrowItem.commitLoans();
        
        
        expectedLoanTotal = 2;
        actualLoanTotal = patron.getNumberOfCurrentLoans();
        
        System.out.println("Loans, expected: " + expectedLoanTotal + 
                " actual: " + actualLoanTotal);

        assertEquals(expectedLoanTotal, actualLoanTotal);
    } 
}
