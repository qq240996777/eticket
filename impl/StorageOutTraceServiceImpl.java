package com.jyrh.repex.wms.storageout.trace.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jyrh.basic.base.utils.ConstantRepex;
import com.jyrh.basic.base.utils.DateUtil;
import com.jyrh.basic.base.utils.Pager;
import com.jyrh.repex.config.basedata.dictionary.service.DictionaryService;
import com.jyrh.repex.wms.storageout.stockout.dao.TtStockOutDetailDao;
import com.jyrh.repex.wms.storageout.stockout.pojo.TtStockOutDetail;
import com.jyrh.repex.wms.storageout.trace.dao.StorageOutTraceDao;
import com.jyrh.repex.wms.storageout.trace.dto.StorageOutTraceDto;
import com.jyrh.repex.wms.storageout.trace.service.StorageOutTraceService;

@Service
public class StorageOutTraceServiceImpl implements StorageOutTraceService {

	@Resource
	private StorageOutTraceDao storageOutTraceDao;
	@Resource
	private DictionaryService dictionaryService;
	@Resource
	private TtStockOutDetailDao stockOutDetailDao;

	@Override
	public List<StorageOutTraceDto> findStorageOutTraceList(StorageOutTraceDto storageOutTraceDto, Pager pager, String orderField, String order) {
		List<StorageOutTraceDto> result = new ArrayList<StorageOutTraceDto>();
		List<Object> stockOutList = storageOutTraceDao.findStockOutDetailList(storageOutTraceDto, pager, orderField, order);
		for (Object object : stockOutList) {
			Object[] stockOutDetailItems = (Object[]) object;
			Long stockOutDetailId = ((BigDecimal) stockOutDetailItems[0]).longValue();
			List<Object> traceList = storageOutTraceDao.findStorageOutTraceList(stockOutDetailId, storageOutTraceDto);
			for (Object object1 : traceList) {
				Object[] traceItems = (Object[]) object1;
				StorageOutTraceDto dto = new StorageOutTraceDto();
				dto.setStockOutDate(DateUtil.date2string((Date) stockOutDetailItems[1]));// 发货日期
				dto.setStockOutNo((String) stockOutDetailItems[2]); // 发货单号
				dto.setCustomer((String) stockOutDetailItems[3]); // 客户
				dto.setProvider((String) stockOutDetailItems[4]);// 供应商
				dto.setProductCode((String) stockOutDetailItems[5]); // 商品编号
				dto.setProductName((String) stockOutDetailItems[6]); // 品名
				dto.setProductState(dictionaryService.getNameByKeyTypeName(((Character) stockOutDetailItems[7]).toString(), ConstantRepex.PRODUCT_STATE)); // 商品状态
				dto.setProductUnit((String) stockOutDetailItems[8]); // 单位
				dto.setStockOutQuantity(((BigDecimal) stockOutDetailItems[9]).longValue()); // 发货数量
				dto.setStockType(dictionaryService.getNameByKeyTypeName(((Character) stockOutDetailItems[10]).toString(), ConstantRepex.STOCKOUT_TYPE));
				dto.setCheckNo((String) stockOutDetailItems[11]); // 对账单号
//				dto.setTraceStatus((null == stockOutDetailItems[12]) ? "0" : ((Character) stockOutDetailItems[12]).toString());
				dto.setTraceStatus((null == stockOutDetailItems[12]) ? "0" : (stockOutDetailItems[12]).toString());
				dto.setFrameOutNo((String) traceItems[0]); // 拣货单号
				dto.setFrameOutDate(DateUtil.date2string((Date) traceItems[1])); // 拣货日期
				dto.setFrameOutQuantity((null == traceItems[2]) ? null : ((BigDecimal) traceItems[2]).longValue()); // 拣货数量
				dto.setStorageLocNo((String) traceItems[3]); // 库位
				dto.setStorage((String) traceItems[4]); // 仓库
				dto.setStorageOutNo((String) traceItems[5]);// 出库单号
				dto.setStorageOutDate(DateUtil.date2string((Date) traceItems[6])); // 出库日期
				dto.setQuantity((null == traceItems[7]) ? null : ((BigDecimal) traceItems[7]).longValue()); // 出库数量
				if (null == traceItems[30]) {
					dto.setTrayNum(null);
				} else {					
					dto.setTrayNum(storageOutTraceDao.findRealTrayNum(((BigDecimal) traceItems[30]).longValue())); // 出库托数
				}
				dto.setWeight((null == traceItems[9]) ? null : ((BigDecimal) traceItems[9]).doubleValue()); // 上架重量
				dto.setArea((null == traceItems[10]) ? null : ((BigDecimal) traceItems[10]).doubleValue()); // 上架面积
				dto.setBul((null == traceItems[11]) ? null : ((BigDecimal) traceItems[11]).doubleValue()); // 上架体积
				dto.setIntoNum((null == traceItems[12]) ? null : ((BigDecimal) traceItems[12]).longValue()); // 上架堆码数
				dto.setNum((null == traceItems[13]) ? null : ((BigDecimal) traceItems[13]).longValue()); // 件数
				dto.setBatch((String) traceItems[14]); // 批次
				dto.setUnitPrice((null == traceItems[15]) ? null : ((BigDecimal) traceItems[15]).doubleValue()); // 单价
				dto.setProductDate(DateUtil.date2string((Date) traceItems[16])); // 生产日期
				dto.setExpirationDate(DateUtil.date2string((Date) traceItems[17])); // 到期日期
				dto.setGuaranteePeriod((null == traceItems[18]) ? null : ((BigDecimal) traceItems[18]).longValue()); // 保质期(单位：天)
				if (null != traceItems[19]) {
					dto.setIsQuarantine(dictionaryService.getNameByKeyTypeName((String) traceItems[19], ConstantRepex.IS_QUARANTINE)); // 检验检疫情况
				}
				dto.setQuarantineNo((String) traceItems[20]); // 检疫证号
//				dto.setQuarantineWeight((String) traceItems[21]); // 检疫证（商品重量）
				dto.setQuarantineWeight((traceItems[21]==null? "" :traceItems[21]) + "");
				dto.setTemperature((null == traceItems[22]) ? null : ((BigDecimal) traceItems[22]).doubleValue()); // 温度
				dto.setRemark((String) traceItems[23]); // 备注
				dto.setIsFormDcc((null == traceItems[24]) ? false : Boolean.valueOf(((Character) traceItems[24]).toString())); // 数据是否来自DCC
				dto.setProducingArea((String) traceItems[25]); // 产地
				dto.setOuterStorage((String) traceItems[26]); // 外库

				dto.setId(stockOutDetailId);
				dto.setStockMerge(stockOutDetailId.toString());
				dto.setFrameOutMerge(dto.getStockMerge() + "_" + ((null == traceItems[27]) ? "" : ((BigDecimal) traceItems[27]).toString()));
				dto.setStorageMerge(dto.getFrameOutMerge() + "_" + ((null == traceItems[28]) ? "" : ((BigDecimal) traceItems[28]).toString()));

				System.out.println("[" + dto.getStockMerge() + "][" + dto.getFrameOutMerge() + "][" + dto.getStorageMerge() + "]");

				result.add(dto);
			}
		}

		return result;
	}

	@Override
	public void txSetTraceStatus(List<Long> idsList, String status) {
		for (Long id : idsList) {
			TtStockOutDetail stockOutDetail = stockOutDetailDao.find(TtStockOutDetail.class, id);
			stockOutDetail.setTraceStatus(status);
			stockOutDetailDao.save(stockOutDetail);
		}

	}

}
