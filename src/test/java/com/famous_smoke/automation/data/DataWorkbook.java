package com.famous_smoke.automation.data;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.famous_smoke.automation.data.BasePageData.BREADCRUMBS_SEPARATOR;


/**
 * Created by jorge on 22-12-2015.
 */
public final class DataWorkbook {

    private static final String DEFAULT_WORKBOOK_FOLDER  = "src/test/resources/testData/";
    private static final String DEFAULT_WORKBOOK_NAME    = "TestData-seleniumframework.xlsx";
    private static final String DATA_SHEET_NAME          = "TestData";
    private static final int    HEADER_ROW               = 0;
    private static final String URL_HEADER               = BrandPageData.URL_FIELD_NAME;
    private static final String CANONICAL_HEADER         = BrandPageData.CANONICAL_FIELD_NAME;
    private static final String TITLE_HEADER             = BrandPageData.TITLE_FIELD_NAME;
    private static final String META_DESCRIPTION_HEADER  = BrandPageData.METADESCRIPTION_FIELD_NAME;
    private static final String HEADER1_HEADER           = BrandPageData.HEADER1_FIELD_NAME;
    private static final String DESCRIPTION_HEADER       = BrandPageData.DESCRIPTION_FIELD_NAME;
    private static final String BREADCRUMBS_TEXT_HEADER  = BrandPageData.BREADCRUMBS_TEXT_FIELD_NAME;
    private static final String BREADCRUMBS_LINKS_HEADER = BrandPageData.BREADCRUMBS_FIELD_NAME;
    private static final String IDENTIFIED_HEADER        = BrandPageData.IDENTIFIED_FIELD_NAME;
    private static final int    URL_COLUMN               = 0;
    private static final int    CANONICAL_COLUMN         = 1;
    private static final int    TITLE_COLUMN             = 2;
    private static final int    META_DESCRIPTION_COLUMN  = 3;
    private static final int    HEADER1_COLUMN           = 4;
    private static final int    DESCRIPTION_COLUMN       = 5;
    private static final int    BREADCRUMBS_TEXT_COLUMN  = 6;
    private static final int    BREADCRUMBS_LINKS_COLUMN = 7;
    private static final int    IDENTIFIED_COLUMN        = 8;

    private final String location;

    private DataWorkbook(final String location) {
        this.location = location;
    }

    public static DataWorkbook getDefaultWorkbook() {
        return new DataWorkbook(DEFAULT_WORKBOOK_FOLDER + DEFAULT_WORKBOOK_NAME);
    }

    public DataWorkbook writeBrandPages(final Collection<BrandPageData> datas) throws IOException {
        Workbook workbook = openWorkBook(location);
        Sheet sheet = createDataSheet(workbook);

        int row = 1;
        for (BrandPageData data : datas) {
            Row brandRow = sheet.createRow(row);

            CellStyle dataStyle = createDataStyle(workbook);
            getDataMap(data).forEach((column, value) -> {
                Cell cell = createCell(brandRow, column, dataStyle);
                cell.setCellValue(value);
                sheet.autoSizeColumn(cell.getColumnIndex());
            });

            ++row;
        }

        writeWorkBook(workbook, location);
        workbook.close();
        return this;
    }

    public Collection<BrandPageData> readBrands() throws IOException {
        HashSet<BrandPageData> brands = new HashSet<>();

        Workbook workbook = openWorkBook(location);

        Sheet sheet = getDataSheet(workbook);
        for (int row = 1; row <= sheet.getLastRowNum(); ++row) {
            Row dataRow = sheet.getRow(row);

            String url = getCellValue(dataRow, URL_COLUMN);
            String canonical = getCellValue(dataRow, CANONICAL_COLUMN);
            String title = getCellValue(dataRow, TITLE_COLUMN);
            String metaDescription = getCellValue(dataRow, META_DESCRIPTION_COLUMN);
            String header1 = getCellValue(dataRow, HEADER1_COLUMN);
            String description = getCellValue(dataRow, DESCRIPTION_COLUMN);
            String breadcrumbsText = getCellValue(dataRow, BREADCRUMBS_TEXT_COLUMN);
            String[] breadcrumbs = getCellValue(dataRow, BREADCRUMBS_LINKS_COLUMN).split(BREADCRUMBS_SEPARATOR);
            String identified = getCellValue(dataRow, IDENTIFIED_COLUMN);

            brands.add(PageDataFactory.createBrand(
                    PageDataFactory.createPage(
                            url, title, canonical,
                            metaDescription, breadcrumbsText,
                            Arrays.asList(breadcrumbs)),
                    header1,
                    description,
                    Boolean.valueOf(identified)));
        }
        return brands;
    }

    private Workbook openWorkBook(final String location) throws IOException {
        File dataFile = new File(location);
        FileInputStream inputStream = new FileInputStream(dataFile);
        return new XSSFWorkbook(inputStream);
    }

    private Workbook writeWorkBook(final Workbook workbook,
                                   final String location) throws IOException {
        File dataFile = new File(location);
        FileOutputStream outputStream = new FileOutputStream(dataFile);
        workbook.write(outputStream);
        return workbook;
    }

    private Sheet getDataSheet(final Workbook workbook) {
        return workbook.getSheet(DATA_SHEET_NAME);
    }

    private Sheet createDataSheet(final Workbook workbook) {
        if (getDataSheet(workbook) != null) {
            deleteSheet(workbook, getDataSheet(workbook));
        }
        Sheet sheet = workbook.createSheet(DATA_SHEET_NAME);
        Row header = sheet.createRow(HEADER_ROW);
        CellStyle style = createHeaderStyle(workbook);

        getHeaderMap().forEach((column, value) -> {
            Cell cell = createCell(header, column, style);
            cell.setCellValue(value);
            sheet.autoSizeColumn(cell.getColumnIndex());
        });

        return sheet;
    }

    private Map<Integer, String> getHeaderMap() {
        return Arrays.stream(new Object[][] {
                {URL_COLUMN, URL_HEADER},
                {CANONICAL_COLUMN, CANONICAL_HEADER},
                {TITLE_COLUMN, TITLE_HEADER},
                {META_DESCRIPTION_COLUMN, META_DESCRIPTION_HEADER},
                {HEADER1_COLUMN, HEADER1_HEADER},
                {DESCRIPTION_COLUMN, DESCRIPTION_HEADER},
                {BREADCRUMBS_TEXT_COLUMN, BREADCRUMBS_TEXT_HEADER},
                {BREADCRUMBS_LINKS_COLUMN, BREADCRUMBS_LINKS_HEADER},
                {IDENTIFIED_COLUMN, IDENTIFIED_HEADER}
        }).collect(Collectors.toMap(kv -> (Integer) kv[0], kv -> (String) kv[1]));
    }

    private Map<Integer, String> getDataMap(final BrandPageData data) {
        return Arrays.stream(new Object[][] {
                {URL_COLUMN, data.getURL()},
                {CANONICAL_COLUMN, data.getCanonical()},
                {TITLE_COLUMN, data.getTitle()},
                {META_DESCRIPTION_COLUMN, data.getMetaDescription()},
                {HEADER1_COLUMN, data.getHeader1()},
                {DESCRIPTION_COLUMN, data.getDescription()},
                {BREADCRUMBS_TEXT_COLUMN, data.getBreadcrumbsText()},
                {BREADCRUMBS_LINKS_COLUMN, getBreadcrumbs(data.getBreadcrumbs())},
                {IDENTIFIED_COLUMN, data.isIdentified().toString()}
        }).collect(Collectors.toMap(kv -> (Integer) kv[0], kv -> (String) kv[1]));
    }

    private void deleteSheet(final Workbook workbook,
                             final Sheet sheet) {
        workbook.removeSheetAt(workbook.getSheetIndex(sheet));
    }

    private CellStyle createHeaderStyle(final Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(HSSFColor.AQUA.index);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        return style;
    }

    private CellStyle createDataStyle(final Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setWrapText(true);
        return style;
    }

    private Cell createCell(final Row row, final int column, final CellStyle style){
        Cell cell = row.createCell(column);
        cell.setCellStyle(style);
        return cell;
    }

    private String getBreadcrumbs(final Collection<String> breadcrumbs) {
        StringBuilder builder = new StringBuilder();
        if (!breadcrumbs.isEmpty()) {
            for (String link : breadcrumbs){
                builder.append(link);
                builder.append(BREADCRUMBS_SEPARATOR);
            }
            builder.deleteCharAt(builder.lastIndexOf(BREADCRUMBS_SEPARATOR));
        }
        return builder.toString();
    }

    private String getCellValue(final Row row, final int column) {
        return row.getCell(column, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
    }

}