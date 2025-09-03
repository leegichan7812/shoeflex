package web.com.springweb.AD_User;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.com.springweb.AD_User.dto.Chart;
import web.com.springweb.AD_User.dto.PurchaseHistoryFullChart;
import web.com.springweb.AD_User.dto.Qnaview;
import web.com.springweb.AD_User.dto.ReviewWithFiles;
import web.com.springweb.AD_User.dto.Users;

@Service
public class AD_UserService {

    @Autowired
    private AD_UserDao dao;

    /* QnA 조회 (이메일 기준) */
    public List<Qnaview> getQnaviewList(String email) {
        if (email == null || email.isEmpty()) {
            return new ArrayList<>();
        }
        return dao.getQnaviewList(email);
    }

    /* QnA 등록 */
    public String insertQnaview(Qnaview ins) {
        return dao.insertQnaview(ins) > 0 ? "등록성공" : "등록실패";
    }

    /* 리뷰 조회 (이메일 기준) */
    public List<ReviewWithFiles> getReviewWithFilesList(String email) {
        if (email == null || email.isEmpty()) {
            return new ArrayList<>();
        }
        return dao.getReviewWithFilesList(email);
    }

    /* 회원 정보 조회 (단일) */
    public Users getUsersByEmail(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }
        return dao.getUsersByEmail(email);
    }

    /* 구매 이력 차트 리스트 */
    public List<PurchaseHistoryFullChart> getPurchaseHistoryFullChartList(String email) {
        if (email == null || email.isEmpty()) {
            return new ArrayList<>();
        }
        return dao.getPurchaseHistoryFullChartList(email);
    }

    /* 구매 이력 (Chart DTO 변환) */
    public Chart getPurchaseHistoryFullChart(String email) {
        List<String> mons = new ArrayList<>();
        List<Integer> salesCounts = new ArrayList<>();
        List<Long> monthTots = new ArrayList<>();

        for (Chart c : dao.getPurchaseHistoryFullChart(email)) {
            mons.add(c.getMon());
            salesCounts.add(c.getSalesCount());
            monthTots.add(c.getMonthTot());
        }

        return new Chart(mons, salesCounts, monthTots);
    }

    /* 구매 요약 */
    public List<PurchaseHistoryFullChart> getPurchaseHistorySummary(String email) {
        if (email == null || email.isEmpty()) {
            return new ArrayList<>();
        }
        return dao.getPurchaseHistorySummary(email);
    }
}
