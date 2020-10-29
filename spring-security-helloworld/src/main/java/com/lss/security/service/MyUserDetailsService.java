package com.lss.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     *
     * @param username   等价于loginacct登录账号
     * @return   UserDetails  登录用户的账号信息和密码信息
     *
     * 把这些信息到数据库中查询
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String sql = "select * from t_admin where loginacct=?";
        //Map中有id loginacct  userpswd email
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, username);


        //获取登录账户的角色信息和权限信息 authorities用于存储权限
        Set<GrantedAuthority> authorities = new HashSet<>();

        //获取角色信息
        String roleSql = "select t_role.`name` from t_role INNER JOIN t_admin_role on(t_role.id=t_admin_role.roleid) " +
                "INNER JOIN t_admin on(t_admin.id=t_admin_role.adminid) where t_admin.id=? ";
        List<Map<String, Object>> roleList = jdbcTemplate.query(roleSql, new ColumnMapRowMapper(), map.get("id"));

        //获取权限信息
        String permissionSql = "select t_permission.`name` from t_permission INNER JOIN t_role_permission on(t_permission.id=t_role_permission.permissionid) INNER JOIN t_role on(t_role.id=t_role_permission.roleid) " +
                "INNER JOIN t_admin_role on(t_role.id=t_admin_role.roleid) where t_admin_role.adminid=?";
        List<Map<String, Object>> permissionList = jdbcTemplate.query(permissionSql, new ColumnMapRowMapper(), map.get("id"));

        for (Map<String, Object> roleMap : roleList) {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+roleMap.get("name").toString()));
        }

        for (Map<String, Object> permissionMap : permissionList) {
            authorities.add(new SimpleGrantedAuthority(permissionMap.get("name").toString()));
        }

        return new User(map.get("loginacct").toString(),map.get("userpswd").toString(), authorities);
    }
}
