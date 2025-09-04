package web.com.springweb.Slide;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.com.springweb.Slide.dto.Slide;

@Service
public class SlideService {
	
	@Autowired
	private SlideDao slideDao;
	
	public List<Slide> getSlidesForHome() {
        return slideDao.selectSlidesForHomeWithBrand();
    }
}
