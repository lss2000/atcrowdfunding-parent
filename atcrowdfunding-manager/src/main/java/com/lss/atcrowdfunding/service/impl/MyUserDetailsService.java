package com.lss.atcrowdfunding.service.impl;

import com.lss.atcrowdfunding.bean.TAdmin;
import com.lss.atcrowdfunding.bean.TAdminExample;
import com.lss.atcrowdfunding.bean.TPermission;
import com.lss.atcrowdfunding.bean.TRole;
import com.lss.atcrowdfunding.mapper.TAdminMapper;
import com.lss.atcrowdfunding.mapper.TPermissionMapper;
import com.lss.atcrowdfunding.mapper.TRoleMapper;
import com.lss.atcrowdfunding.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private TAdminMapper tAdminMapper;

    @Autowired
    private TRoleMapper tRoleMapper;

    @Autowired
    private TPermissionMapper tPermissionMapper;


    /**
     *
     * @param username  ==>相当于loginacct
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //登录
        TAdminExample example=new TAdminExample();
        TAdminExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(username);
        List<TAdmin> tAdminList = tAdminMapper.selectByExample(example);
        TAdmin tAdmin=tAdminList.get(0);

        //角色和权限的集合
        Set<GrantedAuthority> authorities=new HashSet<>();

        //登录用户关联的所有的角色
        List<TRole> listRole= tRoleMapper.listRoleByAdminId(tAdmin.getId());
        //登录用户角色关联的所有权限

        List<TPermission> listPermission= tPermissionMapper.listPermissionByAdminId(tAdmin.getId());

        //组合 角色和权限
        for (TRole tRole : listRole) {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+tRole.getName()));
        }

        for (TPermission tPermission : listPermission) {
            if(StringUtil.isNotEmpty(tPermission.getName())){
                authorities.add(new SimpleGrantedAuthority(tPermission.getName()));

            }
        }


        return new User(tAdmin.getLoginacct().toString(),tAdmin.getUserpswd().toString(),authorities);
    }

}
