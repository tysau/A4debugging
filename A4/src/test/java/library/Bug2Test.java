package library;


import library.borrowitem.BorrowItemControl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import library.borrowitem.BorrowItemUI;
import library.entities.Item;
import library.entities.ItemType;
import library.entities.Library;
import library.entities.Patron;
import static org.mockito.ArgumentMatchers.any;


public class Bug2Test {

    Library library;
    Patron patron;

    Item item1;
    Item item2;
    Item item3;
    Item item4;
    
    @Mock BorrowItemUI itemUI;
    BorrowItemControl borrowItem;
    
    public Bug2Test() {
    }

    @BeforeEach
    public void setUp() {
    // Create a library, patron, items, and borrowItemUI/Control
        library = Library.getInstance();
    
        patron = library.addPatron("John", "Smith", "dotcom", 1234);
        
        item1 = library.addItem("No", "Yes", "1", ItemType.BOOK);
        item2 = library.addItem("Because", "Why", "2", ItemType.BOOK);
        item3 = library.addItem("Below", "Above", "3", ItemType.BOOK);
        item4 = library.addItem("Inside", "Outside", "4", ItemType.BOOK);

        borrowItem = new BorrowItemControl();
        itemUI = spy(new BorrowItemUI(borrowItem));
    }
    
//    @AfterEach
    public void tearDown() {

    }

    /**
     * Reproducing bug 2.1 - Patron can borrow more than 2 items.
     */
    @Test
    public void testBug2() {
        // Setup
        int expectedLoanTotal;
        int actualLoanTotal;

        Mockito.doNothing().when(itemUI).setReady();
        Mockito.doNothing().when(itemUI).setScanning();
        Mockito.doNothing().when(itemUI).setFinalising();
        Mockito.doNothing().when(itemUI).setCompleted();
        Mockito.doNothing().when(itemUI).display(any(Object.class));
        
        // Act
        // Attempt to borrow 3 items
        borrowItem.cardSwiped(patron.getId());
        borrowItem.itemScanned(5);
        borrowItem.itemScanned(6);
        
        try {
            borrowItem.itemScanned(7);
        }
        catch (Exception e) {
        }
        
        borrowItem.commitLoans();
        
        // Assert / Results
        expectedLoanTotal = 2;
        actualLoanTotal = patron.getNumberOfCurrentLoans();
        System.out.println("Script 2.1 - Loans, expected: " + expectedLoanTotal + 
                " Loans actual: " + actualLoanTotal); 
        
        assertEquals(expectedLoanTotal, actualLoanTotal);
    } 
    
    /**
     * Reproducing bug 2A - Patron can borrow additional item in later visit.
     */
    @Test
    public void testBug2a() {
        // Setup
        int expectedLoanTotal;
        int actualLoanTotal;

        Mockito.doNothing().when(itemUI).setReady();
        Mockito.doNothing().when(itemUI).setScanning();
        Mockito.doNothing().when(itemUI).setFinalising();
        Mockito.doNothing().when(itemUI).setCompleted();
        Mockito.doNothing().when(itemUI).display(any(Object.class));
        
        // Act:
        // Borrowing session 1
        borrowItem.cardSwiped(patron.getId());   
        borrowItem.itemScanned(1);
        borrowItem.itemScanned(2);
        
        try {
        borrowItem.itemScanned(3);
        }
        catch (Exception e) {
        }
        borrowItem.commitLoans();
        
        // Borriwing session 2
        borrowItem = new BorrowItemControl();
        itemUI = spy(new BorrowItemUI(borrowItem));

        borrowItem.cardSwiped(patron.getId());
        
        try {
        borrowItem.itemScanned(4);
        borrowItem.commitLoans();
        }
        catch (Exception e) {
        }
  
        // Assert / Results
        expectedLoanTotal = 2;        
        actualLoanTotal = patron.getNumberOfCurrentLoans();
        System.out.println("Script 2.2 - Loans, expected: " + expectedLoanTotal + 
                " Loans actual: " + actualLoanTotal);  
        
        assertEquals(expectedLoanTotal, actualLoanTotal);
    } 
}
