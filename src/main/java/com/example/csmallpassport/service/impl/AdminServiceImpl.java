package com.example.csmallpassport.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.csmallpassport.ex.ServiceException;
import com.example.csmallpassport.mapper.AdminMapper;
import com.example.csmallpassport.pojo.dto.AdminAddNewDTO;
import com.example.csmallpassport.pojo.dto.AdminLoginDTO;
import com.example.csmallpassport.pojo.entity.Admin;
import com.example.csmallpassport.pojo.vo.AdminListItemVO;
import com.example.csmallpassport.pojo.vo.AdminStandardVO;
import com.example.csmallpassport.security.AdminDetails;
import com.example.csmallpassport.service.IAdminService;
import com.example.csmallpassport.web.ServiceCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {

    @Value("${csmall.jwt.secret-key}")
    private String secretKey;

    @Value("${csmall.jwt.duration-in-minute}")
    private Long durationInMinute;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AdminMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void setEnable(Long id) {
        updateEnableById(id,1);
    }

    @Override
    public void setDisable(Long id) {
        updateEnableById(id,0);
    }

    private void updateEnableById(Long id, Integer enable){
        log.debug("开始处理【{}管理员】的业务，ID：{}，目标状态:{}",ENABLE_TEXT[enable],id,enable);
        if (id==1){
            String message = ENABLE_TEXT[enable] + "管理员失败，尝试访问的数据不存在!";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }

        AdminStandardVO currentAdmin = mapper.getStandardById(id);
        if (currentAdmin==null){
            String message = ENABLE_TEXT[enable] + "类别失败，尝试访问的数据不在!";
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }

        if (currentAdmin.getEnable()==enable){
            String message = ENABLE_TEXT[enable] + "类别失败，当前类别已处于【"+ ENABLE_TEXT[enable] +"】状态!";
            throw new ServiceException(ServiceCode.ERR_CONFLICT, message);
        }

        Admin admin = new Admin();
        admin.setId(id);
        admin.setEnable(enable);
        int rows = mapper.update(admin);
        if (rows!=1){
            String message = ENABLE_TEXT[enable] + "类别失败，服务器繁忙，请稍后再试!";
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }

    @Override
    public String login(AdminLoginDTO adminLoginDTO) {
        log.debug("开始处理【管理员登录】的业务，参数：{}",adminLoginDTO);

        //创建认证信息对象
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                adminLoginDTO.getUsername(), adminLoginDTO.getPassword()
        );
        //调用认证管理器进行认证
        Authentication authenticationResult = authenticationManager.authenticate(authentication);
        log.debug("验证登录成功!返回的Authentication为{}",authenticationResult);
//        //如果没有出异常 则表示验证登录成功 需要将认证信息存入到Security上下文
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        securityContext.setAuthentication(authenticationResult);

        //处理验证登录成功后的结果中的当事人
        Object principal = authenticationResult.getPrincipal();
        log.debug("获取验证登录成功后的结果中的当事人:{}",principal);
        AdminDetails adminDetails = (AdminDetails) principal;

        //需要写入JWT中的数据
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",adminDetails.getId());
        claims.put("username",adminDetails.getUsername());
        String authoritiesJsonString = JSON.toJSONString(adminDetails.getAuthorities());
        claims.put("authoritiesJsonString", authoritiesJsonString);
        log.debug("即将生成JWT数据，包含的账号信息是:{}",claims);

        //生成JWT 并返回JWT
        Date exp = new Date(System.currentTimeMillis()+durationInMinute*60*1000);
        String jwt = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        log.debug("生成了JWT数据，并将返回此JWT数据：{}", jwt);
        return jwt;
    }

    @Override
    public void addNew(AdminAddNewDTO adminAddNewDTO) {
        //检测用户名是否唯一
        String username = adminAddNewDTO.getUsername();
        int countByUsername = mapper.countByUserName(username);
        if (countByUsername>0){
            String message = "添加管理员失败，用户名已被占用!";
            throw new ServiceException(ServiceCode.ERR_CONFLICT, message);
        }

        //检测手机号是否唯一
        String phone = adminAddNewDTO.getPhone();
        int countByPhone = mapper.countByPhone(phone);
        if (countByPhone>0){
            String message = "添加管理员失败，手机号已被占用!";
            throw new ServiceException(ServiceCode.ERR_INSERT, message);
        }

        //检测电子邮箱是否唯一
        String email = adminAddNewDTO.getEmail();
        int countByEmail = mapper.countByEmail(email);
        if (countByEmail>0){
            String message = "添加管理员失败，邮箱已被占用!";
            throw new ServiceException(ServiceCode.ERR_INSERT,message);
        }

        Admin admin = new Admin();
        BeanUtils.copyProperties(adminAddNewDTO,admin);

        String rawPassword = admin.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        admin.setPassword(encodedPassword);

        int rows = mapper.insert(admin);
        if (rows!=1){
            String message = "添加管理员失败！ 服务器繁忙，请稍后再试!";
            throw new ServiceException(ServiceCode.ERR_INSERT, message);
        }
    }

    @Override
    public void delete(Long id) {
        log.debug("开始处理【根据id删除管理员】的业务，参数:{}",id);

        if (id==1){
            String message = "删除管理员失败，不可删除1号管理员!";
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }

        AdminStandardVO queryResult = mapper.getStandardById(id);
        if (queryResult==null){
            String message = "获取管理员信息失败，尝试访问的数据不存在!";
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,message);
        }
        int rows = mapper.deleteById(id);
        if (rows!=1){
            String message = "删除管理员失败，服务器繁忙，请稍后再试!";
            throw new ServiceException(ServiceCode.ERR_DELETE,message);
        }
    }

    @Override
    public AdminStandardVO getStandardById(Long id) {
        if (id==1){
            String message = "根据id查询管理员失败，不可查询1号管理员!";
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }
        AdminStandardVO queryResult = mapper.getStandardById(id);
        if (queryResult==null){
            String message = "根据id查找失败，访问的数据不存在!";
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,message);
        }
        return queryResult;
    }

    @Override
    public List<AdminListItemVO> list() {
        List<AdminListItemVO> list = mapper.list();
        Iterator<AdminListItemVO> iterator = list.iterator();
        while (iterator.hasNext()){
            AdminListItemVO itemVO = iterator.next();
            if (itemVO.getId()==1){
                iterator.remove();
                break;
            }
        }
        return list;
    }

}
