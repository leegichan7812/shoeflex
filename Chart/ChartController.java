package web.com.springweb.Chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import web.com.springweb.Chart.dto.ChartMonthlyDto;
import web.com.springweb.Chart.dto.ChartYearlyDto;
import web.com.springweb.vo.MonthDaySalesDto;
import web.com.springweb.vo.QuarterUtils;

@RestController
@RequestMapping("/api/chart")
public class ChartController {

    @Autowired
    private ChartService chartService;

    /**
     * 📊 전월/당월 요일별 매출/판매건수 API
     * - 반환 예시: {
     *     "thisMonth": [월~일 순 DaySalesDto 7개],
     *     "lastMonth": [월~일 순 DaySalesDto 7개]
     *   }
     */
    @GetMapping("/recent-3month-daysales")
    public List<MonthDaySalesDto> getRecent3MonthDaySales() {
        return chartService.getRecent3MonthDaySales();
    }
    
    @GetMapping("/brand-sales-pie")
    public List<Map<String, Object>> getBrandSalesPie(@RequestParam int quarter) {
        // 분기별 연도, 시작일/종료일 계산
        QuarterUtils.QuarterInfo q = QuarterUtils.getQuarterInfo(quarter);
        // 실제 서비스 호출
        return chartService.getBrandSalesPie(q.startDate, q.endDate);
    }
    
    // 탈퇴 이유 전체 차트
    @GetMapping("/stats/withdrawal/reason-pie")
    public ResponseEntity<?> getReasonPie() {
        List<Map<String, Object>> rows = chartService.getReasonCountAll();

        List<Map<String, Object>> series = new ArrayList<>();
        for (Map<String, Object> r : rows) {
            String name = String.valueOf(r.getOrDefault("REASON_TEXT", r.getOrDefault("reason_text", "")));
            Number val  = (Number) r.getOrDefault("CNT", r.get("cnt"));
            series.add(Map.of("name", name, "value", val == null ? 0 : val.intValue()));
        }
        return ResponseEntity.ok(series);
    }
    // 최근 N주간 집계
    @GetMapping("/stats/withdrawal/weekly-forecast")
    public ResponseEntity<?> getWithdrawalWeeklyForecast(
            @RequestParam(defaultValue = "12") int weeks) {
        return ResponseEntity.ok(chartService.getWithdrawalWeeklyWithForecastPy(weeks));
    }
    
    /* 전체 기간 기타 상세사유 TOP N (ECharts name/value 포맷) */
    @GetMapping("/stats/withdrawal/others/top-all")
    public List<Map<String,Object>> getEtcTopAll(@RequestParam(defaultValue = "5") int limit) {
        var rows = chartService.getEtcReasonsTopAll(limit);
        var out = new ArrayList<Map<String,Object>>();
        for (var r: rows) {
            String name = String.valueOf(r.getOrDefault("ETC_TEXT", r.get("etc_text")));
            Number cnt  = (Number) r.getOrDefault("CNT", r.get("cnt"));
            out.add(Map.of("name", name, "value", cnt == null ? 0 : cnt.intValue()));
        }
        return out;
    }
    
    /* 브랜드별 최근 12개월 판매 건수 */
    @GetMapping("/brand/monthly")
    public List<ChartMonthlyDto> getMonthlySalesCount(
            @RequestParam(value = "brands", required = false) List<String> brands) {
        return chartService.getMonthlySalesCount(brands);
    }
    /* 브랜드 연도별 판매금액 */
    @GetMapping("/brand/yearly")
    public List<ChartYearlyDto> getYearlySalesAmount(
            @RequestParam(value = "brands", required = false) List<String> brands) {
        return chartService.getYearlySalesAmount(brands);
    }

}
