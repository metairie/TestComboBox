package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.fxpart.KeyValueString;
import org.fxpart.mockserver.MockDatas;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    private static final KeyCodeCombination UP = new KeyCodeCombination(KeyCode.UP);
    private static final KeyCodeCombination DOWN = new KeyCodeCombination(KeyCode.DOWN);
    private static final KeyCodeCombination LEFT = new KeyCodeCombination(KeyCode.LEFT);
    private static final KeyCodeCombination RIGHT = new KeyCodeCombination(KeyCode.RIGHT);
    private static final KeyCodeCombination BACK_SPACE = new KeyCodeCombination(KeyCode.BACK_SPACE);
    private static final KeyCodeCombination DELETE = new KeyCodeCombination(KeyCode.DELETE);
    private static final KeyCodeCombination HOME = new KeyCodeCombination(KeyCode.HOME);
    private static final KeyCodeCombination TAB = new KeyCodeCombination(KeyCode.TAB);
    private static final KeyCodeCombination END = new KeyCodeCombination(KeyCode.END);

    private Function<String, List<KeyValueString>> searchFunction = (term -> {
        ObservableList<KeyValueString> list = FXCollections.observableArrayList(getDataSource().stream().filter(item -> item.getValue().contains(term == null ? "" : term)).collect(Collectors.toList()));
        return list;
    });

    @FXML
    ComboBox<KeyValueString> combo;

    static String some;
    static String typedText;
    static StringBuilder sb = new StringBuilder();

    public enum AutoCompleteMode {
        STARTS_WITH, CONTAINING,;
    }

    ObservableList<KeyValueString> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCustomCellFactory();
        setTextFieldFormatter(new Function<KeyValueString, String>() {
            @Override
            public String apply(KeyValueString item) {
                System.out.println(" setTextFieldFormatter (item.getValue) = " + item.getValue());
                String ret = String.format("%s", item.getValue());
                return ret;
            }
        });
        combo.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                combo.hide();
            }
        });
        combo.addEventHandler(KeyEvent.KEY_RELEASED, createKeyReleaseEventHandler());
        combo.setItems(FXCollections.observableArrayList(new MockDatas().loadLocation()));
        data = combo.getItems();
    }

    private EventHandler<KeyEvent> createKeyReleaseEventHandler() {
        return event -> {
            System.out.println(" k = " + combo.getEditor().getText());
            if (DOWN.match(event)) {
                showCombo();
                return;
            } else if (UP.match(event) || RIGHT.match(event) || LEFT.match(event) || HOME.match(event) || END.match(event) || TAB.match(event) || event.isControlDown()) {
                return;
            } else {
                if (DELETE.match(event) || BACK_SPACE.match(event)) {
                    System.out.println(" EventHandler " + combo.getEditor().getText());
                }
                // search if possible
//                combo.getItems().setAll(getSearchFunction().apply(combo.getEditor().getText()));
                String term = combo.getEditor().getText();
                List<KeyValueString> list = new MockDatas().loadLocation();
                ObservableList<KeyValueString> list2 = FXCollections.observableArrayList(list);
                showCombo();
            }
        };
    }

    private void showCombo() {
        if (!combo.isShowing()) {
            combo.show();
        }
    }

    private void setTextFieldFormatter(Function<KeyValueString, String> textFieldFormatter) {
        combo.setConverter(new StringConverter<KeyValueString>() {
            @Override
            public String toString(KeyValueString t) {
                return t == null ? null : textFieldFormatter.apply(t);
            }

            @Override
            public KeyValueString fromString(String string) {
                return combo.getValue();
            }
        });
    }

    private void setCustomCellFactory() {
        combo.setCellFactory(new Callback<ListView<KeyValueString>, ListCell<KeyValueString>>() {
                                 @Override
                                 public ListCell<KeyValueString> call(ListView<KeyValueString> param) {
                                     param.setPrefHeight(200);
                                     param.setPrefWidth(400);
                                     final ListCell<KeyValueString> cell = new ListCell<KeyValueString>() {
                                         @Override
                                         protected void updateItem(KeyValueString item, boolean empty) {
                                             super.updateItem(item, empty);
                                             System.out.println(" update item " + (item == null ? " NULL " : " " + item.getValue()));
                                             if (item == null || empty) {
                                                 // setGraphic(null);
                                                 setText(null);
                                             } else {
                                                 setText(item.getKey() + " - " + item.getValue());
                                             }
                                         }
                                     };
                                     return cell;
                                 }
                             }
        );
    }

    public List<KeyValueString> getDataSource() {
        return FXCollections.observableArrayList(new MockDatas().loadLocation());
    }

    public Function<String, List<KeyValueString>> getSearchFunction() {
        return searchFunction;
    }

    public void click(ActionEvent actionEvent) {
    }

}
