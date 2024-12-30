package com.bupt.Jungle.FinancialDataAnalysis.application.service;

import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.CommoditiesAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.CurrencyAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.RegionAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CommoditiesBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CommoditiesEchartsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CommoditiesTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CommoditiesTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.ServiceException;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.CommoditiesMapper;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.CommoditiesPO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommoditiesService {
    private final CommoditiesMapper commoditiesMapper;

    @Autowired
    public CommoditiesService(CommoditiesMapper commoditiesMapper) {
        this.commoditiesMapper = commoditiesMapper;
    }

    public List<RegionBO> getAllRegion() {
        return commoditiesMapper.queryAllRegion()
                .stream()
                .map(RegionAssembler::buildRegionBOFromISOCode)
                .toList();
    }

    public List<CurrencyBO> getAllCurrency() {
        return commoditiesMapper.queryAllCurrency()
                .stream()
                .map(CurrencyAssembler::buildCurrencyBOFromCurrencyCode)
                .toList();
    }

    public CommoditiesTagPageBO getCommoditiesTagPage(String code,
                                                      String platform,
                                                      String region,
                                                      String currency,
                                                      String name,
                                                      Long current,
                                                      Long pageSize) {
        Long total = commoditiesMapper.queryCommoditiesTagTotalCount(
                code,
                platform,
                region,
                currency,
                name
        );

        long offSet = (current - 1) * pageSize;
        if (offSet > total) {
            offSet = 0;
        }

        List<CommoditiesTagBO> commoditiesTagBOS = commoditiesMapper.queryCommoditiesTag(
                code,
                platform,
                region,
                currency,
                name,
                pageSize,
                offSet
        ).stream().map(CommoditiesAssembler::CommoditiesPO2CommoditiesTagBO).toList();
        return CommoditiesAssembler.buildCommoditiesTagPageBOFromCommoditiesTagBOs(commoditiesTagBOS, total);
    }

    public CommoditiesEchartsBO getCommoditiesEchartsData(String code) {
        List<CommoditiesPO> commoditiesTags = commoditiesMapper.queryCommoditiesTagByCode(code);
        if (CollectionUtils.isEmpty(commoditiesTags)) {
            throw new BusinessException("没有该大宗商品");
        }

        if (commoditiesTags.size() > 1) {
            throw new ServiceException(String.format("数据库数据有问题, 一个code查出来两组TAG, code:%s, tags:%s", code, commoditiesTags));
        }

        CommoditiesTagBO commoditiesTagBO = CommoditiesAssembler.CommoditiesPO2CommoditiesTagBO(commoditiesTags.get(0));
        List<CommoditiesBO> commoditiesBOS = commoditiesMapper.queryCommoditiesDataByCode(code).stream().map(CommoditiesAssembler::CommoditiesPO2CommoditiesBO).toList();
        return CommoditiesAssembler.buildCommoditiesEchartsBOFromCommoditiesBOsAndCommoditiesTagBO(commoditiesBOS, commoditiesTagBO);
    }
}
