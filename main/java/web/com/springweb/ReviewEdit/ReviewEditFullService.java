package web.com.springweb.ReviewEdit;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReviewEditFullService {

    @Value("${review.upload-dir}")
    private String uploadDir;   // 실제 저장 경로 (예: C:/spring_uploads/review/)

    @Value("${review.public-prefix}")
    private String publicPrefix; // 브라우저 접근 prefix (예: /img/review/)

    @Autowired
    private ReviewEditFullDao dao;

    private void assertOwner(int loginUserId, int reviewId) throws IllegalAccessException {
        Integer ownerId = dao.selectOwnerUserId(reviewId);
        if (ownerId == null) throw new IllegalAccessException("존재하지 않는 리뷰");
        if (!ownerId.equals(loginUserId)) throw new IllegalAccessException("권한 없음");
    }

    @Transactional
    public void updateFull(int loginUserId, int reviewId, int rating, String content,
                           List<Integer> deleteFileIds,
                           List<Integer> fileIdForReplace, List<MultipartFile> replaceFiles,
                           List<MultipartFile> newFiles) throws Exception {
        assertOwner(loginUserId, reviewId);

        // 1) 본문/평점 갱신
        if (rating < 0 || rating > 5) rating = Math.max(0, Math.min(5, rating));
        dao.updateReview(reviewId, rating, content);

        // 2) 파일 삭제
        if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
            for (Integer fid : deleteFileIds) {
                String oldName = dao.selectStoredFileName(fid); // DB에 저장된 값 (접근 URL)
                dao.deleteReviewFileById(fid);

                // 물리 파일 삭제
                if (oldName != null) {
                    String physicalPath = uploadDir + "/" + new File(oldName).getName();
                    safeDelete(physicalPath);
                }
            }
        }

        // 3) 파일 교체
        if (fileIdForReplace != null && replaceFiles != null) {
            int n = Math.min(fileIdForReplace.size(), replaceFiles.size());
            for (int i = 0; i < n; i++) {
                Integer fid = fileIdForReplace.get(i);
                MultipartFile mf = replaceFiles.get(i);
                if (mf != null && !mf.isEmpty()) {
                    String oldName = dao.selectStoredFileName(fid);

                    String originalName = mf.getOriginalFilename();
                    String saveName = java.util.UUID.randomUUID() + "_" + originalName;

                    File dest = new File(uploadDir, saveName);
                    mf.transferTo(dest);

                    // DB에는 공개 URL로 저장
                    dao.updateReviewFile(fid, publicPrefix + saveName, originalName);

                    // 기존 물리 파일 삭제
                    if (oldName != null) {
                        String physicalPath = uploadDir + "/" + new File(oldName).getName();
                        safeDelete(physicalPath);
                    }
                }
            }
        }

        // 4) 새 파일 추가
        if (newFiles != null && !newFiles.isEmpty()) {
            for (MultipartFile mf : newFiles) {
                if (mf == null || mf.isEmpty()) continue;

                String originalName = mf.getOriginalFilename();
                String saveName = java.util.UUID.randomUUID() + "_" + originalName;

                File dest = new File(uploadDir, saveName);
                mf.transferTo(dest);

                // DB에는 공개 URL 저장
                dao.insertReviewFile(reviewId, publicPrefix + saveName, originalName);
            }
        }
    }

    private void safeDelete(String path) {
        try {
            File f = new File(path);
            if (f.exists()) f.delete();
        } catch (Exception ignore) {}
    }
}
