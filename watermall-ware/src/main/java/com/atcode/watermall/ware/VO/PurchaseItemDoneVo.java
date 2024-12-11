package com.atcode.watermall.ware.VO;

import lombok.Data;

@Data
public class PurchaseItemDoneVo {
    private Long itemId;
    private Integer status;
    private String reason;
}
