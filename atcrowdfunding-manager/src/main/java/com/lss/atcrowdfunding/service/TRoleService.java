package com.lss.atcrowdfunding.service;

import com.lss.atcrowdfunding.bean.TRole;

import java.util.List;

public interface TRoleService {


    List<TRole> listTRole(String keyWord);

    int saveRole(TRole tRole);

    TRole getRole(Integer id);

    Integer updateRole(TRole tRole);

    int deleteBath(List<Integer> idsInt);

    int deleteRoleById(Integer id);
}
