package org.demofx.report.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.demofx.dto.ProcessesDto;
import org.demofx.report.ReportGenerator;

import javax.inject.Named;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Named
public class ExcelReportGeneratorImpl implements ReportGenerator {


    /**
     * Generating report into excel file. Adding bar chart.
     *
     * @param processesDtos list of processes
     * @param filePath      filename
     */
    @Override
    public void generateReport(List<ProcessesDto> processesDtos, String filePath) throws IOException {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("MemoryUsage");

        Row row;
        Cell cell;

        row = sheet.createRow(0);
        row.createCell(0).setCellValue("Process name");
        row.createCell(1).setCellValue("Memory used Kb");

        int i = 1;
        for (ProcessesDto processesDto : processesDtos) {
            row = sheet.createRow(i++);
            cell = row.createCell(0);
            cell.setCellValue(processesDto.getName());
            cell = row.createCell(1);
            cell.setCellValue(processesDto.getMemory());

        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);


        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = drawing.createAnchor(3, 3, 3, 3, 3, 4, processesDtos.size(), processesDtos.size());

        Chart chart = drawing.createChart(anchor);

        CTChart ctChart = ((XSSFChart) chart).getCTChart();
        CTPlotArea ctPlotArea = ctChart.getPlotArea();
        CTBarChart ctBarChart = ctPlotArea.addNewBarChart();
        CTBoolean ctBoolean = ctBarChart.addNewVaryColors();
        ctBoolean.setVal(true);
        ctBarChart.addNewBarDir().setVal(STBarDir.COL);

        for (int r = 2; r < processesDtos.size(); r++) {
            CTBarSer ctBarSer = ctBarChart.addNewSer();

            CTSerTx ctSerTx = ctBarSer.addNewTx();
            CTStrRef ctStrRef = ctSerTx.addNewStrRef();
            ctStrRef.setF("MemoryUsage!$A$" + r);
            ctBarSer.addNewIdx().setVal(r - 2);
            CTAxDataSource cttAxDataSource = ctBarSer.addNewCat();
            ctStrRef = cttAxDataSource.addNewStrRef();
            ctStrRef.setF("MemoryUsage!$B$1");
            CTNumDataSource ctNumDataSource = ctBarSer.addNewVal();
            CTNumRef ctNumRef = ctNumDataSource.addNewNumRef();
            ctNumRef.setF("MemoryUsage!$B$" + r);

            //at least the border lines in Libreoffice Calc ;-)
            ctBarSer.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(new byte[]{0, 0, 0});
            byte[] values = new byte[]{(byte) (130 + 40 * r), (byte) (90 + 40 * r), (byte) (190 + 10 * r), (byte) (190 + 80 * r)};
            ctBarSer.addNewSpPr().addNewSolidFill().addNewSrgbClr().setVal(values);
        }

        //telling the BarChart that it has axes and giving them Ids
        ctBarChart.addNewAxId().setVal(123456);
        ctBarChart.addNewAxId().setVal(123457);

        //cat axis
        CTCatAx ctCatAx = ctPlotArea.addNewCatAx();
        ctCatAx.addNewAxId().setVal(123456); //id of the cat axis
        CTScaling ctScaling = ctCatAx.addNewScaling();
        ctScaling.addNewOrientation().setVal(STOrientation.MIN_MAX);
        ctCatAx.addNewDelete().setVal(false);
        ctCatAx.addNewAxPos().setVal(STAxPos.B);
        ctCatAx.addNewCrossAx().setVal(123457); //id of the val axis
        ctCatAx.addNewTickLblPos().setVal(STTickLblPos.NEXT_TO);

        //val axis
        CTValAx ctValAx = ctPlotArea.addNewValAx();
        ctValAx.addNewAxId().setVal(123457); //id of the val axis
        ctScaling = ctValAx.addNewScaling();
        ctScaling.addNewOrientation().setVal(STOrientation.MIN_MAX);
        ctValAx.addNewDelete().setVal(false);
        ctValAx.addNewAxPos().setVal(STAxPos.L);
        ctValAx.addNewCrossAx().setVal(123456); //id of the cat axis
        ctValAx.addNewTickLblPos().setVal(STTickLblPos.NEXT_TO);

        //legend
        CTLegend ctLegend = ctChart.addNewLegend();
        ctLegend.addNewLegendPos().setVal(STLegendPos.B);
        ctLegend.addNewOverlay().setVal(false);

        FileOutputStream fileOut = new FileOutputStream(filePath);

        wb.write(fileOut);
        fileOut.close();


    }
}
