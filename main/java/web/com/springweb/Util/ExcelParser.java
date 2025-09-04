package web.com.springweb.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import web.com.springweb.vo.ProductExcelDto;
import web.com.springweb.vo.Products;

public class ExcelParser {

    public static List<ProductExcelDto> parseProductColorSizeExcel(MultipartFile file) throws IOException {

        List<ProductExcelDto> result = new ArrayList<>();

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            ProductExcelDto dto = new ProductExcelDto();

            Products product = new Products();
            product.setName(getCellValue(row.getCell(0)));
            product.setDescription(getCellValue(row.getCell(1)));
            product.setPrice(Double.parseDouble(getCellValue(row.getCell(2))));
            product.setBrandId(Integer.parseInt(getCellValue(row.getCell(3))));
            product.setCategoryId(Integer.parseInt(getCellValue(row.getCell(4))));
            product.setImageUrl(getCellValue(row.getCell(5)));

            int colorId = Integer.parseInt(getCellValue(row.getCell(6)));

            Map<Integer, Integer> sizeStockMap = new HashMap<>();
            int sizeStartCol = 7;
            int sizeCount = 13; // 220~280

            for (int j = 0; j < sizeCount; j++) {
                String stockStr = getCellValue(row.getCell(sizeStartCol + j));
                if (!stockStr.isEmpty()) {
                    int stock = Integer.parseInt(stockStr);
                    if (stock > 0) {
                        sizeStockMap.put(j + 1, stock); // sizeId는 1~13
                    }
                }
            }

            dto.setProduct(product);
            dto.setColorId(colorId);
            dto.setSizeStockMap(sizeStockMap);

            result.add(dto);
        }

        workbook.close();
        return result;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue().trim();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((int) cell.getNumericCellValue());
        return "";
    }

}
