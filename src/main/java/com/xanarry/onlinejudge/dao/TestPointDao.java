package com.xanarry.onlinejudge.dao;

import com.xanarry.onlinejudge.model.TestPointBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by xanarry on 18-1-5.
 */
public interface TestPointDao {
    int addTestPoint(@Param("testPoint") TestPointBean testPoint);

    void deleteTestPoint(@Param("problemID") int problemID, @Param("testPointID") int testPointID);

    void updateTestPoint(@Param("testPoint") TestPointBean testPoint);

    List<TestPointBean> getTestPointList(@Param("problemID") int problemID);

    TestPointBean getTestPoint(@Param("problemID") int problemID, @Param("testPointID") int testPointID);
}
