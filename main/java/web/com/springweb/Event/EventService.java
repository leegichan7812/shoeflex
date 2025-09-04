package web.com.springweb.Event;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import web.com.springweb.Event.dto.Event;

@Service
public class EventService {

    private final EventDao eventDao;
    
    // 외부 디렉토리 (예: C:/spring_uploads/slide) → WebConfig에서 /img/slide/** 로 매핑
    @Value("${slide.upload-dir}")
    private String uploadDir;

    // DB에 저장할 공개 URL prefix
    @Value("${slide.public-prefix:/img/slide/}")
    private String publicPrefix;

    public EventService(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public List<Event> getUpcomingAll() {
        return eventDao.selectUpcomingAll();
    }

    /** 브랜드/타이틀/이미지 동시 업서트 */
    public String upsertBrandAndSlide(int calendarId,
                                      Integer brandId,
                                      String brandName,
                                      String slideTitle,
                                      MultipartFile slideImg) {

        // 🔧 문자열 정리(공백만 오면 null) — NVL 로직과 궁합 맞춤
        if (brandName != null) {
            brandName = brandName.trim();
            if (brandName.isEmpty()) brandName = null;
        }
        if (slideTitle != null) {
            slideTitle = slideTitle.trim();
            if (slideTitle.isEmpty()) slideTitle = null; // "" → NULL (Oracle에서는 원래 ""도 NULL 취급)
        }

        String imagePublicPath = null;

        // 파일 있으면 저장
        if (slideImg != null && !slideImg.isEmpty()) {
            String saved = saveFile(slideImg, "ev_" + calendarId);
            imagePublicPath = joinPublicPath(saved);
        }

        // ✅ 파일 없어도 항상 UPDATE 호출 (타이틀만 변경 가능)
        int updated = eventDao.updateBrandAndSlide(calendarId, brandId, brandName, slideTitle, imagePublicPath);
        System.out.println("[updateBrandAndSlide] calendarId=" + calendarId + ", updated=" + updated);

        // 디버깅: 저장 후 값 확인
        try {
            Event after = eventDao.selectById(calendarId);
            if (after != null) {
                System.out.println("[after] slideTitle=" + after.getSlideTitle()
                        + ", slideImage=" + after.getSlideImage()
                        + ", brandId=" + after.getBrandId()
                        + ", brandName=" + after.getBrandName());
            }
        } catch (Exception ignore) {}

        return imagePublicPath;
    }


    /** 이미지 삭제 (파일도 삭제) */
    public void deleteSlide(int calendarId) {
        String curPath = eventDao.selectSlideImage(calendarId); // /img/slide/xxx.png
        if (curPath != null && !curPath.isBlank()) {
            deleteFileIfExists(extractFileName(curPath));
        }
        eventDao.clearSlide(calendarId);
    }

    // ===== 파일 유틸 =====
    private String saveFile(MultipartFile file, String prefix) {
        try {
            Files.createDirectories(Path.of(uploadDir));
            String ext = getExt(file.getOriginalFilename());
            String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String filename = prefix + "_" + stamp + (ext.isEmpty() ? "" : "." + ext);
            Path target = Path.of(uploadDir, filename);
            file.transferTo(target.toFile());
            return filename;
        } catch (Exception e) {
            throw new RuntimeException("슬라이드 이미지 저장 실패", e);
        }
    }
    private String joinPublicPath(String filename) {
        String p = (publicPrefix == null || publicPrefix.isBlank()) ? "/img/slide/" : publicPrefix;
        if (!p.endsWith("/")) p += "/";
        return p + filename;
    }
    private String extractFileName(String publicPath) {
        int idx = publicPath.lastIndexOf('/');
        return (idx >= 0) ? publicPath.substring(idx + 1) : publicPath;
    }
    private void deleteFileIfExists(String filename) {
        try { Files.deleteIfExists(Path.of(uploadDir, filename)); } catch (Exception ignore) {}
    }
    private String getExt(String name) {
        if (name == null) return "";
        int i = name.lastIndexOf('.');
        return (i >= 0) ? name.substring(i + 1) : "";
    }
}
