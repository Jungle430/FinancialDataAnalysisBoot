package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper;

import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.CommoditiesPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommoditiesMapper {
    List<String> queryAllRegion();

    List<String> queryAllCurrency();

    List<CommoditiesPO> queryCommoditiesDataByCode(@Param("code") String code);

    List<CommoditiesPO> queryCommoditiesTagByCode(@Param("code") String code);

    List<CommoditiesPO> queryCommoditiesTag(
            @Param("code") String code,
            @Param("platform") String platform,
            @Param("region") String region,
            @Param("currency") String currency,
            @Param("name") String name,
            @Param("limitSize") Long limitSize,
            @Param("offSet") Long offSet
    );

    Long queryCommoditiesTagTotalCount(
            @Param("code") String code,
            @Param("platform") String platform,
            @Param("region") String region,
            @Param("currency") String currency,
            @Param("name") String name
    );
}
