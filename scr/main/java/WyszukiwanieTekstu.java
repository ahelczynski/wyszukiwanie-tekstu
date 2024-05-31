package main.java;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WyszukiwanieTekstu extends BorderPane {
    private TextArea resultArea;
    private TextField patternField;
    private File selectedDirectory;

    public WyszukiwanieTekstu() {
        setPadding(new Insets(10));

        VBox inputPane = createInputPane();
        setTop(inputPane);

        resultArea = new TextArea();
        resultArea.setEditable(false);
        setCenter(resultArea);
    }

    private VBox createInputPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        HBox hbox = new HBox(10);
        Label patternLabel = new Label("Wprowadź szukany tekst:");
        patternField = new TextField();
        Button selectDirectoryButton = new Button("Wybierz katalog");
        Button searchButton = new Button("Szukaj");

        selectDirectoryButton.setOnAction(e -> selectDirectory());
        searchButton.setOnAction(e -> searchFiles());

        patternField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchFiles();
            }
        });

        hbox.getChildren().addAll(patternLabel, patternField, selectDirectoryButton, searchButton);
        vbox.getChildren().add(hbox);

        return vbox;
    }

    private void selectDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = (Stage) getScene().getWindow();
        selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            resultArea.setText("Przeszukiwany katalog: " + selectedDirectory.getAbsolutePath());
        }
    }

    private void searchFiles() {
        if (selectedDirectory == null) {
            resultArea.setText("Wskaż katalog");
            return;
        }

        String pattern = patternField.getText();
        String regexPattern = convertToRegex(pattern);

        File[] txtFiles = selectedDirectory.listFiles((dir, name) -> name.endsWith(".txt"));

        StringBuilder resultBuilder = new StringBuilder();
        if (txtFiles != null) {
            for (File file : txtFiles) {
                searchFile(file, regexPattern, resultBuilder);
            }
        }

        resultArea.setText(resultBuilder.toString());
    }

    private String convertToRegex(String pattern) {
        StringBuilder regex = new StringBuilder();

        for (char c : pattern.toCharArray()) {
            switch (c) {
                case '*':
                    regex.append(".*");
                    break;
                case '?':
                    regex.append(".");
                    break;
                default:
                    regex.append(Pattern.quote(String.valueOf(c)));
                    break;
            }
        }

        return regex.toString();
    }

    private void searchFile(File file, String regexPattern, StringBuilder resultBuilder) {
        Pattern pattern;
        try {
            pattern = Pattern.compile(regexPattern);
        } catch (PatternSyntaxException e) {
            resultArea.setText("Invalid pattern: " + regexPattern);
            return;
        }

        try (Stream<String> lines = Files.lines(Paths.get(file.getPath()))) {
            List<String> matchingLines = lines.filter(line -> pattern.matcher(line).find())
                                              .collect(Collectors.toList());

            for (int i = 0; i < matchingLines.size(); i++) {
                String line = matchingLines.get(i);
                int lineNumber = i + 1;
                resultBuilder.append(String.format("Plik: %s, Linia: %d, Fragment: %s%n", file.getName(), lineNumber, line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
