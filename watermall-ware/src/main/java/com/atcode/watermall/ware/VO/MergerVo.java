package com.atcode.watermall.ware.VO;

import lombok.Data;

import java.util.List;

@Data
public class MergerVo {
    private Long purchaseId; //整单id
    private List<Long> items; //合并项集合
}
