


/**
* 接口防刷
*
* 由于发送验证码的接口暴露，为了防止恶意攻击，我们不能随意让接口被调用。
*
* 在redis中以phone-code将电话号码和验证码进行存储并将当前时间与code一起存储
* 如果调用时以当前phone取出的值不为空且当前时间在存储时间的60s以内，说明60s内该号码已经调用过，返回错误信息
* 60s以后再次调用，需要删除之前存储的phone-code
* code存在一个过期时间，我们设置为10min，10min内验证该验证码有效
  */  


位置：com.atcode.watermall.vo.UserRegistVo

    /**
     * 注册接口编写
     *
     * 在gulimall-auth-server服务中编写注册的主体逻辑：
     *
     * 若JSR303校验未通过，则通过BindingResult封装错误信息，并重定向至注册页面
     * 若通过JSR303校验，则需要从redis中取值判断验证码是否正确，正确的话通过会员服务注册
     * 会员服务调用成功则重定向至登录页，否则封装远程服务返回的错误信息返回至注册页面
     *
     *       <dependency>
     *             <groupId>org.springframework.boot</groupId>
     *             <artifactId>spring-boot-starter-validation</artifactId>
     *         </dependency>

     * RedirectAttributes可以通过session保存信息并在重定向的时候携带过去。
     * 重定向携带数据，利用session原理。将数据放在session中。只要跳到下一个页面，取出数据以后，session里面的数据就会删掉。
     * 使用 return "forward:/reg.html"; 会出现：
     * 问题：Request method 'POST' not supported的问题
     * 原因：用户注册-> /regist[post] ------>转发/reg.html (路径映射默认都是get方式访问的)
     * 校验出错转发到注册页
     */


    /**
     * 通过watermall-member会员服务注册逻辑
     *
     * 通过异常机制判断当前注册会员名和电话号码是否已经注册，如果已经注册，则抛出对应的自定义异常，并在返回时封装对应的错误信息
     * 如果没有注册，则封装传递过来的会员信息，并设置默认的会员等级、创建时间
     * 修改“com.atguigu.watermall.member.controller.MemberController” 类，代码如下
     */
