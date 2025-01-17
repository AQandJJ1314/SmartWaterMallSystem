package com.atcode.watermall.member.service;

import com.atcode.watermall.member.exception.PhoneExistException;
import com.atcode.watermall.member.exception.UserNameExistException;
import com.atcode.watermall.member.vo.MemberLoginVo;
import com.atcode.watermall.member.vo.MemberRegistVo;
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

    /**
     * 注册用户
     * @param vo
     */
    void regist(MemberRegistVo vo);

    /**
     * 校验手机号唯一性
     *
     * @param phone
     * @throws PhoneExistException
     */
    void checkPhoneUnique(String phone) throws PhoneExistException;

    /**
     * 校验用户名唯一性
     *
     * @param userName
     * @throws UserNameExistException
     */
    void checkUserNameUnique(String userName) throws UserNameExistException;

    MemberEntity login(MemberLoginVo vo);
}

