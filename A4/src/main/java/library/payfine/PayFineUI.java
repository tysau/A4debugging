package library.payfine;
import java.util.Scanner;


public class PayFineUI {


	private enum UiState { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };

	private PayFineControl control;
	private Scanner scanner;
	private UiState state;

	
	public PayFineUI(PayFineControl control) {
		this.control = control;
		scanner = new Scanner(System.in);
		state = UiState.INITIALISED;
		control.setUi(this);
	}
	
	
	public void run() {
		displayOutput("Pay Fine Use Case UI\n");
		
		while (true) {
			
			switch (state) {
			
    			case READY:
    				String patronIdStr = getInput("Swipe patron card (press <enter> to cancel): ");
    				if (patronIdStr.length() == 0) {
    					control.cancel();
    					break;
    				}
    				try {
    					long patronId = Long.valueOf(patronIdStr).longValue();
    					control.cardSwiped(patronId);
    				}
    				catch (NumberFormatException e) {
    					displayOutput("Invalid patronID");
    				}
    				break;
    				
    			case PAYING:
    				double amount = 0;
    				String amountStr = getInput("Enter amount (<Enter> cancels) : ");
    				if (amountStr.length() == 0) {
    					control.cancel();
    					break;
    				}
    				try {
    					amount = Double.valueOf(amountStr).doubleValue();
    				}
    				catch (NumberFormatException e) {}
    				if (amount <= 0) {
    					displayOutput("Amount must be positive");
    					break;
    				}
    				control.payFine(amount);
    				break;
    								
    			case CANCELLED:
    				displayOutput("Pay Fine process cancelled");
    				return;
    			
    			case COMPLETED:
    				displayOutput("Pay Fine process complete");
    				return;
    			
    			default:
    				displayOutput("Unhandled state");
    				throw new RuntimeException("FixBookUI : unhandled state :" + state);			  			
			}		
		}		
	}

	
	private String getInput(String prompt) {
		System.out.print(prompt);
		return scanner.nextLine();
	}	
		
		
	private void displayOutput(Object object) {
		System.out.println(object);
	}	
			

	public void display(Object object) {
		displayOutput(object);
	}


	public void setCompleted() {
		state = UiState.COMPLETED;		
	}


	public void setPaying() {
		state = UiState.PAYING;		
	}


	public void setCancelled() {
		state = UiState.CANCELLED;		
	}


	public void setReady() {
		state = UiState.READY;		
	}


}
