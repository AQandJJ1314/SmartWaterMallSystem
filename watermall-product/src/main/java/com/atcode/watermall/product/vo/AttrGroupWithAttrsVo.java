package com.atcode.watermall.product.vo;

import com.atcode.watermall.product.entity.AttrEntity;
import com.atcode.watermall.product.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

@Data
public class AttrGroupWithAttrsVo extends AttrGroupEntity {

    private List<AttrEntity> attrs;
}
