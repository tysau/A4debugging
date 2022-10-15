/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
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
import org.junit.jupiter.api.function.Executable;
import static org.mockito.ArgumentMatchers.anyDouble;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author tysau
 */
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
     * Test of incorrect late fee.
     */
    @Test
    public void testItem() {
        
        Double quotedFee;
        Double actualFee;
        
        calendar.incrementDate(3);
        library.updateCurrentLoanStatus();        
        System.out.println(library.listCurrentLoans());

        Mockito.doNothing().when(returnItemUI).setReady();
        
        returnItem.itemScanned(1);
        
        quotedFee = library.calculateOverDueFine(loan);
        
        returnItem.dischargeLoan(false);
        
        actualFee = patron.finesOwed();
        
        System.out.println("fines, quoted: " + quotedFee + 
                " actual: " + patron.finesOwed());

        assertEquals(quotedFee, actualFee);
    }
    
}
