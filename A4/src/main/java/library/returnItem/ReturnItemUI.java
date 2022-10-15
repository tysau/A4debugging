package library.returnItem;
import java.util.Scanner;


public class ReturnItemUI {

	private enum UiState { INITIALISED, READY, INSPECTING, COMPLETED };

	private ReturnItemControl control;
	private Scanner scanner;
	private UiState state;

	
	public ReturnItemUI(ReturnItemControl control) {
		this.control = control;
		scanner = new Scanner(System.in);
		state = UiState.INITIALISED;
		control.setUi(this);
	}


	public void run() {		
		displayOutput("Return Item Use Case UI\n");
		
		while (true) {
			
			switch (state) {
			
    			case INITIALISED:
    				break;
    				
    			case READY:
    				String itemIdStr = getInput("Scan Item (<enter> completes): ");
    				if (itemIdStr.length() == 0) {
    					control.scanningCompleted();
    				}
    				else {
    					try {
    						long itemId = Long.valueOf(itemIdStr).longValue();
    						control.itemScanned(itemId);
    					}
    					catch (NumberFormatException e) {
    						displayOutput("Invalid itemId");
    					}					
    				}
    				break;				
    				
    			case INSPECTING:
    				String answer = getInput("Is item damaged? (Y/N): ");
    				boolean isDamaged = false;
    				if (answer.toUpperCase().equals("Y")) {				
    					isDamaged = true;
    				}
    				control.dischargeLoan(isDamaged);
    			
    			case COMPLETED:
    				displayOutput("Return processing complete");
    				return;
    			
    			default:
    				displayOutput("Unhandled state");
    				throw new RuntimeException("ReturnItemUI : unhandled state :" + state);			
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
	
	public void setReady() {
		state = UiState.READY;		
	}


	public void setInspecting() {
		state = UiState.INSPECTING;		
	}


	public void setCompleted() {
		state = UiState.COMPLETED;		
	}

	
}
