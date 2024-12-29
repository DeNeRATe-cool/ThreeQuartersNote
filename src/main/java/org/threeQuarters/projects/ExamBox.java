package org.threeQuarters.projects;

import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import org.threeQuarters.ai.Question;

import java.util.ArrayList;

enum CHOICE{
    A,B,C,D
}

public class ExamBox extends VBox {

    public ArrayList<ToggleButton> buttons;

    @Getter
    @Setter
    private Question question;

    Label ask;
    Label key;

    @Getter
    @Setter
    private boolean enableBoolean;

    @Getter
    @Setter
    private boolean correctBoolean;

    @Getter
    @Setter
    private boolean selectBoolean;

    public ExamBox(Question question)
    {
        this.question = question;
        buttons = new ArrayList<>();
        for(int i = 0;i < question.getOptions().length;i++)
        {
            ToggleButton toggleButton = new ToggleButton(question.getOptionsCut()[i]);
            toggleButton.setId("toggle-button2");
            setButtonsAction(toggleButton);
            buttons.add(toggleButton);
        }
        ask = new Label(question.getQuestionTextCut());
        key = new Label(question.getExplanationCut());
        enableBoolean = true;
    }

    public ExamBox getBox()
    {
        if(this.enableBoolean)
        {
            this.getChildren().clear();
            this.getChildren().add(ask);
            for(ToggleButton option :buttons)this.getChildren().add(option);
        }
        else
        {
            this.getChildren().clear();
            this.getChildren().add(ask);
            int cnt = 0;
            for(ToggleButton option :buttons)
            {
                this.getChildren().add(new Label(((char) ('A'+cnt++)) +  option.getText()));
            }
            this.getChildren().add(new Label("you choose:"+CHOICE2String(getUserChoice())));
            this.getChildren().add(new Label("Correct Key:" + question.getCorrectAnswerCut()));
            this.getChildren().add(key);
        }
        return this;
    }

    private void setButtonsAction(ToggleButton toggleButton)
    {
        toggleButton.selectedProperty().addListener((obs,oldv,newv)->{
            if(newv)
            {
                for(ToggleButton tog : buttons) {
                    if(tog != toggleButton)
                    tog.setSelected(false);
                }
                CHOICE tmp = CHOICE.C;
                selectBoolean = true;
                for(int i = 0;i < buttons.size();i++)
                {
                    if(buttons.get(i) == toggleButton)
                    {
                        switch(i){
                            case 0->{
                                tmp = CHOICE.A;
                            }
                            case 1->{
                                tmp = CHOICE.B;
                            }
                            case 3->{
                                tmp = CHOICE.D;
                            }
                            default -> {
                                tmp = CHOICE.C;
                            }
                        }
                    }
                }
                correctBoolean = (tmp == getKeyChoice());
            }
            else
            {
//                toggleButton.setSelected(false);
            }
        });
    }

    private static String CHOICE2String(CHOICE c)
    {
        switch (c)
        {
            case A->{
                return "A";
            }
            case B->{
                return "B";
            }
            case D->{
                return "D";
            }
            default -> {
                return "C";
            }
        }
    }

    private CHOICE getUserChoice()
    {
        for(int i = 0;i < buttons.size();i++)
        {
            if(buttons.get(i).isSelected())
            {
                switch(i){
                    case 0->{
                        return CHOICE.A;
                    }
                    case 1->{
                        return CHOICE.B;
                    }
                    case 3->{
                        return CHOICE.D;
                    }
                    default -> {
                        return CHOICE.C;
                    }
                }
            }
        }
        return CHOICE.C;
    }

    private CHOICE getKeyChoice()
    {
        String ans = this.question.getCorrectAnswer();

        switch(ans){
            case "A"->{
                return CHOICE.A;
            }
            case "B"->{
                return CHOICE.B;
            }
            case "D"->{
                return CHOICE.D;
            }
            default -> {
                return CHOICE.C;
            }
        }
    }

}
