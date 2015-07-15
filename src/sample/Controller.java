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
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.fxpart.KeyValueString;
import org.fxpart.mockserver.MockDatas;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;
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
    private static final String HIGHLIGHTED_DROPDOWN_CLASS = "highlighted-dropdown";
    private static final String USUAL_DROPDOWN_CLASS = "usual-dropdown";

    private ObservableList<KeyValueString> dataSource = FXCollections.observableArrayList(new MockDatas().loadLocation());
    private Function<String, List<KeyValueString>> searchFunction = (term -> {
        ObservableList<KeyValueString> list = FXCollections.observableArrayList(getDataSource().stream().filter(new Predicate<KeyValueString>() {
            @Override
            public boolean test(KeyValueString item) {
                return item.getValue().contains(term == null ? "" : term);
            }
        }).collect(Collectors.toList()));
        return list;
    });

    @FXML
    ComboBox<KeyValueString> combo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCustomCellFactory();
        setTextFieldFormatter(item -> String.format("%s", item.getValue()));
        combo.addEventHandler(KeyEvent.KEY_RELEASED, createKeyReleaseEventHandler());
    }

    private EventHandler<KeyEvent> createKeyReleaseEventHandler() {
        return event -> {
            if (DOWN.match(event)) {
                if (!combo.isShowing()) {
                    combo.show();
                }
                return;
            } else if (UP.match(event) || RIGHT.match(event) || LEFT.match(event) || HOME.match(event) || END.match(event) || TAB.match(event) || event.isControlDown()) {
                return;
            } else {
                // search if possible
                ObservableList<KeyValueString> list = combo.getItems();
                list.setAll(getSearchFunction().apply(combo.getEditor().getText()));
                combo.show();
            }
        };
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
                                     param.setPrefHeight(150);
                                     final ListCell<KeyValueString> cell = new ListCell<KeyValueString>() {
                                         @Override
                                         protected void updateItem(KeyValueString item, boolean empty) {
                                             super.updateItem(item, empty);
                                             if (item == null || empty) {
                                                 setGraphic(null);
                                             } else {
                                                 HBox styledText = new HBox();
                                                 String keyString = item.getKey();
                                                 String valueString = (item).getValue();
                                                 String itemString = keyString + " - " + valueString;
                                                 if (combo.getEditor().getText().length() != 0) {
                                                     Integer searchStringPosition = valueString.indexOf(combo.getEditor().getText());

                                                     // itemString contains searchString. It should be split and searchString should be highLighted
                                                     if (searchStringPosition >= 0) {
                                                         String beginString = valueString.substring(0, searchStringPosition);
                                                         String highlightedString = valueString.substring(searchStringPosition, searchStringPosition + combo.getEditor().getText().length());
                                                         String endString = valueString.substring(searchStringPosition + combo.getEditor().getText().length());

                                                         Text separator = new Text(keyString + " - ");
                                                         separator.getStyleClass().add(USUAL_DROPDOWN_CLASS);
                                                         styledText.getChildren().add(separator);

                                                         final Text begin = new Text(beginString);
                                                         begin.getStyleClass().add(USUAL_DROPDOWN_CLASS);
                                                         styledText.getChildren().add(begin);

                                                         final Text highlighted = new Text(highlightedString);
                                                         highlighted.getStyleClass().add(HIGHLIGHTED_DROPDOWN_CLASS);
                                                         styledText.getChildren().add(highlighted);

                                                         final Text end = new Text(endString);
                                                         end.getStyleClass().add(USUAL_DROPDOWN_CLASS);
                                                         styledText.getChildren().add(end);


                                                     } else {
                                                         styledText.getChildren().add(new Text(itemString));
                                                     }
                                                 } else {
                                                     styledText.getChildren().add(new Text(itemString));
                                                 }
                                                 setGraphic(styledText);
                                             }
                                         }
                                     };
                                     return cell;
                                 }
                             }
        );
    }

    public List<KeyValueString> getDataSource() {
        return dataSource;
    }

    public void setDataSource(ObservableList<KeyValueString> dataSource) {
        this.dataSource = dataSource;
    }

    public Function<String, List<KeyValueString>> getSearchFunction() {
        return searchFunction;
    }

    public void click(ActionEvent actionEvent) {

    }
}
