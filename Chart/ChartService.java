package web.com.springweb.Chart;

import java.text.Normalizer;
import static java.text.Normalizer.Form;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import web.com.springweb.Chart.dto.ChartMonthlyDto;
import web.com.springweb.Chart.dto.ChartYearlyDto;
import web.com.springweb.vo.MonthDaySalesDto;

@Service
public class ChartService {

    @Autowired
    private ChartDao chartDao;

    // 최근 3개월, 월별-요일별 판매 데이터 반환
    public List<MonthDaySalesDto> getRecent3MonthDaySales() {
        List<Map<String, Object>> raw = chartDao.getRecent3MonthDaySales();
        List<MonthDaySalesDto> result = new ArrayList<>();

        for (Map<String, Object> row : raw) {
            MonthDaySalesDto dto = new MonthDaySalesDto();
            dto.setMonth((String) row.get("MONTH"));
            dto.setDow((String) row.get("DOW"));
            dto.setDowName((String) row.get("DOW_NAME"));
            dto.setCount(((Number) row.get("COUNT")).intValue());
            dto.setSales(((Number) row.get("SALES")).longValue());
            result.add(dto);
        }
        return result;
    }

    
    public List<Map<String, Object>> getBrandSalesPie(LocalDate startDate, LocalDate endDate) {
        return chartDao.getBrandSalesPie(startDate, endDate);
    }
    
    // 전체 탈퇴 이유 차트
    public List<Map<String, Object>> getReasonCountAll() {
        return chartDao.getReasonCountAll();
    }
    
    
    private static String norm(String s) {
        if (s == null) return "";
        String t = Normalizer.normalize(s, Form.NFC);  // 한글 정규화
        t = t.replace('\u00A0',' ')                    // NBSP → space
             .replace("\u200B","")                     // zero-width space 제거
             .trim();
        return t;
    }

    // 최근 N주간 집계
    public Map<String, Object> getWithdrawalWeeklyWithForecastPy(int weeks) {
        // 1) 주간 집계
        List<Map<String,Object>> rows = chartDao.getWithdrawalWeekly(weeks);

        // 2) 축 구성 (★ reason/주차 모두 정규화)
        LinkedHashSet<String> weekSet   = new LinkedHashSet<>();
        LinkedHashSet<String> reasonSet = new LinkedHashSet<>();
        for (Map<String,Object> r: rows) {
            weekSet.add(String.valueOf(r.get("WEEK")));              // 주차 포맷은 쿼리에서 통일
            reasonSet.add(norm(String.valueOf(r.get("REASON"))));    // ★
        }
        List<String> weeksList = new ArrayList<>(weekSet);
        List<String> reasons   = new ArrayList<>(reasonSet);

        // 3) reason별 points 생성 (★ 키 정규화)
        Map<String, List<Map<String,Object>>> pointsByReason = new LinkedHashMap<>();
        for (String rs : reasons) pointsByReason.put(rs, new ArrayList<>());

        Map<String, Map<String,Integer>> table = new HashMap<>();
        for (Map<String,Object> r: rows) {
            String w  = String.valueOf(r.get("WEEK"));
            String rs = norm(String.valueOf(r.get("REASON")));       // ★
            int cnt   = ((Number)r.get("CNT")).intValue();
            table.computeIfAbsent(rs, k->new HashMap<>()).put(w, cnt);
        }
        for (String rs : reasons) {
            Map<String,Integer> m = table.getOrDefault(rs, Map.of());
            for (String w : weeksList) {
                pointsByReason.get(rs).add(Map.of("week", w, "count", m.getOrDefault(w,0)));
            }
        }

        // 4) 파이썬 호출 (★ reason 정규화 된 값으로 보냄)
        Map<String,Object> req = Map.of(
            "horizon", 4,
            "series", reasons.stream()
                .map(rs -> Map.of("reason", rs, "points", pointsByReason.get(rs)))
                .toList()
        );

        String baseUrl = pythonBaseUrl;
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String,Object>> entity = new HttpEntity<>(req, headers);

        Map resp;
        try {
            resp = rt.postForObject(baseUrl + "/forecast/withdrawal", entity, Map.class);
        } catch (Exception e) {
            resp = Map.of("horizon", 4, "results",
                    reasons.stream()
                        .map(rs -> Map.of("reason", rs, "forecast", List.of()))
                        .toList());
        }

        // 5) 응답 파싱 (★ reason 키 정규화 + 숫자 안전 변환)
        @SuppressWarnings("unchecked")
        List<Map<String,Object>> results = (List<Map<String,Object>>) resp.get("results");
        List<String> forecastWeeks = new ArrayList<>();
        Map<String, List<Double>> forecast = new LinkedHashMap<>();

        for (Map<String,Object> r : results) {
            String rs = norm(String.valueOf(r.get("reason")));         // ★
            @SuppressWarnings("unchecked")
            List<Map<String,Object>> f = (List<Map<String,Object>>) r.get("forecast");

            List<Double> vals = new ArrayList<>();
            for (Map<String,Object> p : f) {
                String w = String.valueOf(p.get("week"));
                if (forecastWeeks.size() < f.size()) forecastWeeks.add(w);
                Object v = p.get("count");
                double d = (v instanceof Number) ? ((Number) v).doubleValue()
                                                 : Double.parseDouble(String.valueOf(v));
                vals.add(Math.max(0.0, d));                             // 음수 방지(보조)
            }
            forecast.put(rs, vals);
        }

        // 실데이터(★ 동일한 정규화 키로 반환)
        Map<String, List<Integer>> series = new LinkedHashMap<>();
        for (String rs : reasons) {
            List<Integer> seq = new ArrayList<>();
            Map<String,Integer> m = table.getOrDefault(rs, Map.of());
            for (String w : weeksList) seq.add(m.getOrDefault(w,0));
            series.put(rs, seq);
        }

        // (선택) 디버그 로그: 키 매칭 확인
        // System.out.println("[DBG] reasons=" + reasons);
        // System.out.println("[DBG] forecast keys=" + forecast.keySet());

        return Map.of(
            "weeks", weeksList,
            "reasons", reasons,           // 프런트에서 추가 정규화 없이 그대로 사용 가능
            "series", series,
            "forecastWeeks", forecastWeeks,
            "forecast", forecast
        );
    }

    @Value("${python.forecast.base-url:http://localhost:8001}")
    private String pythonBaseUrl;
    
    /* 전체 기간 기타 상세사유 TOP N (최대 50 제한) */
    public List<Map<String, Object>> getEtcReasonsTopAll(int limit) {
        int top = Math.min(Math.max(limit, 1), 50);
        return chartDao.getEtcReasonsTopAll(top);
    }
    
    /* 브랜드별 최근 12개월 판매 건수 */
    public List<ChartMonthlyDto> getMonthlySalesCount(List<String> brandNames) {
        return chartDao.getMonthlySalesCount(brandNames);
    }    
    /* 브랜드 연도별 판매금액 */
    public List<ChartYearlyDto> getYearlySalesAmount(List<String> brandNames) {
        return chartDao.getYearlySalesAmount(brandNames);
    }
}
