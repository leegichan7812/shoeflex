package web.com.springweb.AD_Brand;

import java.util.List;

import org.springframework.stereotype.Service;

import web.com.springweb.AD_Brand.dto.Brand;

@Service
public class BrandService {
    private final BrandDao dao;
    public BrandService(BrandDao dao){ this.dao = dao; }

    public List<Brand> list(){ return dao.getBrandList(); }

    public boolean updateStatus(int brandId, String status){
        return dao.updateStatus(brandId, status) > 0;
    }

    public boolean logicalDelete(int brandId){
        return dao.logicalDelete(brandId) > 0;
    }
}