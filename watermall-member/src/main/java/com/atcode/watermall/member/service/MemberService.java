package com.atcode.watermall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atcode.common.utils.PageUtils;
import com.atcode.watermall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-26 10:53:20
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

