package library.borrowitem;
import java.util.Scanner;


public class BorrowItemUI {
	
	public static enum UiState { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };

	private UiState state;
	private BorrowItemControl control;
	private Scanner scanner;

	
	public BorrowItemUI(BorrowItemControl control) {
		this.control = control;
		scanner = new Scanner(System.in);
		state = UiState.INITIALISED;
		control.setUI(this);
	}

	
	private String getInput(String prompt) {
		System.out.print(prompt);
		return scanner.nextLine();
	}	
		
		
	private void displayOutput(Object object) {
		System.out.println(object);
	}
	
				
	public void run() {
		displayOutput("Borrow Item Use Case UI\n");
		
		while (true) {
			
			switch (state) {			
			
    			case CANCELLED:
    				displayOutput("Borrowing Cancelled");
    				return;
    				
    			case READY:
    				String patrodIdStr = getInput("Swipe patron card (press <enter> to cancel): ");
    				if (patrodIdStr.length() == 0) {
    					control.cancel();
    					break;
    				}
    				try {
    					long patronId = Long.valueOf(patrodIdStr).longValue();
    					control.cardSwiped(patronId);
    				}
    				catch (NumberFormatException e) {
    					displayOutput("Invalid Patron Id");
    				}
    				break;
    				
    			case RESTRICTED:
    				getInput("Press <any key> to cancel");
    				control.cancel();
    				break;			
    				
    			case SCANNING:
    				String itemIdStr = getInput("Scan Item (<enter> completes): ");
    				if (itemIdStr.length() == 0) {
    					control.borrowingCompleted();
    					break;
    				}
    				try {
    					int itemId = Integer.valueOf(itemIdStr).intValue();
    					control.itemScanned(itemId);
    					
    				} catch (NumberFormatException e) {
    					displayOutput("Invalid Item Id");
    				} 
    				break;					
    				
    			case FINALISING:
    				String answer = getInput("Commit loans? (Y/N): ");
    				if (answer.toUpperCase().equals("N")) {
    					control.cancel();					
    				} 
    				else {
    					control.commitLoans();
    					getInput("Press <any key> to complete ");
    				}
    				break;
    								
    			case COMPLETED:
    				displayOutput("Borrowing Completed");
    				return;	
    				
    			default:
    				displayOutput("Unhandled state");
    				throw new RuntimeException("BorrowItemUI : unhandled state :" + state);			
			}
		}		
	}


	public void display(Object object) {
		displayOutput(object);		
	}


	public void setReady() {
		state = UiState.READY;		
	}


	public void setScanning() {
		state = UiState.SCANNING;		
	}


	public void setRestricted() {
		state = UiState.RESTRICTED;		
	}

	public void setFinalising() {
		state = UiState.FINALISING;		
	}


	public void setCompleted() {
		state = UiState.COMPLETED;		
	}

	public void setCancelled() {
		state = UiState.CANCELLED;		
	}

}
