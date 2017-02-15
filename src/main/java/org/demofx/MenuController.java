package org.demofx;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;


import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import org.apache.poi.ss.usermodel.Cell;
import org.demofx.compare.ProcessCompare;
import org.demofx.tasklist.TaskListing;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFChart;

import org.demofx.report.ReportGenerator;
import org.demofx.dto.ProcessesDto;
import org.demofx.dto.TasksDto;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


@Named
public class MenuController implements Initializable {
    @FXML
    private MenuBar menuBar;

    @FXML
    private GridPane gridPane;

    @FXML
    private MenuItem importAndCompare;

    @FXML
    private MenuItem saveReportBt;

    @FXML
    private MenuItem exportExcel;

    @Inject
    private ReportGenerator reportGenerator;

    @Inject
    private org.demofx.marshal.Marshaller marshaller;

    @Inject
    private TaskListing taskListing;

    @Inject
    private ProcessCompare processCompare;

    public static final String PROCESS_NAME = "Process name";
    public static final String PROCESS_UID = "Process UID";
    public static final String PROCESS_MEMORY_USED = "Process memory used";
    public static final String NEW_LINE = System.getProperty("line.separator");

    private ObservableList<OsProcess> fullProcessesInfo = FXCollections.observableArrayList();


    @FXML
    private void exitAction(final ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void executeTaskListGroupByName() {

    }

    @FXML
    private void executeTaskList() {

        List<OsProcess> osProcesses = taskListing.getRunningTaskInfo();
        fullProcessesInfo.clear();
        fullProcessesInfo.addAll(osProcesses);
        //filling table
        TableView<OsProcess> osProcessTableView = createFullTable(fullProcessesInfo);

        gridPane.getChildren().clear();
        gridPane.getChildren().addAll(osProcessTableView);

        //todo : have to find better solution
        gridPane.setHgrow(gridPane.getChildren().get(0), Priority.ALWAYS);
        gridPane.setVgrow(gridPane.getChildren().get(0), Priority.ALWAYS);
        saveReportBt.setDisable(false);
        exportExcel.setDisable(false);
        importAndCompare.setDisable(false);
    }

    @FXML
    private void exportExcelAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx"));
        File selectedFile =
                fileChooser.showSaveDialog(menuBar.getScene().getWindow());
        if (selectedFile != null)
            try {
                reportGenerator.generateReport(GroupUtil.groupByName(fullProcessesInfo), selectedFile.getAbsolutePath());
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Couldn't save file");
                alert.setContentText("Try again!(Check disk space, etc)");

                alert.showAndWait();
            }
    }

    @FXML
    private void saveAction(final ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
        File selectedFile =
                fileChooser.showSaveDialog(menuBar.getScene().getWindow());
        if (selectedFile != null) {
            TasksDto tasksDto = new TasksDto(GroupUtil.groupByName(fullProcessesInfo));
            try {

                marshaller.marshal(tasksDto, selectedFile.getAbsolutePath());
            } catch (IOException | JAXBException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error during export to  XML");
                alert.setHeaderText("Couldn't save file");
                alert.setContentText("Repeat operation!");

                alert.showAndWait();
            }
        }
    }

    @FXML
    private void importAction(final ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(menuBar.getScene().getWindow());

        if (file != null) {
            List<ProcessesDto> importedProcessInfo;
            try {
                TableView<ComparedInfo> tableLeft = new TableView<>();
                importedProcessInfo = marshaller.unmarshal(file.getAbsolutePath()).getProcessesDtos();

                List<ComparedInfo> comparedInfos = processCompare.compareProcesses(GroupUtil.groupByName(fullProcessesInfo), importedProcessInfo);

                gridPane.getChildren().clear();
                gridPane.getChildren().addAll(createTable(tableLeft, comparedInfos));

                //todo : have to find better solution
                gridPane.setHgrow(gridPane.getChildren().get(0), Priority.ALWAYS);
                gridPane.setVgrow(gridPane.getChildren().get(0), Priority.ALWAYS);

            } catch (JAXBException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error during export to import XML file");
                alert.setHeaderText("Couldn't import file");
                alert.setContentText("Select file with correct structure!");

                alert.showAndWait();
            }
        }
    }

    private TableView<OsProcess> createFullTable(List<OsProcess> osProcesses) {

        TableView<OsProcess> osProcessTableView = new TableView<>();
        TableColumn<OsProcess, String> processNameCol = new TableColumn<>(PROCESS_NAME);
        processNameCol.setMinWidth(200);
        processNameCol.setCellValueFactory(
                new PropertyValueFactory<OsProcess, String>("imageName"));

        TableColumn<OsProcess, Double> pidCol = new TableColumn<>(PROCESS_UID);
        pidCol.setMinWidth(200);
        pidCol.setCellValueFactory(
                new PropertyValueFactory<OsProcess, Double>("pid"));

        TableColumn<OsProcess, Double> memoryCol = new TableColumn<>(PROCESS_MEMORY_USED);
        memoryCol.setMinWidth(200);
        memoryCol.setCellValueFactory(
                new PropertyValueFactory<OsProcess, Double>("memoryUsedKB"));

        ObservableList<OsProcess> data =
                FXCollections.observableArrayList(osProcesses);

        osProcessTableView.getColumns().addAll(processNameCol, pidCol, memoryCol);
        osProcessTableView.setItems(data);
        return osProcessTableView;
    }

    private TableView createTable(TableView<ComparedInfo> table, List<ComparedInfo> comparedInfos) {

        TableColumn currentData = new TableColumn("Current data");
        TableColumn<ProcessesDto, String> currentProcessNameCol = new TableColumn<>(PROCESS_NAME);
        currentProcessNameCol.setMinWidth(200);
        currentProcessNameCol.setCellValueFactory(
                new PropertyValueFactory<ProcessesDto, String>("currentProcessName"));
        currentProcessNameCol.setSortable(false);

        TableColumn<ProcessesDto, Double> currentMemoryCol = new TableColumn<>(PROCESS_MEMORY_USED);
        currentMemoryCol.setMinWidth(200);
        currentMemoryCol.setCellValueFactory(
                new PropertyValueFactory<ProcessesDto, Double>("currentProcessMemory"));
        currentMemoryCol.setSortable(false);
        currentData.getColumns().addAll(currentProcessNameCol, currentMemoryCol);

        TableColumn loadedData = new TableColumn("Loaded data");
        TableColumn<ProcessesDto, String> loadedProcessNameCol = new TableColumn<>(PROCESS_NAME);
        loadedProcessNameCol.setMinWidth(200);
        loadedProcessNameCol.setCellValueFactory(
                new PropertyValueFactory<ProcessesDto, String>("importedProcessName"));
        loadedProcessNameCol.setSortable(false);

        TableColumn<ProcessesDto, Double> loadedMemoryCol = new TableColumn<>(PROCESS_MEMORY_USED);
        loadedMemoryCol.setMinWidth(200);
        loadedMemoryCol.setCellValueFactory(
                new PropertyValueFactory<ProcessesDto, Double>("importedProcessMemory"));
        loadedMemoryCol.setSortable(false);
        loadedData.getColumns().addAll(loadedProcessNameCol, loadedMemoryCol);

        ObservableList<ComparedInfo> data =
                FXCollections.observableArrayList(comparedInfos);
        table.setRowFactory(tr -> new TableRow<ComparedInfo>() {
            @Override
            public void updateItem(ComparedInfo item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.isLeftRed() || item.isLeftRed()) {
                    setStyle("-fx-background-color: tomato;");
                } else {
                    setStyle("");
                }
            }
        });
        table.getColumns().addAll(currentData, loadedData);
        table.setItems(data);
        return table;
    }

    @Override
    public void initialize(java.net.URL arg0, ResourceBundle arg1) {

        saveReportBt.setDisable(true);
        exportExcel.setDisable(true);
        importAndCompare.setDisable(true);
    }


}