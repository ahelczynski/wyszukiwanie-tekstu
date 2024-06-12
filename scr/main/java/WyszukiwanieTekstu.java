package main.java;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;

public class WyszukiwanieTekstu extends BorderPane {
    private TextFlow resultArea;
    private TextField patternField;
    private File selectedDirectory;

    public WyszukiwanieTekstu() {
        setPadding(new Insets(10));

        VBox inputPane = createInputPane();
        setTop(inputPane);

        resultArea = new TextFlow();
        resultArea.setPadding(new Insets(10));
        ScrollPane scrollPane = new ScrollPane(resultArea);
        setCenter(scrollPane);
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
            resultArea.getChildren().clear();
            resultArea.getChildren().add(new Text("Przeszukiwany katalog: " + selectedDirectory.getAbsolutePath() + "\n"));
        }
    }

    private void searchFiles() {
        if (selectedDirectory == null) {
            resultArea.getChildren().clear();
            resultArea.getChildren().add(new Text("Wskaż katalog\n"));
            return;
        }

        String pattern = patternField.getText();
        String regexPattern = convertToRegex(pattern);

        File[] txtFiles = selectedDirectory.listFiles((dir, name) -> name.endsWith(".txt"));

        resultArea.getChildren().clear();
        if (txtFiles != null) {
            for (File file : txtFiles) {
                searchFile(file, regexPattern);
            }
        }
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

    private void searchFile(File file, String regexPattern) {
        Pattern pattern;
        try {
            pattern = Pattern.compile(regexPattern);
        } catch (PatternSyntaxException e) {
            resultArea.getChildren().add(new Text("Invalid pattern: " + regexPattern + "\n"));
            return;
        }

        try (Stream<String> lines = Files.lines(Paths.get(file.getPath()))) {
            int lineNumber = 0;
            for (String line : (Iterable<String>) lines::iterator) {
                lineNumber++;
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    resultArea.getChildren().add(formatLine(file.getName(), lineNumber, line, pattern));
                    resultArea.getChildren().add(new Text("\n"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TextFlow formatLine(String fileName, int lineNumber, String line, Pattern pattern) {
        TextFlow textFlow = new TextFlow();

        String prefix = String.format("Plik: %s, Linia: %d, Fragment: ", fileName, lineNumber);
        textFlow.getChildren().add(new Text(prefix));

        Matcher matcher = pattern.matcher(line);
        int lastEnd = 0;
        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                textFlow.getChildren().add(new Text(line.substring(lastEnd, matcher.start())));
            }

            Text matchText = new Text(line.substring(matcher.start(), matcher.end()));
            matchText.setFill(Color.RED);
            textFlow.getChildren().add(matchText);

            lastEnd = matcher.end();
        }

        if (lastEnd < line.length()) {
            textFlow.getChildren().add(new Text(line.substring(lastEnd)));
        }

        return textFlow;
    }
}