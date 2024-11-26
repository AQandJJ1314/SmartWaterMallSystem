package com.atcode.watermall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atcode.common.utils.PageUtils;
import com.atcode.watermall.ware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-26 13:39:03
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

