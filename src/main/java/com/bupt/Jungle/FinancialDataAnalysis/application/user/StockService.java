package com.bupt.Jungle.FinancialDataAnalysis.application.user;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.StockMapper;
import com.bupt.Jungle.FinancialDataAnalysis.util.CurrencyUtil;
import com.bupt.Jungle.FinancialDataAnalysis.util.ISOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    private final StockMapper stockMapper;

    @Autowired
    public StockService(StockMapper stockMapper) {
        this.stockMapper = stockMapper;
    }

    public List<RegionBO> getAllMarketRegion() {
        return stockMapper.queryAllMarketRegion()
                .stream()
                .map(isoCode -> RegionBO
                        .builder()
                        .isoCode(isoCode)
                        .simplifiedChineseName(ISOUtil.isoSimplifiedChineseName(isoCode))
                        .englishName(ISOUtil.isoEnglishName(isoCode))
                        .build()
                )
                .toList();
    }

    public List<RegionBO> getAllRegion() {
        return stockMapper.queryAllRegion()
                .stream()
                .map(isoCode -> RegionBO
                        .builder()
                        .isoCode(isoCode)
                        .simplifiedChineseName(ISOUtil.isoSimplifiedChineseName(isoCode))
                        .englishName(ISOUtil.isoEnglishName(isoCode))
                        .build()
                )
                .toList();
    }


    public List<CurrencyBO> getAllCurrency() {
        return stockMapper.queryAllCurrency()
                .stream()
                .map(currencyCode -> CurrencyBO
                        .builder()
                        .currencyCode(currencyCode)
                        .simplifiedChineseName(CurrencyUtil.getSimplifiedCurrencyChineseName(currencyCode))
                        .englishName(CurrencyUtil.getCurrencyEnglishName(currencyCode))
                        .build()
                ).toList();
    }
}
