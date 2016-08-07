/*
MIT License

Copyright (c) 2016 Chris Hall

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package cyano.dnd5e.ase;

import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/**
 *
 * @author cyanobacterium
 */
public class MainViewController implements Initializable {
	
	private static final int[] POINT_BUY_VALUES = {Integer.MIN_VALUE,
		-15,-10,-7,-5,-3,
		-2,-1,0,1,2,
		3,4,5,7,9,
		12,15,19,24,30
	};
	
	@FXML private Spinner ptBuy;
	@FXML private Spinner stat1;
	@FXML private Spinner stat2;
	@FXML private Spinner stat3;
	@FXML private Spinner stat4;
	@FXML private Spinner stat5;
	@FXML private Label output;
	@FXML private Label total;
	
	private final Spinner[] spinners = new Spinner[5];
	
	@FXML
	private void update() {
		//
		log().finer("\tUpdating");
		int goal = (Integer)ptBuy.getValue();
		log().fine("Point buy target = "+goal);
		
		int sum = 0;
		for(int i = 0; i < 5; i++){
			int score = (Integer)spinners[i].getValue();
			int pts = POINT_BUY_VALUES[score];
			sum += pts;
			log().fine("\t"+score+"\t("+pts+" pts)");
		}
		log().fine("Total point value = "+sum+" pts");
		
		int result = 1;
		
		int pointsRemeining = (goal - sum);
		log().finer("Need to make up "+pointsRemeining+" pts");
		for(int i = POINT_BUY_VALUES.length-1; i > 0; i--){
			if(POINT_BUY_VALUES[i] <= pointsRemeining){
				result = i;
				break;
			}
		}
		
		int actual = sum+POINT_BUY_VALUES[result];
		
		log().info("Assigned value "+result+" for "+actual+"/"+goal+" pts");
		output.setText(String.valueOf(result));
		total.setText(String.valueOf("Point Total = " + actual + " / " + goal + " points"));
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ChangeListener spinnerUpdateListener = (ChangeListener) (ObservableValue observable, Object oldValue, Object newValue) ->update();
		
		SpinnerValueFactory buyValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,POINT_BUY_VALUES[18]*6,27);
		ptBuy.setValueFactory(buyValueFactory);
		ptBuy.valueProperty().addListener(spinnerUpdateListener);
		
		spinners[0] = stat1;
		spinners[1] = stat2;
		spinners[2] = stat3;
		spinners[3] = stat4;
		spinners[4] = stat5;
		
		int[] starting = {15,14,13,12,10};
		
		
		for(int i = 0; i < 5; i++){
			SpinnerValueFactory abilityScoreVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(3,18,10);
			spinners[i].setValueFactory(abilityScoreVF);
			spinners[i].valueProperty().addListener(spinnerUpdateListener);
			abilityScoreVF.setValue(starting[i]);
		}
		
		
		update();
	}	
	
	private static Logger log(){
		return Logger.getLogger(MainViewController.class.getName());
	}
	
	
	@FXML private void rollStats(){
		Random r = new Random();
		for(int i = 0; i < 5; i++){
			int[] rolls = new int[4];
			for(int n = 0; n < rolls.length; n++){
				rolls[n] = r.nextInt(6) + 1;
			}
			log().finer("\tRolled 4d6: "+Arrays.toString(rolls));
			Arrays.sort(rolls);
			int sum = 0; 
			log().finer("\tDropping "+rolls[0]);
			for(int n = 1; n < rolls.length; n++){
				sum += rolls[n];
			}
			log().info("Rolled "+Arrays.toString(rolls)+" = "+sum);
			spinners[i].getValueFactory().setValue(sum);
		}
		update();
	}
	
}
