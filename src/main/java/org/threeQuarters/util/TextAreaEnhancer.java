package org.threeQuarters.util;

import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TextAreaEnhancer {
    public static void enhanceTextArea(TextArea textArea) {
        textArea.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String input = event.getCharacter();
            int caretPosition = textArea.getCaretPosition();

            switch (input) {
                case "\"":
                    textArea.insertText(caretPosition, "\"\"");
                    textArea.positionCaret(caretPosition + 1);
                    event.consume();
                    break;
                case "(":
                    textArea.insertText(caretPosition, "()");
                    textArea.positionCaret(caretPosition + 1);
                    event.consume();
                    break;
                case "[":
                    textArea.insertText(caretPosition, "[]");
                    textArea.positionCaret(caretPosition + 1);
                    event.consume();
                    break;
                case "{":
                    textArea.insertText(caretPosition, "{}");
                    textArea.positionCaret(caretPosition + 1);
                    event.consume();
                    break;
            }
        });

        textArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                int caretPosition = textArea.getCaretPosition();
                String textBeforeCaret = textArea.getText(0, caretPosition);
                int lastNewLineIndex = textBeforeCaret.lastIndexOf("\n");

                String lastLine = textBeforeCaret.substring(lastNewLineIndex + 1);
                StringBuilder indentation = new StringBuilder();

                for (char c : lastLine.toCharArray()) {
                    if (c == ' ' || c == '\t') {
                        indentation.append(c);
                    } else {
                        break;
                    }
                }

                textArea.insertText(caretPosition, "\n" + indentation.toString());
                textArea.positionCaret(caretPosition + indentation.length() + 1);
                event.consume();
            }
        });
    }
}
