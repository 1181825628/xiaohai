package com.lianxi01.xh.dao;

import com.lianxi01.xh.entity.iocn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface incn extends JpaRepository<iocn,String>, JpaSpecificationExecutor<iocn> {


}
