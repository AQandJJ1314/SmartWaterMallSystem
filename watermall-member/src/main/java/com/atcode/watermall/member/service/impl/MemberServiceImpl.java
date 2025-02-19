package com.atcode.watermall.member.service.impl;

import com.atcode.watermall.member.dao.MemberLevelDao;
import com.atcode.watermall.member.entity.MemberLevelEntity;
import com.atcode.watermall.member.exception.PhoneExistException;
import com.atcode.watermall.member.exception.UserNameExistException;
import com.atcode.watermall.member.vo.MemberLoginVo;
import com.atcode.watermall.member.vo.MemberRegistVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atcode.common.utils.PageUtils;
import com.atcode.common.utils.Query;

import com.atcode.watermall.member.dao.MemberDao;
import com.atcode.watermall.member.entity.MemberEntity;
import com.atcode.watermall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void regist(MemberRegistVo vo) {
        MemberDao memberDao = this.baseMapper;
        MemberEntity entity = new MemberEntity();
        // 设置默认等级
        MemberLevelEntity levelEntity = memberLevelDao.getDefaultLevel();
        entity.setLevelId(levelEntity.getId());

        // 检查用户名和手机号是否唯一。为了让controller能感知异常，异常机制
        checkPhoneUnique(vo.getPhone());
        checkUserNameUnique(vo.getUserName());

        entity.setMobile(vo.getPhone());
        entity.setUsername(vo.getUserName());
        // 密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(vo.getPassword());
        entity.setPassword(encode);
        memberDao.insert(entity);
    }

    @Override
    public void checkPhoneUnique(String phone) throws PhoneExistException {
        MemberDao memberDao = this.baseMapper;
//        Integer count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        Long count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (count > 0){
            throw new PhoneExistException();
        }
    }

    @Override
    public void checkUserNameUnique(String userName) throws UserNameExistException {
        MemberDao memberDao = this.baseMapper;
        //TODO 后续查看数据类型Long 或者 Integer
//        Integer count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("username", userName));
        Long count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("username", userName));
        if (count > 0){
            throw new UserNameExistException();
        }
    }

    @Override
    public MemberEntity login(MemberLoginVo vo) {
        String loginAccount = vo.getLoginAccount();
        String password = vo.getPassword();
        // 去数据库查询
        MemberDao memberDao = this.baseMapper;
        MemberEntity memberEntity = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("username", loginAccount).or().eq("mobile", loginAccount));
        if (memberEntity == null){
            // 登录失败
            return null;
        }else {
            // 1、获取到数据库的password
            String passwordDB = memberEntity.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            // 2、密码匹配
            //TODO 密码匹配规则，这里只做测试用equals  测试完成
            boolean matches = passwordEncoder.matches(password, passwordDB);
//            boolean matches = password.equals(passwordDB) ? true : false;

            if (matches){
                memberEntity.setPassword("");
                return memberEntity;
            }else {
                return null;
            }
        }

    }
}
