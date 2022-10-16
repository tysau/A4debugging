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
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class Bug2Test {

    Library library;
    Patron patron;

    Item item1;
    Item item2;
    Item item3;
    Item item4;    
    int itemId1;
    int itemId2;
    int itemId3;
    int itemId4;
            
    
    @Mock BorrowItemUI itemUI;
    BorrowItemControl borrowItem;
    
    int expectedLoanTotal;
    int actualLoanTotal;
    
    public Bug2Test() {
    }

    @BeforeEach
    public void setUp() {
    // Create a library, patron, items, and borrowItemUI/Control
    // And borrow 2 items.
    
        library = Library.getInstance();
        patron = library.addPatron("John", "Smith", "dotcom", 1234);
        
        item1 = library.addItem("No", "Yes", "1", ItemType.BOOK);
        item2 = library.addItem("Because", "Why", "2", ItemType.BOOK);
        item3 = library.addItem("Below", "Above", "3", ItemType.BOOK);
        item4 = library.addItem("Inside", "Outside", "4", ItemType.BOOK);
        itemId1 = item1.getId().intValue();
        itemId2 = item2.getId().intValue();
        itemId3 = item3.getId().intValue();
        itemId4 = item4.getId().intValue();
        
        borrowItem = new BorrowItemControl();
        borrowItem.setUI(itemUI);
        
        // Borrow 2 items to begin with
        borrowItem.cardSwiped(patron.getId());
        borrowItem.itemScanned(itemId1);
        borrowItem.itemScanned(itemId2);
    }
    
//    @AfterEach
    public void tearDown() {

    }

    /**
     * Reproducing bug 2.1 - Patron tries to borrow more than loan limit
     * in a single borrowing session.
     */    
    @Test
    public void testBug21() {
        // Setup
        
        // Act - Try to borrow a third item
        try {
            borrowItem.itemScanned(itemId3);
        }
        catch (Exception e) {
            System.out.println("Script 2.1 - Third item refused - Borrow limit");
        }
        
        borrowItem.commitLoans();
        
        // Assert / Results
        expectedLoanTotal = 2;
        actualLoanTotal = patron.getNumberOfCurrentLoans();
        
        System.out.println("Script 2.1 - Loans expected: " + expectedLoanTotal + 
                " Loans actual: " + actualLoanTotal); 
        
        assertEquals(expectedLoanTotal, actualLoanTotal);        
    }    

    /**
     * Reproducing bug 2.2 - Patron can borrow additional item in later visit.
     */
    @Test
    public void testBug22() {
        // Setup
        
        // Act: Try to borrow a third item       
        try {
            borrowItem.itemScanned(itemId3);
        }
        catch (Exception e) {
            System.out.println("Script 2.2 - Third item refused - Borrow limit");        
        }
        borrowItem.commitLoans();

        // Act: Return again and swipe card
        borrowItem = new BorrowItemControl();
        borrowItem.setUI(itemUI);
        borrowItem.cardSwiped(patron.getId());
        
        // Try to borrow an extra item
        try {
            borrowItem.itemScanned(itemId4);
            borrowItem.commitLoans();
        }
        catch (Exception e) {
            System.out.println("Script 2.2 - Borrow extra item rejected");
        }
  
        // Assert / Results
        expectedLoanTotal = 2;        
        actualLoanTotal = patron.getNumberOfCurrentLoans();
        System.out.println("Script 2.2 - Loans expected: " + expectedLoanTotal + 
                " Loans actual: " + actualLoanTotal);  
        
        assertEquals(expectedLoanTotal, actualLoanTotal);
    } 
}
