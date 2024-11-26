package com.atcode.watermall.member.dao;

import com.atcode.watermall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-26 10:53:20
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
